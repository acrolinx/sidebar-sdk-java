/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.localization;

import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Singleton to handle localization keys and resource bundles */
public final class Localizer {
  private static final Logger logger = LoggerFactory.getLogger(Localizer.class);
  private static final Localizer instance = new Localizer();

  private Locale currentLocale;
  private ResourceBundle resourceBundle;

  private Localizer() {
    this.changeLocale(Locale.ENGLISH);
  }

  public static Localizer getInstance() {
    return instance;
  }

  public Enumeration<String> getAllKeys() {
    return this.resourceBundle.getKeys();
  }

  public void changeLocale(Locale locale) {
    Locale.setDefault(locale);

    try {
      ResourceBundle.Control utf8Control = new UTF8ResourceBundleControl();
      this.resourceBundle = ResourceBundle.getBundle("localization/JavaSDK", locale, utf8Control);
      this.currentLocale = locale;
      logger.debug("Locale changed to: {}", locale);
    } catch (MissingResourceException e) {
      logger.error("Could not find locale resources", e);
    }
  }

  public Locale getCurrentLocale() {
    return currentLocale;
  }

  public ResourceBundle getResourceBundle() {
    return resourceBundle;
  }

  public String getText(LocalizedStrings key) {
    return getStringForKey(key.toString());
  }

  public String getStringForKey(String key) {
    Objects.requireNonNull(key, "key should not be null");

    if (resourceBundle.containsKey(key)) {
      return resourceBundle.getString(key);
    }

    return key;
  }
}
