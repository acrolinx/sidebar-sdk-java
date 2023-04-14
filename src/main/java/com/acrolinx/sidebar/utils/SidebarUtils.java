/* Copyright (c) 2016-present Acrolinx GmbH */

package com.acrolinx.sidebar.utils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acrolinx.sidebar.pojo.settings.SoftwareComponent;
import com.acrolinx.sidebar.pojo.settings.SoftwareComponentCategory;

@SuppressWarnings({"WeakerAccess", "unused"})
public class SidebarUtils
{
    private static final Logger logger = LoggerFactory.getLogger(SidebarUtils.class);

    public static final String SIDEBAR_ERROR_HTML = "<!DOCTYPE html>\n" + "<html lang=\"en\">\n" + "<head>\n"
            + "  <meta charset=\"UTF-8\">\n" + "  <title>Error while trying to load sidebar</title>\n" + "</head>\n"
            + "<body>\n" + "Sidebar start page failed to load. Please check log files: \n"
            + LoggingUtils.getLogFileLocation() + "</body>\n" + "</html>";

    /**
     * Opens the given URL in the default Browser of the current OS. Note that this method is likely
     * to cause JVM crashes within SWT-based applications!
     *
     * @param url
     */
    public static void openWebPageInDefaultBrowser(final String url)
    {
        if (SidebarUtils.isValidURL(url)) {
            try {
                openURIInDefaultBrowser(new URI(url));
            } catch (final URISyntaxException e) {
                logger.error(e.getMessage());
            }
        } else {
            logger.warn("Attempt to open invalid URL: {}", url);
        }
    }

    /**
     * Validates a URL. Local URLs are allowed.
     *
     * @param url
     * @return true if url is valid
     */
    public static boolean isValidURL(final String url)
    {
        if ((url != null) && (url.length() > 0)) {
            boolean matches = url.matches("^(https?)://.*$");
            try {
                new URL(url);
                return matches;
            } catch (final MalformedURLException e) {
                logger.error("Non valid URL", e);
                return false;
            }
        }
        return false;
    }

    private static void openURIInDefaultBrowser(final URI url)
    {
        final Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if ((desktop != null) && desktop.isSupported(Desktop.Action.BROWSE)) {
            new Thread(() -> {
                try {
                    Desktop.getDesktop().browse(url);
                } catch (final Exception e) {
                    logger.error(e.getMessage());
                }
            }).start();
        } else {
            logger.error("Desktop is not available to get systems default browser.");
        }
    }

    /**
     * Opens the log file. For internal use. Attempts to open and preselect log file in systems file
     * manager (only for mac os and windows). If that fails, it just shows the containing folder in
     * the file manager.
     */
    public static void openLogFile()
    {

        final String logFileLocation = LoggingUtils.getLogFileLocation();
        if (logFileLocation != null) {
            final String logFile = new File(logFileLocation).getPath();
            if (openSystemSpecific(logFile)) {
                return;
            }
            openLogFileFolderInFileManger();
        }
    }

    private static void openLogFileFolderInFileManger()
    {
        final String logFileLocation = LoggingUtils.getLogFileLocation();
        if (logFileLocation != null) {
            final String folder = new File(logFileLocation).getParent();
            final Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if ((desktop != null) && desktop.isSupported(Desktop.Action.OPEN)) {
                new Thread(() -> {
                    try {
                        Desktop.getDesktop().open(new File(folder));
                    } catch (final Exception e) {
                        logger.error(e.getMessage());
                    }
                }).start();
            } else {
                logger.error("Desktop is not available to get systems default browser.");
            }
        }
    }

    /**
     * Returns the sidebar URL for a given Acrolinx Server Address. For internal use.
     *
     * @param serverAddress
     * @return sidebar url
     */
    public static String getSidebarUrl(final String serverAddress)
    {
        return serverAddress + (serverAddress.endsWith("/") ? "sidebar/v14/index.html" : "/sidebar/v14/index.html");

    }

    private static String getCurrentSDKImplementationVersion()
    {
        // Need version from properties file to get proper version number in case sdk gets packed
        // into fat jar
        final String versionFromPropertiesFile = getJavaSDKVersionFromPropertiesFile();
        if (versionFromPropertiesFile == null) {
            return SidebarUtils.class.getPackage().getImplementationVersion();
        } else {
            return versionFromPropertiesFile;
        }
    }

