
package com.acrolinx.sidebar.jfx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class JSLogger
{
    private final Logger logger = LoggerFactory.getLogger(JSLogger.class);

    public void log(String args)
    {
        logger.info(args);
    }

    public void error(String args)
    {
        logger.error(args);
    }
}
