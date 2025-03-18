/* Copyright (c) 2023 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

/**
 * A utility class containing only static methods to quickly check constructor and method arguments.
 */
public final class Validate {
  /**
   * @throws IllegalArgumentException if {@code directoryPath} is {@code null} or {@link
   *     Files#isDirectory(Path, java.nio.file.LinkOption...) not a directory}.
   */
  public static void isDirectory(Path directoryPath, String variableName) {
    notNull(directoryPath, "directoryPath");

    if (!Files.isDirectory(directoryPath)) {
      throw new IllegalArgumentException(variableName + " is not a directory: " + directoryPath);
    }
  }

  /**
   * @throws IllegalArgumentException if {@code filePath} is {@code null} or {@link
   *     Files#isRegularFile(Path, java.nio.file.LinkOption...) not a regular file}.
   */
  public static void isRegularFile(Path filePath, String variableName) {
    notNull(filePath, variableName);

    if (!Files.isRegularFile(filePath)) {
      throw new IllegalArgumentException(variableName + " is not a regular file: " + filePath);
    }
  }

  /**
   * @throws IllegalArgumentException if {@code string} is {@code null} or does not {@link
   *     Pattern#matches match} the given {@code pattern}.
   */
  public static void matches(String string, Pattern pattern, String variableName) {
    notNull(string, variableName);

    if (!pattern.matcher(string).matches()) {
      throw new IllegalArgumentException(
          variableName + " must match pattern '" + pattern.pattern() + "': " + string);
    }
  }

  /**
   * @throws IllegalArgumentException if {@code object} is {@code null}.
   */
  public static void notNull(Object object, String variableName) {
    if (object == null) {
      throw new IllegalArgumentException(variableName + " must not be null");
    }
  }

  private Validate() {
    throw new IllegalStateException();
  }
}
