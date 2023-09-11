/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.joran.spi.JoranException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Objects;
import org.slf4j.LoggerFactory;

/**
 * Set up the integration's logging.
 */
public final class LoggingUtils
{
    private LoggingUtils()
    {
        throw new IllegalStateException();
    }

    private static Path getLogFilePathOsSpecific(String applicationName)
    {
        Path userTempDirLocation = SidebarUtils.getUserTempDirLocation();
        String propertyValueString = System.getProperty("os.name").toLowerCase();

        if (propertyValueString.contains("mac")) {
            return Paths.get(userTempDirLocation.toString(), "Logs", "Acrolinx", applicationName);
        }

        if (propertyValueString.contains("win")) {
            return Paths.get(userTempDirLocation.toString(), "Acrolinx", "Logs", applicationName);
        }

        return Paths.get(userTempDirLocation.toString(), "acrolinx", "logs", applicationName);
    }

    private static void loadLogFileConfig(InputStream inputStream, String applicationName)
            throws JoranException, IOException
    {
        if (!(LoggerFactory.getILoggerFactory() instanceof LoggerContext)) {
            if (inputStream != null) {
                inputStream.close();
            }

            return;
        }

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.reset();

        JoranConfigurator joranConfigurator = new JoranConfigurator();
        joranConfigurator.setContext(loggerContext);
        loggerContext.putProperty("application-name", applicationName);
        loggerContext.putProperty("log-dir-path", getLogFilePathOsSpecific(applicationName).toString());
        joranConfigurator.doConfigure(inputStream); // loads logback file
        inputStream.close();
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(
                org.slf4j.Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.toLevel(System.getProperty("acrolog.level"), Level.INFO));
    }

    /**
     * Call this method on your integration's startup. This will initialize the logs as specified in the
     * Acrolinx Coding Guidance.
     * 
     * @param applicationName the integration's name e.g. AcrolinxForEditorName
     */
    public static void setupLogging(String applicationName) throws IOException, JoranException
    {
        useDefaultLoggingConfig(applicationName);
    }

    private static void useDefaultLoggingConfig(String applicationName) throws IOException, JoranException
    {
        Objects.requireNonNull(applicationName, "application name should be set");

        InputStream inputStream = LoggingUtils.class.getResourceAsStream("/logback_default.xml");
        loadLogFileConfig(inputStream, applicationName);
    }

    /**
     * Retrieve the current path to the integrations log file. The path is OS-specific.
     *
     * @return path to the integrations logging file.
     */
    public static String getLogFileLocation()
    {
        String logFileLocation = "";
        File clientLogFile;
        FileAppender<?> fileAppender = null;

        if (!(LoggerFactory.getILoggerFactory() instanceof LoggerContext)) {
            return null;
        }

        LoggerContext lContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        for (ch.qos.logback.classic.Logger logger : lContext.getLoggerList()) {
            for (Iterator<Appender<ILoggingEvent>> index = logger.iteratorForAppenders(); index.hasNext();) {
                Object object = index.next();

                if (object instanceof FileAppender) {
                    fileAppender = (FileAppender<?>) object;
                }
            }
        }

        if (fileAppender != null) {
            clientLogFile = new File(fileAppender.getFile());
        } else {
            clientLogFile = null;
        }

        if (clientLogFile != null) {
            logFileLocation = clientLogFile.toURI().toString();
        }

        return logFileLocation;
    }

    /**
     * Resets the current Logging Context.
     */
    public static void resetLoggingContext()
    {
        if (!(LoggerFactory.getILoggerFactory() instanceof LoggerContext)) {
            return;
        }

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.reset();
    }
}
