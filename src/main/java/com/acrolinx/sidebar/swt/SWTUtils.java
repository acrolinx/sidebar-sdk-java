/**
 *  Copyright (c) 2020-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.swt;

import org.eclipse.swt.browser.Browser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class SWTUtils {

    private static Logger logger =  LoggerFactory.getLogger(AcrolinxSidebarSWT.class);

    protected static void loadScriptJS(Browser browser, String script)
    {
        try {
            final ClassLoader classLoader = SWTUtils.class.getClassLoader();
            final InputStream inputStream = classLoader.getResourceAsStream(script);
            final BufferedReader reader;
            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line;
                final StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                final String scriptLoaded = sb.toString();
                reader.close();
                browser.evaluate(scriptLoaded);
            }

        } catch (final Exception e) {
            logger.error(e.getMessage());
        }
    }
}
