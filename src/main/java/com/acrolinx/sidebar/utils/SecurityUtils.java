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

    private static void logPropertyValue(String propertyName, String propertyValue){
        logger.info("Property " + propertyName + " is set to " + propertyValue);
    }

    public static void setUpEnvironment()
    {
        String propertyNameAllowHeaders = "sun.net.http.allowRestrictedHeaders";
        String propertyRH = System.getProperty(propertyNameAllowHeaders);
        logPropertyValue(propertyNameAllowHeaders, propertyRH);

        String propertyNameHTTPProtocols = "https.protocols";
        String property = System.getProperty(propertyNameHTTPProtocols);
        logPropertyValue(propertyNameHTTPProtocols, property);

        String propertyNameGSS = "sun.security.jgss.native";
        String propertyGSS = System.getProperty(propertyNameGSS);
        logPropertyValue(propertyNameGSS, propertyGSS);

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
