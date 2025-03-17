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
    byte[] buffer1 = new byte[BUFFER_SIZE];
    byte[] buffer2 = new byte[BUFFER_SIZE];
    try (InputStream in1 = Files.newInputStream(path1);
        InputStream in2 = Files.newInputStream(path2); ) {
      long totalRead = 0;
      while (true) {
        int nRead1 = in1.readNBytes(buffer1, 0, BUFFER_SIZE);
        int nRead2 = in2.readNBytes(buffer2, 0, BUFFER_SIZE);

        int i = Arrays.mismatch(buffer1, 0, nRead1, buffer2, 0, nRead2);
        if (i > -1) {
          return totalRead + i;
        }
        if (nRead1 < BUFFER_SIZE) {
          // we've reached the end of the files, but found no mismatch
          return -1;
        }
        totalRead += nRead1;
      }
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
