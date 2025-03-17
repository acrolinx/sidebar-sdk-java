/* Copyright (c) 2023 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Collection;
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
   * @throws IllegalArgumentException if {@code duration} is {@code null}, {@link
   *     Duration#isNegative() negative} or {@link Duration#isZero() zero}.
   */
  public static void isPositive(Duration duration, String variableName) {
    notNull(duration, variableName);

    if (duration.isNegative() || duration.isZero()) {
      throw new IllegalArgumentException(variableName + " must be positive: " + duration);
    }
  }

  /**
   * @throws IllegalArgumentException if {@code intValue} is less than one.
   */
  public static void isPositive(int intValue, String variableName) {
    if (intValue < 1) {
      throw new IllegalArgumentException(variableName + " must be positive: " + intValue);
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
   * @see #isRegularFile(Path, String)
   * @see #matches(String, Pattern, String)
   */
  public static void isRegularFileWithMatchingName(
      Path filePath, Pattern pattern, String variableName) {
    isRegularFile(filePath, variableName);
    matches(filePath.getFileName().toString(), pattern, "fileName");
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
   * @throws IllegalArgumentException if {@code collection} is {@code null} or {@link
   *     Collection#isEmpty() empty}.
   */
  public static void notEmpty(Collection<?> collection, String variableName) {
    notNull(collection, variableName);

    if (collection.isEmpty()) {
      throw new IllegalArgumentException(variableName + " must not be empty");
    }
  }

  /**
   * @throws IllegalArgumentException if {@code string} is {@code null} or {@link String#isEmpty()
   *     empty}.
   */
  public static void notEmpty(String string, String variableName) {
    notNull(string, variableName);

    if (string.isEmpty()) {
      throw new IllegalArgumentException(variableName + " must not be empty");
    }
  }

  /**
   * @throws IllegalArgumentException if {@code intValue} is less than {@code otherIntValue}.
   */
  public static void notLessThan(int intValue, int otherIntValue, String variableName) {
    if (intValue < otherIntValue) {
      throw new IllegalArgumentException(
          variableName + " must not be less than " + otherIntValue + ": " + intValue);
    }
  }

  /**
   * @throws IllegalArgumentException if {@code intValue} is less than zero.
   */
  public static void notNegative(int intValue, String variableName) {
    if (intValue < 0) {
      throw new IllegalArgumentException(variableName + " must not be negative: " + intValue);
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
