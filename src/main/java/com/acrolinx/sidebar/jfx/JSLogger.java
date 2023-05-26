/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.jfx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSLogger
{
    private static final Logger logger = LoggerFactory.getLogger(JSLogger.class);

    public void log(String message)
    {
        logger.info(message);
    }

    public void error(String message)
    {
        logger.error(message);
    }
}
