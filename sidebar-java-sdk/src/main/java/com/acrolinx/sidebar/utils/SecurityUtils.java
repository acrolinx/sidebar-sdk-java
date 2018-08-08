/* Copyright (c) 2017-present Acrolinx GmbH */


package com.acrolinx.sidebar.utils;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * For internal use.
 */
public class SecurityUtils
{
    private static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);
    final private static String ACRO_PREFIX = "acrolinx_force_";

    public static void setUpEnvironment()
    {
        String propertyRH = System.getProperty("sun.net.http.allowRestrictedHeaders");
        logger.info("Property sun.net.http.allowRestrictedHeaders" + " set to " + propertyRH);

        String property = System.getProperty("https.protocols");
        logger.info("Property https.protocols" + " set to " + property);

        String propertyGSS = System.getProperty("sun.security.jgss.native");
        logger.info("Property sun.security.jgss.native" + " set to " + propertyGSS);

        Properties systemProperties = (Properties) System.getProperties().clone();
        systemProperties.forEach((k, v) -> {
            String key = k.toString().toLowerCase();
            if (key.startsWith(ACRO_PREFIX)) {
                logger.info("Setting up environment properties enforced by configuration.");
                logger.info(key + ": " + v.toString());
                System.setProperty(key.substring(ACRO_PREFIX.length()), v.toString());
                logger.info(key.substring(ACRO_PREFIX.length()) + ": "
                        + System.getProperty(key.substring(ACRO_PREFIX.length())));
            }
        });
    }
}
