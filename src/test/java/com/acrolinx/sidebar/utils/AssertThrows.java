/* Copyright (c) 2023 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import java.util.NoSuchElementException;
import java.util.Objects;
import org.opentest4j.AssertionFailedError;

/**
 * Utility class to test code that is supposed to throw an exception or error. Best used with Java 8
 * lambda expressions, e.g.:
 *
 * <pre>{@code
 * AssertThrows.illegalArgumentException(() -> someObject.someMethod(invalidParameter), "expected exception message");
 * }</pre>
 *
 * To test code that throws arbitrary exceptions, use the {@link #exception(Action, String, Class)}
 * method.
 */
public final class AssertThrows {
  public static <E extends Error> E error(
      Action action, String expectedMessage, Class<E> expectedErrorClass) {
    verifyNotNull(expectedErrorClass, "expectedErrorClass");

    return assertThrows(action, expectedMessage, expectedErrorClass);
  }

  public static <E extends Exception> E exception(
      Action action, String expectedMessage, Class<E> expectedExceptionClass) {
    verifyNotNull(expectedExceptionClass, "expectedExceptionClass");

    return assertThrows(action, expectedMessage, expectedExceptionClass);
  }

  public static IllegalArgumentException illegalArgumentException(
      Action action, String expectedMessage) {
    return assertThrows(action, expectedMessage, IllegalArgumentException.class);
  }

  public static IllegalStateException illegalStateException(Action action, String expectedMessage) {
    return assertThrows(action, expectedMessage, IllegalStateException.class);
  }

  public static NoSuchElementException noSuchElementException(
      Action action, String expectedMessage) {
    return assertThrows(action, expectedMessage, NoSuchElementException.class);
  }

  private static <T extends Throwable> T assertThrows(
      Action action, String expectedMessage, Class<T> expectedType) {
    verifyNotNull(action, "action");

    try {
      action.perform();
    } catch (Throwable e) {
      if (!expectedType.isAssignableFrom(e.getClass())) {
        throw createAssertionFailedError(
            "wrong type", expectedType.getSimpleName(), e.getClass().getSimpleName());
      }

      String actualMessage = e.getMessage();

      if (!Objects.equals(expectedMessage, actualMessage)) {
        throw createAssertionFailedError("wrong message", expectedMessage, actualMessage);
      }

      return expectedType.cast(e);
    }

    throw new AssertionFailedError(
        "expected <" + expectedType.getSimpleName() + "> with message <" + expectedMessage + '>');
  }

  private static AssertionFailedError createAssertionFailedError(
      String message, String expected, String actual) {
    return new AssertionFailedError(
        message + " ==> expected: <" + expected + "> but was: <" + actual + '>', expected, actual);
  }

  private static void verifyNotNull(Object object, String variableName) {
    if (object == null) {
      throw new IllegalArgumentException(variableName + " must not be null");
    }
  }

  private AssertThrows() {
    throw new IllegalStateException();
  }
}
