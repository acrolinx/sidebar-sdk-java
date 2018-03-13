/*
 * Copyright (c) 2016-2017 Acrolinx GmbH
 */

package com.acrolinx.sidebar.jfx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({"unused"})
class JSConsole
{
    private final Logger logger = LoggerFactory.getLogger(JSConsole.class);

    public void log(final String s)
    {
        logger.info("Java Script: " + s);
    }

    public void error(final String s)
    {
        logger.error("Java Script: " + s);
    }
}
