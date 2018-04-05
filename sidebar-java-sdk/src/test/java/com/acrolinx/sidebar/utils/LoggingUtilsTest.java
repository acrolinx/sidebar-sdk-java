
package com.acrolinx.sidebar.utils;

import static org.junit.Assert.assertTrue;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;

import ch.qos.logback.classic.Level;

public class LoggingUtilsTest
{
    @Test
    public void setupLogging() throws Exception
    {
        LoggingUtils.setupLogging("TEST");
        String logFileLocation = LoggingUtils.getLogFileLocation();
        Assert.assertTrue(logFileLocation != null);
        Assert.assertTrue(logFileLocation.contains("TEST"));
        Files.deleteIfExists(Paths.get(logFileLocation));
    }

    @Test
    public void defaultTestLevelIsINFO() throws Exception
    {
        LoggingUtils.setupLogging("TEST01");
        Logger logger = LoggerFactory.getLogger("LoggingUtilsTest");
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(
                ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        Level level = root.getLevel();
        assertTrue(level.toString().equalsIgnoreCase("INFO"));
        logger.debug("debug test");
        logger.warn("warning test");
        String logFileLocation = LoggingUtils.getLogFileLocation();
        Assert.assertTrue(logFileLocation != null);
        List<String> strings = Files.readAllLines(Paths.get(logFileLocation), Charsets.UTF_8);
        strings.stream().forEach(string -> {
            assertTrue(string.contains("WARN"));
            assertTrue(string.contains("warning test"));
        });
        Files.deleteIfExists(Paths.get(logFileLocation));
    }

    @Test
    public void setLevelToDEBUG() throws Exception
    {
        System.setProperty("acrolog.level", "debug");
        LoggingUtils.setupLogging("TEST02");
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(
                ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        Level level = root.getLevel();
        assertTrue(level.toString().equalsIgnoreCase("DEBUG"));
        Logger logger = LoggerFactory.getLogger("LoggingUtilsTest");
        logger.debug("debug test1");
        String logFileLocation = LoggingUtils.getLogFileLocation();
        Assert.assertTrue(logFileLocation != null);
        List<String> strings = Files.readAllLines(Paths.get(logFileLocation), Charsets.UTF_8);
        strings.stream().forEach(string -> {
            assertTrue(string.contains("DEBUG"));
            assertTrue(string.contains("debug test1"));
        });
        Files.deleteIfExists(Paths.get(logFileLocation));
        System.clearProperty("acrolog.level");
    }

    @Test
    public void turnLoggingOff() throws Exception
    {
        System.setProperty("acrolog.level", "off");
        LoggingUtils.setupLogging("TEST03");
        Logger logger = LoggerFactory.getLogger("LoggingUtilsTest");
        logger.info("debug test112");
        logger.error("error test112");
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(
                ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        Level level = root.getLevel();
        assertTrue(level.equals(Level.OFF));
        String logFileLocation = LoggingUtils.getLogFileLocation();
        Assert.assertTrue(logFileLocation != null);
        List<String> strings = Files.readAllLines(Paths.get(logFileLocation), Charsets.UTF_8);
        strings.stream().forEach(string -> assertTrue("".equals(string)));
        Files.deleteIfExists(Paths.get(logFileLocation));
        System.clearProperty("acrolog.level");
    }

    @Test
    public void getLogFileWhenNoLoggingIsConfiguredReturnsNull() throws Exception
    {
        LoggingUtils.resetLoggingContext();
        String logFileLocation = LoggingUtils.getLogFileLocation();
        assertTrue(logFileLocation == null);
    }

}