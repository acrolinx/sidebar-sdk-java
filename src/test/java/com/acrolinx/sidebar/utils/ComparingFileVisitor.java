/* Copyright (c) 2024 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import org.opentest4j.AssertionFailedError;

final class ComparingFileVisitor extends SimpleFileVisitor<Path> {
  static FileVisitor<Path> create(Path firstDirectory, Path secondDirectory) {
    Validate.isDirectory(firstDirectory, "firstDirectory");
    Validate.isDirectory(secondDirectory, "secondDirectory");

    return new ComparingFileVisitor(firstDirectory, secondDirectory);
  }

  private final Path firstDirectory;
  private final Path secondDirectory;

  private ComparingFileVisitor(Path firstDirectory, Path secondDirectory) {
    this.firstDirectory = firstDirectory;
    this.secondDirectory = secondDirectory;
  }

  @Override
  public FileVisitResult visitFile(
      Path fileInFirstDirectory, BasicFileAttributes basicFileAttributes) throws IOException {
    Validate.notNull(fileInFirstDirectory, "fileInFirstDirectory");
    Validate.notNull(basicFileAttributes, "basicFileAttributes");

    final Path fileInSecondDirectory =
        secondDirectory.resolve(firstDirectory.relativize(fileInFirstDirectory));

    byte[] buffer1 = Files.readAllBytes(fileInFirstDirectory);
    byte[] buffer2 = Files.readAllBytes(fileInSecondDirectory);

    if (!Arrays.equals(buffer1, buffer2)) {
      throw new AssertionFailedError(
          "files mismatch: " + fileInFirstDirectory + " != " + fileInSecondDirectory);
    }

    return FileVisitResult.CONTINUE;
  }
}
