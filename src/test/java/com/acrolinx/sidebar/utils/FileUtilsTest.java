/* Copyright (c) 2025 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class FileUtilsTest {
  @Test
  void extractZipFileTest(@TempDir Path temporaryDirectory) throws Exception {
    Path zipFilePath = Path.of("src/test/resources/test.zip");

    FileUtils.extractZipFile(zipFilePath, temporaryDirectory);

    DirectoryComparer.assertEquals(
        Path.of("src/test/resources/extracted-test-zip"), temporaryDirectory);

    FileUtils.deleteDirectory(temporaryDirectory);

    Assertions.assertFalse(Files.exists(temporaryDirectory));
  }
}
