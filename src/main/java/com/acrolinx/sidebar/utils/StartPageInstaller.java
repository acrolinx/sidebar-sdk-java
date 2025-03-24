/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import com.acrolinx.sidebar.pojo.settings.AcrolinxSidebarInitParameter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StartPageInstaller {
  private static final String SIDEBAR_STARTPAGE_ZIP = "sidebar-startpage.zip";
  private static final Logger logger = LoggerFactory.getLogger(StartPageInstaller.class);

  /** Extracts the Acrolinx start page to file system. Internal use only. */
  public static void exportStartPageResources() throws IOException {
    try (InputStream inputStream =
        StartPageInstaller.class.getResourceAsStream('/' + SIDEBAR_STARTPAGE_ZIP)) {

      if (inputStream == null) {
        throw new IllegalStateException("Resource not found: " + SIDEBAR_STARTPAGE_ZIP);
      }

      final Path defaultStartPageInstallLocation = getDefaultStartPageInstallLocation();

      // Make sure we have a non-corrupted installation folder
      if (Files.exists(defaultStartPageInstallLocation)) {
        FileUtils.deleteDirectory(defaultStartPageInstallLocation);
      }

      createStartPageInstallationDirectory(defaultStartPageInstallLocation);

      final Path zipFilePath = defaultStartPageInstallLocation.resolve(SIDEBAR_STARTPAGE_ZIP);

      Files.copy(inputStream, zipFilePath);
      FileUtils.extractZipFile(zipFilePath, defaultStartPageInstallLocation);
    }
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

    return assetDir.toUri() + "index.html";
  }

  public static boolean isExportRequired(
      final AcrolinxSidebarInitParameter acrolinxSidebarInitParameter) {
    return acrolinxSidebarInitParameter.getShowServerSelector()
        || isNullOrEmpty(acrolinxSidebarInitParameter.getSidebarUrl());
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

  private static Path getAcrolinxDir(final Path userTempDirLocation, final String osName) {
    if (osName.toLowerCase().contains("mac") || osName.contains("windows")) {
      return userTempDirLocation.resolve("Acrolinx");
    }

    return userTempDirLocation.resolve("acrolinx");
  }

  private static Path getDefaultStartPageInstallLocation() {
    final Path userTempDirLocation = SidebarUtils.getUserTempDirLocation();
    final String osName = System.getProperty("os.name");
    final Path acrolinxDir = getAcrolinxDir(userTempDirLocation, osName);

    return acrolinxDir.resolve(
        "acrolinx_start_page" + '_' + SidebarUtils.getCurrentSdkImplementationVersion());
  }

  private static void createStartPageInstallationDirectory(Path path) throws IOException {
    if (!Files.exists(path)) {
      Files.createDirectories(path);
      logger.debug("Creating acrolinx start page directory in: {}", path);
    }
  }

  private static boolean isNullOrEmpty(String string) {
    return string == null || string.isEmpty();
  }

  private StartPageInstaller() {
    throw new IllegalStateException();
  }
}
