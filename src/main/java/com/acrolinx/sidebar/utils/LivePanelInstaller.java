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
public class LivePanelInstaller
{
    private static final Logger logger = LoggerFactory.getLogger(LivePanelInstaller.class);
    private static final String LIVE_PANEL_DIR = "acrolinx_reuse_panel";

    protected static String getLivePanelVersion()
    {
        //ToDo: Generate version.properties file in live-panel if needed
        return "0.1.0";
    }

    /**
     * Extracts the Acrolinx live panel to file system. Internal use only.
     * 
     * @throws URISyntaxException
     * @throws IOException
     */
    public static void exportLivePanelResources() throws URISyntaxException, IOException
    {
        logger.info("Exporting Live Panel Resources.");
        final Path assetDir = getDefaultLivePanelInstallLocation();
        try(InputStreamReader inputStreamReader = new InputStreamReader(LivePanelInstaller.class.getResourceAsStream("/acrolinx-live/files.txt"), StandardCharsets.UTF_8);
            BufferedReader listFile = new BufferedReader(inputStreamReader);)  {
            String assetResource;
            while ((assetResource = listFile.readLine()) != null) {
                final Path assetFile = assetDir.resolve(assetResource.substring(1, assetResource.length()));
                if (assetFile != null) {
                    final Path parent = assetFile.getParent();
                    if ((parent != null) && !Files.exists(parent)) {
                        Files.createDirectories(parent);
                    }
                    try(InputStream asset = LivePanelInstaller.class.getResourceAsStream("/acrolinx-live" + assetResource)) {
                        if ((asset != null) && (true || !Files.exists(assetFile))) {
                            Files.copy(asset, assetFile, StandardCopyOption.REPLACE_EXISTING);
                        }
                    } catch (final Exception e) {
                        logger.error("Error in exportLivePanelResources", e);
                    }
                }
            }
        } catch (final Exception e) {
            logger.error("Error in exportLivePanelResources", e);
        }
    }

    private static Path getDefaultLivePanelInstallLocation() throws URISyntaxException, IOException
    {
        final Path userTempDirLocation = SidebarUtils.getUserTempDirLocation();
        final String osName = System.getProperty("os.name");
        Path acrolinxDir;
        if (osName.toLowerCase().contains("mac") || osName.toLowerCase().contains("darwin") || osName.contains("windows")) {
            acrolinxDir = userTempDirLocation.resolve("Acrolinx");
        } else {
            acrolinxDir = userTempDirLocation.resolve("acrolinx");
        }
        Path livePanelDirectory = acrolinxDir.resolve(LIVE_PANEL_DIR + "_" + getLivePanelVersion());
        if (!Files.exists(livePanelDirectory)) {
            livePanelDirectory = Files.createDirectories(livePanelDirectory);
            logger.debug("Creating acrolinx live panel directory in: " + livePanelDirectory.toString());
        }
        return livePanelDirectory;
    }

    /**
     * Returns the URI to the extracted live panel.
     * 
     * @return Path to current live panel.
     * @throws IOException
     * @throws URISyntaxException
     */
    public static String getLivePanelURL()
    {
        try {
            final Path assetDir = getDefaultLivePanelInstallLocation();
            if (!Files.exists(assetDir.resolve("index.html"))) {
                logger.debug("Acrolinx live panel not present!");
                exportLivePanelResources();
            }
            return assetDir.toUri().toString() + "index.html";
        } catch (final Exception e) {
            logger.error("Error getting live panel URL", e);
            return "";
        }
    }
}
