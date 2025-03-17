/* Copyright (c) 2025 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class FileUtilsTest {

  @Test
  void extractZipFileTest() throws Exception {
    Path zipFilePath = Path.of("src/test/resources/sidebar-startpage.zip");
    Path tempDirectory = Files.createTempDirectory(null);

    FileUtils.extractZipFile(zipFilePath, tempDirectory);

    DirectoryComparer.assertEquals(Path.of("src/test/resources/sidebar-startpage"), tempDirectory);
  }
}
