/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * For internal use.
 */
public final class SecurityUtils
{
    private static final Logger logger = LoggerFactory.getLogger(SecurityUtils.class);
    private static final String ACRO_PREFIX = "acrolinx_force_";

    private SecurityUtils()
    {
        throw new IllegalStateException();
    }

    private static void logPropertyValue(String propertyName, String propertyValue)
    {
        logger.info("Property {} is set to {}", propertyName, propertyValue);
    }

    public static void setUpEnvironment()
    {
        final String propertyNameAllowRestrictedHeaders = "sun.net.http.allowRestrictedHeaders";
        logPropertyValue(propertyNameAllowRestrictedHeaders, System.getProperty(propertyNameAllowRestrictedHeaders));

        final String propertyNameHttpProtocols = "https.protocols";
        logPropertyValue(propertyNameHttpProtocols, System.getProperty(propertyNameHttpProtocols));

        final String propertyNameGssNative = "sun.security.jgss.native";
        logPropertyValue(propertyNameGssNative, System.getProperty(propertyNameGssNative));

        Properties systemProperties = (Properties) System.getProperties().clone();
        systemProperties.forEach((keyObject, value) -> {
            String key = keyObject.toString().toLowerCase();

            if (key.startsWith(ACRO_PREFIX)) {
                logger.info("Setting up environment properties enforced by configuration.");
                logger.info("{} : {}", key, value);
                System.setProperty(key.substring(ACRO_PREFIX.length()), value.toString());
                logger.info("{} : {}", key.substring(ACRO_PREFIX.length()),
                        System.getProperty(key.substring(ACRO_PREFIX.length())));
            }
        });
    }
}
