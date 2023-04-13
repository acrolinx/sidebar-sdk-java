/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

public class LoggingUtilsTest
{
    @Test
    public void setupLogging() throws Exception
    {
        LoggingUtils.setupLogging("TEST");
        String logFileLocation = LoggingUtils.getLogFileLocation();
        Assert.assertTrue(logFileLocation.contains("TEST"));
        ((LoggerContext) LoggerFactory.getILoggerFactory()).stop();
        Files.deleteIfExists(Paths.get(new URI(logFileLocation)));
    }

    @Test
    public void defaultTestLevelIsINFO() throws Exception
    {
        LoggingUtils.setupLogging("TEST01");
        final Logger logger = LoggerFactory.getLogger("LoggingUtilsTest");
        final ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(
                Logger.ROOT_LOGGER_NAME);
        final Level level = root.getLevel();
        assertEquals(Level.INFO, level);

        logger.debug("debug test");
        logger.warn("warning test");
        final String logFileLocation = LoggingUtils.getLogFileLocation();
        Assert.assertNotNull(logFileLocation);

        final List<String> strings = Files.readAllLines(Paths.get(new URI(logFileLocation)), StandardCharsets.UTF_8);
        strings.stream().forEach(string -> {
            assertTrue(string.contains("WARN"));
            assertTrue(string.contains("warning test"));
        });

        ((LoggerContext) LoggerFactory.getILoggerFactory()).stop();
        Files.deleteIfExists(Paths.get(new URI(logFileLocation)));
    }

    @Test
    public void setLevelToDEBUG() throws Exception
    {
        System.setProperty("acrolog.level", "debug");
        LoggingUtils.setupLogging("TEST02");
        final ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(
                Logger.ROOT_LOGGER_NAME);
        final Level level = root.getLevel();
        assertEquals(Level.DEBUG, level);

        final Logger logger = LoggerFactory.getLogger("LoggingUtilsTest");
        logger.debug("debug test1");
        final String logFileLocation = LoggingUtils.getLogFileLocation();
        Assert.assertNotNull(logFileLocation);

        final List<String> strings = Files.readAllLines(Paths.get(new URI(logFileLocation)), StandardCharsets.UTF_8);
        strings.stream().forEach(string -> {
            assertTrue(string.contains("DEBUG"));
            assertTrue(string.contains("debug test1"));
        });

        ((LoggerContext) LoggerFactory.getILoggerFactory()).stop();
        Files.deleteIfExists(Paths.get(new URI(logFileLocation)));
        System.clearProperty("acrolog.level");
    }

    @Test
    public void turnLoggingOff() throws Exception
    {
        System.setProperty("acrolog.level", "off");
        LoggingUtils.setupLogging("TEST03");
        final Logger logger = LoggerFactory.getLogger("LoggingUtilsTest");
        logger.info("debug test112");
        logger.error("error test112");
        final ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(
                Logger.ROOT_LOGGER_NAME);
        final Level level = root.getLevel();
        assertEquals(Level.OFF, level);

        final String logFileLocation = LoggingUtils.getLogFileLocation();
        Assert.assertNotNull(logFileLocation);

        final List<String> strings = Files.readAllLines(Paths.get(new URI(logFileLocation)), StandardCharsets.UTF_8);
        strings.stream().forEach(string -> assertEquals("", string));
        ((LoggerContext) LoggerFactory.getILoggerFactory()).stop();
        Files.deleteIfExists(Paths.get(new URI(logFileLocation)));
        System.clearProperty("acrolog.level");
    }

    @Test
    public void getLogFileWhenNoLoggingIsConfiguredReturnsEmpty()
    {
        LoggingUtils.resetLoggingContext();
        final String logFileLocation = LoggingUtils.getLogFileLocation();
        assertEquals("", logFileLocation);
    }
}
