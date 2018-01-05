/*
 * Copyright (c) 2016-2017 Acrolinx GmbH
 */

package com.acrolinx.sidebar.utils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acrolinx.sidebar.pojo.settings.SoftwareComponent;
import com.acrolinx.sidebar.pojo.settings.SoftwareComponentCategory;

@SuppressWarnings("unused")
public class SidebarUtils
{
    private static final Logger logger = LoggerFactory.getLogger(SidebarUtils.class);

    public static final String sidebarErrorHTML = "<!DOCTYPE html>\n" + "<html lang=\"en\">\n" + "<head>\n"
            + "  <meta charset=\"UTF-8\">\n" + "  <title>Error while trying to load sidebar</title>\n" + "</head>\n"
            + "<body>\n" + "Sidebar start page failed to load. Please check log files: \n"
            + LoggingUtils.getLogFileLocation() + "</body>\n" + "</html>";

    /**
     * Opens the given URL in the default Browser of the current OS.
     * Note that this method is likely to cause JVM crashes within SWT-based applications!
     *
     * @param url
     */
    public static void openWebPageInDefaultBrowser(String url)
    {
        try {
            openURIInDefaultBrowser(new URI(url));
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Validates a URL. Local URLs are allowed.
     * @param url
     * @return true if url is valid
     */
    public static boolean isValidURL(String url)
    {
        UrlValidator urlValidator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS);
        boolean valid = urlValidator.isValid(url);
        // workaround for data pipe server
        if (!valid) {
            String tldString = getTldString(url);
            if (tldString != null && tldString.length() > 0) {
                boolean validTld = DomainValidator.getInstance().isValidTld(tldString);
                if (!validTld) {
                    if (tldString.equals("cloud")) {
                        String replace = url.replace(".cloud", ".de");
                        valid = urlValidator.isValid(replace);
                    }
                }
            }
        }
        return valid;
    }

    private static String getTldString(String urlString)
    {
        URL url = null;
        String tldString = null;
        if (urlString != null && urlString.length() > 0) {
            try {

                url = new URL(urlString);
                String[] domainNameParts = url.getHost().split("\\.");
                tldString = domainNameParts[domainNameParts.length - 1];
            } catch (MalformedURLException e) {
                logger.error("Non valid URL", e);
            }
        }
        return tldString;
    }

    private static void openURIInDefaultBrowser(URI url)
    {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            new Thread(() -> {
                try {
                    Desktop.getDesktop().browse(url);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }).start();
        } else {
            logger.error("Desktop is not available to get systems default browser.");
        }
    }

    /**
     * Opens the log file. For internal use.
     * Attempts to open and preselect log file in systems file manager (only for mac os and windows).
     * If that fails, it just shows the containing folder in the file manager.
     *
     */
    public static void openLogFile()
    {

        String logFile = new File(LoggingUtils.getLogFileLocation()).getPath();
        if (openSystemSpecific(logFile))
            return;
        openLogFileFolderInFileManger();
    }

    private static void openLogFileFolderInFileManger()
    {
        String folder = new File(LoggingUtils.getLogFileLocation()).getParent();
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.OPEN)) {
            new Thread(() -> {
                try {
                    Desktop.getDesktop().open(new File(folder));
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }).start();
        } else {
            logger.error("Desktop is not available to get systems default browser.");
        }
    }

    /**
     * Returns the sidebar URL for a given Acrolinx Server Address. For internal use.
     * @param serverAddress
     * @return
     */
    public static String getSidebarUrl(String serverAddress)
    {
        return serverAddress + (serverAddress.endsWith("/") ? "sidebar/v14/index.html" : "/sidebar/v14/index.html");

    }

    private static String getCurrentSDKImplementationVersion()
    {
        // Need version from properties file to get proper version number in case sdk gets packed into fat jar
        String versionFromPropertiesFile = getJavaSDKVersionFromPropertiesFile();
        if (versionFromPropertiesFile == null) {
            return SidebarUtils.class.getPackage().getImplementationVersion();
        } else
            return versionFromPropertiesFile;
    }

    private static String getJavaSDKVersionFromPropertiesFile()
    {
        String resourceName = "/versionJavaSDK.properties";
        Properties props = new Properties();
        InputStream resourceStream = SidebarUtils.class.getResourceAsStream(resourceName);
        if (resourceStream != null) {
            try {
                props.load(resourceStream);
                return (String) props.get("VERSION_JAVA_SDK");
            } catch (IOException e) {
                logger.error("Could not read java sdk version!");
                logger.error(e.getMessage());
            } finally {
                try {
                    resourceStream.close();
                } catch (IOException e) {
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
     * Test if a sidebar is available for the given server address
     * @param serverAddress
     * @return true if sidebar is available
     */
    public static boolean isValidServerAddress(String serverAddress)
    {
        try {
            URL url = new URL(getSidebarUrl(serverAddress));
            URLConnection conn = url.openConnection();
            conn.connect();
        } catch (Exception e) {
            logger.error("Invalid Server URL!");
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    protected static Path getUserTempDirLocation() throws URISyntaxException
    {
        String s = System.getProperty("os.name").toLowerCase();
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
     * Attempts to show file in system specific file manager. Works only for mac and windows.
     * For internal use only.
     * @param path to file
     * @return
     */

    public static boolean openSystemSpecific(String path)
    {

        OSUtils.EnumOS os = OSUtils.getOS();

        if (os.isMac()) {
            if (runCommand("open", "-R", path))
                return true;
        }

        if (os.isWindows()) {
            if (runCommand("explorer", "/select", path))
                return true;
        }

        return false;
    }

    private static boolean runCommand(String command, String args, String file)
    {

        logger.info("Trying to exec:\n   cmd = " + command + "\n   args = " + args + "\n   %s = " + file);

        ArrayList<String> parts = new ArrayList<>();
        parts.add(command);

        if (args != null) {
            for (String s : args.split(" ")) {
                parts.add(s.trim());
            }
        }
        parts.add(args);
        parts.add(file);

        try {
            Process p = Runtime.getRuntime().exec(parts.toArray(new String[parts.size()]));
            try {
                int retval = p.exitValue();
                if (retval == 0) {
                    logger.error("Process ended immediately.");
                    return false;
                } else {
                    logger.error("Process crashed.");
                    return false;
                }
            } catch (IllegalThreadStateException e) {
                logger.debug("Process is running.", e);
                return true;
            }
        } catch (IOException e) {
            logger.error("Error running command.", e);
            return false;
        }
    }

}