    private static String getJavaSDKVersionFromPropertiesFile()
    {
        final String resourceName = "/versionJavaSDK.properties";
        final Properties props = new Properties();
        final InputStream resourceStream = SidebarUtils.class.getResourceAsStream(resourceName);
        if (resourceStream != null) {
            try {
                props.load(resourceStream);
                return (String) props.get("VERSION_JAVA_SDK");
            } catch (final IOException e) {
                logger.error("Could not read java sdk version!");
                logger.error(e.getMessage());
            } finally {
                try {
                    resourceStream.close();
                } catch (final IOException e) {
                    logger.debug("Could not close resource stream or stream already cleaned up!");
                    logger.error(e.getMessage());
                }
            }
        } else {
            logger.error("Version properties file for Java SDK not found!");
        }
        return null;
    }

    /**
     * Test if a sidebar is available for the given server address.
     *
     * @param serverAddress
     * @return true if sidebar is available
     */
    public static boolean isValidServerAddress(final String serverAddress)
    {
        try {
            final URL url = new URL(getSidebarUrl(serverAddress));
            final URLConnection conn = url.openConnection();
            conn.connect();
        } catch (final Exception e) {
            logger.error("Invalid Server URL!");
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    protected static Path getUserTempDirLocation() throws URISyntaxException
    {
        final String s = System.getProperty("os.name").toLowerCase();
        String temDirProp;
        if (s.contains("mac")) {
            temDirProp = System.getProperty("user.home");
            return Paths.get(temDirProp, "Library");
        }
        return Paths.get(System.getProperty("java.io.tmpdir"));

    }

    /**
     * Internal use.
     */
    public static SoftwareComponent getJavaSDKSoftwareComponent()
    {
        return new SoftwareComponent("com.acrolinx.sidebar.java", "Java SDK", getCurrentSDKImplementationVersion(),
                SoftwareComponentCategory.DETAIL);
    }

    /**
     * Attempts to show file in system specific file manager. Works only for mac and windows. For
     * internal use only.
     *
     * @param path to file
     * @return boolean
     */

    public static boolean openSystemSpecific(final String path)
    {

        final OSUtils.EnumOS os = OSUtils.getOS();

        if (os.isMac()) {
            if (runCommand("open", "-R", path)) {
                return true;
            }
        }

        if (os.isWindows()) {
            if (runCommand("explorer", "/select,", "\"" + path + "\"")) {
                return true;
            }
        }

        return false;
    }

    private static boolean runCommand(final String command, final String args, final String file)
    {

        logger.info("Trying to exec:\n   cmd = {} \n   args = {} \n   %s = {}", command, args, file);

        final ArrayList<String> parts = new ArrayList<>();
        parts.add(command);

        if (args != null) {
            for (final String s : args.split(" ")) {
                parts.add(s.trim());
            }
        }
        parts.add(args);
        parts.add(file);

        try {
            final Process p = Runtime.getRuntime().exec(parts.toArray(new String[parts.size()]));
            try {
                final int retval = p.exitValue();
                if (retval == 0) {
                    logger.error("Process ended immediately.");
                    return false;
                } else {
                    logger.error("Process crashed.");
                    return false;
                }
            } catch (final IllegalThreadStateException e) {
                logger.debug("Process is running.");
                return true;
            }
        } catch (final IOException e) {
            logger.error("Error running command.", e);
            return false;
        }
    }

    public static int getSystemJavaVersion()
    {
        String version = SidebarUtils.getFullCurrentJavaVersionString();
        if (version.startsWith("1.")) {
            version = version.substring(2, 3);
        } else {
            int dot = version.indexOf(".");
            if (dot != -1) {
                version = version.substring(0, dot);
            }
        }
        return Integer.parseInt(version);
    }

    public static String getSystemJavaVMName()
    {
        return System.getProperty("java.vm.name");
    }

    public static String getFullCurrentJavaVersionString()
    {
        return System.getProperty("java.version");
    }

    public static String getPathOfCurrentJavaJRE()
    {
        return System.getProperty("java.home");
    }
}
