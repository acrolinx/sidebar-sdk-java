/* Copyright (c) 2024 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class DirectoryComparer {
  public static void assertEquals(Path firstDirectory, Path secondDirectory) throws IOException {
    Validate.isDirectory(firstDirectory, "firstDirectory");
    Validate.isDirectory(secondDirectory, "secondDirectory");

    Files.walkFileTree(
        firstDirectory, ComparingFileVisitor.create(firstDirectory, secondDirectory));
    Files.walkFileTree(
        secondDirectory, ComparingFileVisitor.create(secondDirectory, firstDirectory));
  }

  private DirectoryComparer() {
    throw new IllegalStateException();
  }
}
