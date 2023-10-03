/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.localization;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/*
 * https://github.com/joconner/enhanced-resources
 */
class UTF8ResourceBundleControl extends ResourceBundle.Control {
  UTF8ResourceBundleControl() {
    // nothing to do here
  }

  @Override
  public ResourceBundle newBundle(
      final String baseName,
      final Locale locale,
      final String format,
      final ClassLoader classLoader,
      final boolean reload)
      throws IllegalAccessException, InstantiationException, IOException {
    final String bundleName = toBundleName(baseName, locale);
    ResourceBundle resourceBundle = null;

    if (format.equals("java.class")) {
      resourceBundle = super.newBundle(baseName, locale, format, classLoader, reload);
    } else if (format.equals("java.properties")) {
      final String resourceName =
          bundleName.contains("://") ? null : toResourceName(bundleName, "properties");

      if (resourceName == null) {
        return resourceBundle;
      }

      InputStream inputStream;

      if (reload) {
        inputStream = reload(resourceName, classLoader);
      } else {
        inputStream = classLoader.getResourceAsStream(resourceName);
      }

      if (inputStream != null) {
        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
          resourceBundle = new PropertyResourceBundle(reader);
        }
      }
    } else {
      throw new IllegalArgumentException("Unknown format: " + format);
    }

    return resourceBundle;
  }

  private static InputStream reload(final String resourceName, final ClassLoader classLoader)
      throws IOException {
    final URL url = classLoader.getResource(resourceName);

    if (url != null) {
      final URLConnection urlConnection = url.openConnection();

      if (urlConnection != null) {
        // Disable caches to get fresh data for reloading.
        urlConnection.setUseCaches(false);
        return urlConnection.getInputStream();
      }
    }

    return null;
  }
}
