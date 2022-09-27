/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acrolinx.sidebar.pojo.settings.AcrolinxSidebarInitParameter;
import com.google.common.base.Strings;

@SuppressWarnings("WeakerAccess")
public class StartPageInstaller
{
    private static final Logger logger = LoggerFactory.getLogger(StartPageInstaller.class);
    private static final String SERVER_SELECTOR_DIR = "acrolinx_start_page";

    protected static String getStartPageVersion()
    {
        final String resourceName = "/server-selector/version.properties";
        final Properties props = new Properties();
        final InputStream resourceStream = StartPageInstaller.class.getResourceAsStream(resourceName);
        if (resourceStream != null) {
            try {
                props.load(resourceStream);
                return (String) props.get("version");
            } catch (final IOException e) {
                logger.error("Could not read server selector version!");
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
            logger.error("Version properties file for server selector not found!");
        }
        return null;
    }

    /**
     * Extracts the Acrolinx start page to file system. Internal use only.
     * 
     * @throws URISyntaxException
     * @throws IOException
     */
    public static void exportStartPageResources() throws URISyntaxException, IOException
    {
        InputStream asset;
        logger.info("Exporting Server Selector Resources.");
        final Path assetDir = getDefaultStartPageInstallLocation();
        try (BufferedReader listFile = new BufferedReader(new InputStreamReader(
                StartPageInstaller.class.getResourceAsStream("/server-selector/files.txt"), StandardCharsets.UTF_8))) {
            String assetResource;
            while ((assetResource = listFile.readLine()) != null) {
                final Path assetFile = assetDir.resolve(assetResource.substring(1, assetResource.length()));
                if (assetFile != null) {
                    final Path parent = assetFile.getParent();
                    if ((parent != null) && !Files.exists(parent)) {
                        Files.createDirectories(parent);
                    }
                    asset = StartPageInstaller.class.getResourceAsStream("/server-selector" + assetResource);
                    if ((asset != null) && (true || !Files.exists(assetFile))) {
                        Files.copy(asset, assetFile, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
        }
    }

    private static Path getDefaultStartPageInstallLocation() throws URISyntaxException, IOException
    {
        final Path userTempDirLocation = SidebarUtils.getUserTempDirLocation();
        final String osName = System.getProperty("os.name");
        Path acrolinxDir;
        if (osName.toLowerCase().contains("mac") || osName.toLowerCase().contains("darwin") || osName.contains("windows")) {
            acrolinxDir = userTempDirLocation.resolve("Acrolinx");
        } else {
            acrolinxDir = userTempDirLocation.resolve("acrolinx");
        }
        Path serverSelectorDirectory = acrolinxDir.resolve(SERVER_SELECTOR_DIR + "_" + getStartPageVersion());
        if (!Files.exists(serverSelectorDirectory)) {
            serverSelectorDirectory = Files.createDirectories(serverSelectorDirectory);
            logger.debug("Creating acrolinx start page directory in: " + serverSelectorDirectory.toString());
        }
        return serverSelectorDirectory;
    }

    /**
     * Returns the URI to the extracted start page.
     * 
     * @return Path to current start page.
     * @throws IOException
     * @throws URISyntaxException
     */
    public static String getStartPageURL() throws IOException, URISyntaxException
    {
        final Path assetDir = getDefaultStartPageInstallLocation();
        if (!Files.exists(assetDir.resolve("index.html"))) {
            logger.debug("Acrolinx start page not present!");
            exportStartPageResources();
        }
        return assetDir.toUri().toString() + "index.html";
    }

    public static String prepareSidebarUrl(final AcrolinxSidebarInitParameter initParam)
    {
        try {
            if (!isExportRequired(initParam)) {
                return initParam.getSidebarUrl();
            }

            return getStartPageURL();
        } catch (final Exception e) {
            logger.error("Error getting sidebarURL", e);
            return "";
        }
    }

    public static boolean isExportRequired(final AcrolinxSidebarInitParameter initParam)
    {
        return true || initParam.getShowServerSelector() || Strings.isNullOrEmpty(initParam.getSidebarUrl());
    }
}
