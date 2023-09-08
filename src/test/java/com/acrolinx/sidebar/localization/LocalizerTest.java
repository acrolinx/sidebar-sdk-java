/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.localization;

import java.util.Locale;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LocalizerTest
{
    @Test
    void getText()
    {
        Localizer.getInstance().changeLocale(Locale.ENGLISH);
        String text = Localizer.getInstance().getText(LocalizedStrings.SHOW_SIDEBAR_LABEL);
        Assertions.assertEquals("Show Sidebar", text);
    }

    @Test
    void changeLocale()
    {
        Localizer.getInstance().changeLocale(Locale.GERMAN);
        String text = Localizer.getInstance().getText(LocalizedStrings.SHOW_SIDEBAR_LABEL);
        Assertions.assertEquals("Sidebar anzeigen", text);
    }

    @Test
    void changeLocaleToUnknown()
    {
        Localizer.getInstance().changeLocale(Locale.CHINESE);
        String text = Localizer.getInstance().getText(LocalizedStrings.SHOW_SIDEBAR_LABEL);
        Assertions.assertEquals("Show Sidebar", text);
    }

    @Test
    void testEncoding()
    {
        Localizer.getInstance().changeLocale(Locale.GERMAN);
        String text = Localizer.getInstance().getText(LocalizedStrings.SHOW_SIDEBAR_TOOLTIP);
        Assertions.assertEquals("Sidebar anzeigen", text);
    }
}
