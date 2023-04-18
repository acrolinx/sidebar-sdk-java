/* Copyright (c) 2017-present Acrolinx GmbH */

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

class UTF8ResourceBundleControl extends ResourceBundle.Control
{
    UTF8ResourceBundleControl()
    {
        // nothing to do here
    }

    @Override
    public ResourceBundle newBundle(final String baseName, final Locale locale, final String format,
            final ClassLoader loader, final boolean reload)
            throws IllegalAccessException, InstantiationException, IOException
    {
        final String bundleName = toBundleName(baseName, locale);
        ResourceBundle bundle = null;
        if (format.equals("java.class")) {
            bundle = super.newBundle(baseName, locale, format, loader, reload);
        } else if (format.equals("java.properties")) {
            final String resourceName = bundleName.contains("://") ? null : toResourceName(bundleName, "properties");
            if (resourceName == null) {
                return bundle;
            }
            InputStream stream;
            if (reload) {
                stream = reload(resourceName, loader);
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }
            if (stream != null) {
                try (Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                    bundle = new PropertyResourceBundle(reader);
                }
            }
        } else {
            throw new IllegalArgumentException("Unknown format: " + format);
        }
        return bundle;
    }

    private static InputStream reload(final String resourceName, final ClassLoader classLoader) throws IOException
    {
        InputStream stream = null;
        final URL url = classLoader.getResource(resourceName);
        if (url != null) {
            final URLConnection connection = url.openConnection();
            if (connection != null) {
                // Disable caches to get fresh data for
                // reloading.
                connection.setUseCaches(false);
                stream = connection.getInputStream();
            }
        }
        return stream;
    }
}
