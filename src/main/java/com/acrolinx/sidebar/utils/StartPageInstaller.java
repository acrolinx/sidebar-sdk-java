/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import com.acrolinx.sidebar.pojo.settings.AcrolinxSidebarInitParameter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StartPageInstaller {
  private static final Logger logger = LoggerFactory.getLogger(StartPageInstaller.class);
  private static final String SERVER_SELECTOR_DIR = "acrolinx_start_page";

  static String getStartPageVersion() {
    final String resourceName = "/server-selector/version.properties";
    final Properties properties = new Properties();

    try (InputStream resourceStream = StartPageInstaller.class.getResourceAsStream(resourceName)) {
      properties.load(resourceStream);
      return properties.getProperty("version");
    } catch (final IOException e) {
      logger.error("Could not read server selector version!", e);
      return null;
    }
  }

  /** Extracts the Acrolinx start page to file system. Internal use only. */
  public static void exportStartPageResources() throws IOException {
    logger.info("Exporting Server Selector Resources.");
    final Path assetDir = getDefaultStartPageInstallLocation();

    try (BufferedReader bufferedReader =
        new BufferedReader(
            new InputStreamReader(
                StartPageInstaller.class.getResourceAsStream("/server-selector/files.txt"),
                StandardCharsets.UTF_8))) {
      String assetResource;

      while ((assetResource = bufferedReader.readLine()) != null) {
        final Path assetFile = assetDir.resolve(assetResource.substring(1, assetResource.length()));

        if (assetFile != null) {
          final Path parent = assetFile.getParent();

          if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent);
          }

          InputStream inputStream =
              StartPageInstaller.class.getResourceAsStream("/server-selector" + assetResource);

          if (inputStream != null && !Files.exists(assetFile)) {
            Files.copy(inputStream, assetFile, StandardCopyOption.REPLACE_EXISTING);
          }
        }
      }
    }
  }

  private static Path getDefaultStartPageInstallLocation() throws IOException {
    final Path userTempDirLocation = SidebarUtils.getUserTempDirLocation();
    final String osName = System.getProperty("os.name");
    Path acrolinxDir = getAcrolinxDir(userTempDirLocation, osName);

    Path serverSelectorDirectory =
        acrolinxDir.resolve(SERVER_SELECTOR_DIR + "_" + getStartPageVersion());

    if (!Files.exists(serverSelectorDirectory)) {
      serverSelectorDirectory = Files.createDirectories(serverSelectorDirectory);
      logger.debug("Creating acrolinx start page directory in: {}", serverSelectorDirectory);
    }

    return serverSelectorDirectory;
  }

  private static Path getAcrolinxDir(final Path userTempDirLocation, final String osName) {
    if (osName.toLowerCase().contains("mac") || osName.contains("windows")) {
      return userTempDirLocation.resolve("Acrolinx");
    }

    return userTempDirLocation.resolve("acrolinx");
  }

  /**
   * Returns the URI to the extracted start page.
   *
   * @return Path to current start page.
   */
  public static String getStartPageUrl() throws IOException {
    final Path assetDir = getDefaultStartPageInstallLocation();

    if (!Files.exists(assetDir.resolve("index.html"))) {
      logger.debug("Acrolinx start page not present!");
      exportStartPageResources();
    }

    return assetDir.toUri().toString() + "index.html";
  }

  public static String prepareSidebarUrl(
      final AcrolinxSidebarInitParameter acrolinxSidebarInitParameter) {
    try {
      if (!isExportRequired(acrolinxSidebarInitParameter)) {
        return acrolinxSidebarInitParameter.getSidebarUrl();
      }

      return getStartPageUrl();
    } catch (final Exception e) {
      logger.error("Error getting sidebarURL", e);
      return "";
    }
  }

  public static boolean isExportRequired(
      final AcrolinxSidebarInitParameter acrolinxSidebarInitParameter) {
    return acrolinxSidebarInitParameter.getShowServerSelector()
        || isNullOrEmpty(acrolinxSidebarInitParameter.getSidebarUrl());
  }

  private static boolean isNullOrEmpty(String string) {
    return string == null || string.isEmpty();
  }

  private StartPageInstaller() {
    throw new IllegalStateException();
  }
}
