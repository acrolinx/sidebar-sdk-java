/* Copyright (c) 2022-present Acrolinx GmbH */

package com.acrolinx.sidebar.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


@SuppressWarnings("WeakerAccess")
public class ReusePanelInstaller
{
    private static final Logger logger = LoggerFactory.getLogger(ReusePanelInstaller.class);
    private static final String REUSE_PANEL_DIR = "acrolinx_reuse_panel";

    protected static String getReusePanelVersion()
    {
        //ToDo: Generate version.properties file in reuse-panel if needed
        return "0.1.0";
    }

    /**
     * Extracts the Acrolinx reuse panel to file system. Internal use only.
     * 
     * @throws URISyntaxException
     * @throws IOException
     */
    public static void exportReusePanelResources() throws URISyntaxException, IOException
    {
        InputStream asset;
        logger.info("Exporting Reuse Panel Resources.");
        final Path assetDir = getDefaultReusePanelInstallLocation();
        try (BufferedReader listFile = new BufferedReader(new InputStreamReader(
                ReusePanelInstaller.class.getResourceAsStream("/reuse-panel/files.txt"), StandardCharsets.UTF_8))) {
            String assetResource;
            while ((assetResource = listFile.readLine()) != null) {
                final Path assetFile = assetDir.resolve(assetResource.substring(1, assetResource.length()));
                if (assetFile != null) {
                    final Path parent = assetFile.getParent();
                    if ((parent != null) && !Files.exists(parent)) {
                        Files.createDirectories(parent);
                    }
                    asset = ReusePanelInstaller.class.getResourceAsStream("/reuse-panel" + assetResource);
                    if ((asset != null) && (true || !Files.exists(assetFile))) {
                        Files.copy(asset, assetFile, StandardCopyOption.REPLACE_EXISTING);
                    }
                }
            }
        }
    }

    private static Path getDefaultReusePanelInstallLocation() throws URISyntaxException, IOException
    {
        final Path userTempDirLocation = SidebarUtils.getUserTempDirLocation();
        final String osName = System.getProperty("os.name");
        Path acrolinxDir;
        if (osName.toLowerCase().contains("mac") || osName.contains("windows")) {
            acrolinxDir = userTempDirLocation.resolve("Acrolinx");
        } else {
            acrolinxDir = userTempDirLocation.resolve("acrolinx");
        }
        Path reusePanelDirectory = acrolinxDir.resolve(REUSE_PANEL_DIR + "_" + getReusePanelVersion());
        if (!Files.exists(reusePanelDirectory)) {
            reusePanelDirectory = Files.createDirectories(reusePanelDirectory);
            logger.debug("Creating acrolinx reuse panel directory in: " + reusePanelDirectory.toString());
        }
        return reusePanelDirectory;
    }

    /**
     * Returns the URI to the extracted reuse panel.
     * 
     * @return Path to current reuse panel.
     * @throws IOException
     * @throws URISyntaxException
     */
    public static String getReusePanelURL()
    {
        try {
            final Path assetDir = getDefaultReusePanelInstallLocation();
            if (!Files.exists(assetDir.resolve("index.html"))) {
                logger.debug("Acrolinx reuse panel not present!");
                exportReusePanelResources();
            }
            return assetDir.toUri().toString() + "index.html";
        } catch (final Exception e) {
            logger.error("Error getting reuse panel URL", e);
            return "";
        }
    }
}
