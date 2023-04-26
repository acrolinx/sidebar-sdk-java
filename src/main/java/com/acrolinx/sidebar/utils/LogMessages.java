/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.utils;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;

/**
 * For internal use.
 */
public class LogMessages
{
    public static void logJavaVersionAndUIFramework(Logger logger, String uiFramework)
    {
        logger.info("Java Version: {} ; UI Framework: {}", System.getProperty("java.version"), uiFramework);
    }

    public static void logCheckRequested(Logger logger)
    {
        logger.info("Check requested.");
    }

    public static void logExternalContentRequested(Logger logger)
    {
        logger.info("External Content requested.");
    }

    public static void logCheckRejected(Logger logger)
    {
        logger.info("Check rejected.");
    }

    public static void logCheckFinishedWithDurationTime(Logger logger, long durationTimeInMS)
    {
        logger.info("Check finished. Check took {}", DurationFormatUtils.formatDuration(durationTimeInMS, "HH:mm:ss,SSS"));
    }

    public static void logSelectingRange(Logger logger)
    {
        logger.info("Request select range.");
    }

    public static void logReplacingRange(Logger logger)
    {
        logger.info("Request replace range.");
    }
}
