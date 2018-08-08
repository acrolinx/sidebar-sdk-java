/* Copyright (c) 2017-present Acrolinx GmbH */


package com.acrolinx.sidebar.localization;

import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

/**
 * Singleton to handle localization keys and resource bundles
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Localizer
{

    private static Localizer instance;
    private Locale currentLocale;
    private ResourceBundle resourceBundle;
    final private Logger logger = LoggerFactory.getLogger(Localizer.class);

    protected Localizer()
    {
        this.changeLocale(Locale.ENGLISH);
    }

    public static Localizer getInstance()
    {
        if (instance == null)
            instance = new Localizer();
        return instance;
    }

    public Enumeration<String> getAllKeys()
    {
        return this.resourceBundle.getKeys();
    }

    public void changeLocale(Locale locale)
    {
        Locale.setDefault(locale);
        try {
            ResourceBundle.Control utf8Control = new UTF8ResourceBundleControl();
            this.resourceBundle = ResourceBundle.getBundle("localization/JavaSDK", locale, utf8Control);
            this.currentLocale = locale;
            logger.debug("Locale changed to: " + locale);
        } catch (MissingResourceException e) {
            logger.error("Could not find locale resources.");
            logger.error(e.getMessage());
        }
    }

    public Locale getCurrentLocale()
    {
        return currentLocale;
    }

    public ResourceBundle getResourceBundle()
    {
        return resourceBundle;
    }

    public String getText(LocalizedStrings key)
    {
        return getStringForKey(key.toString());
    }

    public String getStringForKey(String key)
    {

        Preconditions.checkNotNull(key, "key should not be null");

        if (resourceBundle.containsKey(key)) {
            return resourceBundle.getString(key);
        } else {
            return key;
        }
    }
}
