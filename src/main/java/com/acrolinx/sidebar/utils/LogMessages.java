/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import java.time.Duration;
import org.slf4j.Logger;

/** For internal use. */
public final class LogMessages {
  public static void logJavaVersionAndUiFramework(Logger logger, String uiFramework) {
    logger.info(
        "Java Version: {} ; UI Framework: {}", System.getProperty("java.version"), uiFramework);
  }

  public static void logCheckRequested(Logger logger) {
    logger.info("Check requested.");
  }

  public static void logExternalContentRequested(Logger logger) {
    logger.info("External Content requested.");
  }

  public static void logCheckRejected(Logger logger) {
    logger.info("Check rejected.");
  }

  public static void logCheckFinishedWithDurationTime(Logger logger, Duration elapsedTime) {
    logger.info("Check finished. Check took {}", elapsedTime);
  }

  public static void logSelectingRange(Logger logger) {
    logger.info("Request select range.");
  }

  public static void logReplacingRange(Logger logger) {
    logger.info("Request replace range.");
  }

  private LogMessages() {
    throw new IllegalStateException();
  }
}
