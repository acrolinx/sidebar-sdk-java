
package com.acrolinx.sidebar.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartPageInstaller
{
    private static final Logger logger = LoggerFactory.getLogger(StartPageInstaller.class);
    private static final String serverSelectorDir = "acrolinx_start_page";

    protected static String getStartPageVersion()
    {
        String resourceName = "/server-selector/version.properties";
        Properties props = new Properties();
        InputStream resourceStream = StartPageInstaller.class.getResourceAsStream(resourceName);
        if (resourceStream != null) {
            try {
                props.load(resourceStream);
                return (String) props.get("version");
            } catch (IOException e) {
                logger.error("Could not read server selector version!");
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
            logger.error("Version properties file for server selector not found!");
        }
        return null;
    }

    /**
     * Extracts the Acrolinx start page to file system. Internal use only.
     * @throws URISyntaxException
     * @throws IOException
     */
    static public void exportStartPageResources() throws URISyntaxException, IOException
    {
        InputStream asset;
        logger.info("Exporting Server Selector Resources.");
        Path assetDir = getDefaultStartPageInstallLocation();
        try (BufferedReader listFile = new BufferedReader(new InputStreamReader(
                StartPageInstaller.class.getResourceAsStream("/server-selector/files.txt"), StandardCharsets.UTF_8))) {
            String assetResource;
            while ((assetResource = listFile.readLine()) != null) {
                Path assetFile = assetDir.resolve(assetResource.substring(1, assetResource.length()));
                if (assetFile != null) {
                    Path parent = assetFile.getParent();
                    if (parent != null && !Files.exists(parent)) {
                        Files.createDirectories(parent);
                    }
					asset = StartPageInstaller.class.getResourceAsStream("/server-selector" + assetResource);
					if (asset != null && !Files.exists(assetFile)) {
						Files.copy(asset, assetFile, StandardCopyOption.REPLACE_EXISTING);
					}
				}
            }
        }
    }

    private static Path getDefaultStartPageInstallLocation() throws URISyntaxException, IOException
    {
        Path userTempDirLocation = SidebarUtils.getUserTempDirLocation();
        String osName = System.getProperty("os.name");
        Path acrolinxDir;
        if (osName.toLowerCase().contains("mac") || osName.contains("windows")) {
            acrolinxDir = userTempDirLocation.resolve("Acrolinx");
        } else {
            acrolinxDir = userTempDirLocation.resolve("acrolinx");
        }
        Path serverSelectorDirectory = acrolinxDir.resolve(serverSelectorDir + "_" + getStartPageVersion());
        if (!Files.exists(serverSelectorDirectory)) {
            serverSelectorDirectory = Files.createDirectories(serverSelectorDirectory);
            logger.debug("Creating acrolinx start page directory in: " + serverSelectorDirectory.toString());
        }
        return serverSelectorDirectory;
    }

    /**
     * Returns the URI to the extracted start page.
     * @return Path to current start page.
     * @throws IOException
     * @throws URISyntaxException
     */
    public static String getStartPageURL() throws IOException, URISyntaxException
    {
        Path assetDir = getDefaultStartPageInstallLocation();
        if (!Files.exists(assetDir.resolve("index.html"))) {
            logger.debug("Acrolinx start page not present!");
            exportStartPageResources();
        }
        return assetDir.toUri().toString() + "index.html";
    }
}
