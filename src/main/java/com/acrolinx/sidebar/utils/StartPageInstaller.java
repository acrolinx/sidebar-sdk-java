/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import com.acrolinx.sidebar.pojo.settings.AcrolinxSidebarInitParameter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StartPageInstaller {
  private static final Logger logger = LoggerFactory.getLogger(StartPageInstaller.class);

  /** Extracts the Acrolinx start page to file system. Internal use only. */
  public static void exportStartPageResources() throws IOException, URISyntaxException {
    final Path defaultStartPageInstallLocation = getDefaultStartPageInstallLocation();

    URL sidebarStartPageZipUrl = StartPageInstaller.class.getResource("/sidebar-startpage.zip");

    if (sidebarStartPageZipUrl == null) {
      throw new IllegalStateException("Sidebar start page zip not found.");
    }

    FileUtils.extractZipFile(
        Path.of(sidebarStartPageZipUrl.toURI()), defaultStartPageInstallLocation);
  }

  private static Path getDefaultStartPageInstallLocation() throws IOException {
    final Path userTempDirLocation = SidebarUtils.getUserTempDirLocation();
    final String osName = System.getProperty("os.name");
    Path acrolinxDir = getAcrolinxDir(userTempDirLocation, osName);

    Path defaultStartPageInstallLocation =
        acrolinxDir.resolve(
            "acrolinx_start_page" + '_' + SidebarUtils.getCurrentSdkImplementationVersion());

    if (!Files.exists(defaultStartPageInstallLocation)) {
      Files.createDirectories(defaultStartPageInstallLocation);
      logger.debug(
          "Creating acrolinx start page directory in: {}", defaultStartPageInstallLocation);
    }

    return defaultStartPageInstallLocation;
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
  public static String getStartPageUrl() throws IOException, URISyntaxException {
    final Path assetDir = getDefaultStartPageInstallLocation();

    if (!Files.exists(assetDir.resolve("index.html"))) {
      logger.debug("Acrolinx start page not present!");
      exportStartPageResources();
    }

    return assetDir.toUri() + "index.html";
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
