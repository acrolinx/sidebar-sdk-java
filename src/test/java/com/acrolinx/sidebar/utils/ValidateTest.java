/* Copyright (c) 2023 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class ValidateTest {
  private static final String VARIABLE_NAME = "extracted-test-zip";

  @Test
  void isDirectoryTest() {
    Validate.isDirectory(Path.of("src/test"), VARIABLE_NAME);

    AssertThrows.illegalArgumentException(
        () -> Validate.isDirectory(null, VARIABLE_NAME), "directoryPath must not be null");
    AssertThrows.illegalArgumentException(
        () ->
            Validate.isDirectory(
                Path.of("src/test/resources/extracted-test-zip/foo.txt"), VARIABLE_NAME),
        "extracted-test-zip is not a directory: src/test/resources/extracted-test-zip/foo.txt");
  }

  @Test
  void isRegularFileTest() {
    Validate.isRegularFile(Path.of("src/test/resources/extracted-test-zip/foo.txt"), VARIABLE_NAME);

    AssertThrows.illegalArgumentException(
        () -> Validate.isRegularFile(null, VARIABLE_NAME), "extracted-test-zip must not be null");

    final Path notExistingFile = Path.of("this/file/does/not/exist");
    AssertThrows.illegalArgumentException(
        () -> Validate.isRegularFile(notExistingFile, VARIABLE_NAME),
        "extracted-test-zip is not a regular file: " + notExistingFile);

    final Path directoryFile = Path.of("src/test");
    AssertThrows.illegalArgumentException(
        () -> Validate.isRegularFile(directoryFile, VARIABLE_NAME),
        "extracted-test-zip is not a regular file: " + directoryFile);
  }

  @Test
  void notNullTest() {
    Validate.notNull("", VARIABLE_NAME);

    AssertThrows.illegalArgumentException(
        () -> Validate.notNull(null, VARIABLE_NAME), "extracted-test-zip must not be null");
  }
}
