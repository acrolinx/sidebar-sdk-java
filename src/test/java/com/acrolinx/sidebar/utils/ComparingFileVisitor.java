/* Copyright (c) 2024 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import org.opentest4j.AssertionFailedError;

final class ComparingFileVisitor extends SimpleFileVisitor<Path> {
  private static final int BUFFER_SIZE = 8192;

  static FileVisitor<Path> create(Path firstDirectory, Path secondDirectory) {
    Validate.isDirectory(firstDirectory, "firstDirectory");
    Validate.isDirectory(secondDirectory, "secondDirectory");

    return new ComparingFileVisitor(firstDirectory, secondDirectory);
  }

  private static long mismatch(Path path1, Path path2) throws IOException {
    byte[] buffer1;
    byte[] buffer2;
    try (InputStream in1 = Files.newInputStream(path1);
        InputStream in2 = Files.newInputStream(path2); ) {

      buffer1 = in1.readAllBytes();
      buffer2 = in2.readAllBytes();

      if (Arrays.equals(buffer1, buffer2)) {
        return -1;
      }

      return 0;
    }
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

    final long mismatch = mismatch(fileInFirstDirectory, fileInSecondDirectory);

    if (mismatch != -1) {
      throw new AssertionFailedError(
          "file mismatch at byte "
              + mismatch
              + ": "
              + fileInFirstDirectory
              + ", "
              + fileInSecondDirectory);
    }

    return FileVisitResult.CONTINUE;
  }
}
