/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LoggingUtilsTest {
  @AfterEach
  void afterEach() {
    ((LoggerContext) LoggerFactory.getILoggerFactory()).stop();
    System.clearProperty("acrolog.level");
  }

  @Test
  void setupLogging() throws Exception {
    LoggingUtils.setupLogging("TEST");
    final String logFileLocation = LoggingUtils.getLogFileLocation();
    Assertions.assertTrue(logFileLocation.contains("TEST"));
  }

  @Test
  void defaultTestLevelIsINFO() throws Exception {
    LoggingUtils.setupLogging("TEST01");

    final Logger logger = LoggerFactory.getLogger(LoggingUtilsTest.class);
    final ch.qos.logback.classic.Logger rootLogger =
        (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    assertEquals(Level.INFO, rootLogger.getLevel());

    logger.debug("debug test");
    logger.warn("warning test");

    final List<String> logMessages = readAllLogMessages(LoggingUtils.getLogFileLocation());
    logMessages.stream()
        .forEach(
            logMessage -> {
              assertTrue(logMessage.contains("WARN"));
              assertTrue(logMessage.contains("warning test"));
            });
  }

  @Test
  void setLevelToDEBUG() throws Exception {
    System.setProperty("acrolog.level", "debug");
    LoggingUtils.setupLogging("TEST02");

    final ch.qos.logback.classic.Logger root =
        (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    assertEquals(Level.DEBUG, root.getLevel());

    final Logger logger = LoggerFactory.getLogger(LoggingUtilsTest.class);
    logger.debug("debug test1");

    final List<String> logMessages = readAllLogMessages(LoggingUtils.getLogFileLocation());
    logMessages.stream()
        .forEach(
            logMessage -> {
              assertTrue(logMessage.contains("DEBUG"));
              assertTrue(logMessage.contains("debug test1"));
            });
  }

  @Test
  void turnLoggingOff() throws Exception {
    System.setProperty("acrolog.level", "off");
    LoggingUtils.setupLogging("TEST03");

    final Logger logger = LoggerFactory.getLogger(LoggingUtilsTest.class);
    logger.info("debug test112");
    logger.error("error test112");

    final ch.qos.logback.classic.Logger root =
        (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
    assertEquals(Level.OFF, root.getLevel());

    final List<String> logMessages = readAllLogMessages(LoggingUtils.getLogFileLocation());
    logMessages.stream().forEach(logMessage -> assertEquals("", logMessage));
  }

  @Test
  void getLogFileWhenNoLoggingIsConfiguredReturnsEmpty() {
    LoggingUtils.resetLoggingContext();
    final String logFileLocation = LoggingUtils.getLogFileLocation();
    assertEquals("", logFileLocation);
  }

  private static List<String> readAllLogMessages(String logFileLocation)
      throws IOException, URISyntaxException {
    Assertions.assertNotNull(logFileLocation);
    return Files.readAllLines(Path.of(new URI(logFileLocation)), StandardCharsets.UTF_8);
  }
}
