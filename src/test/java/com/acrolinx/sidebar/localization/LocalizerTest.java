/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.localization;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

public class LocalizerTest
{
    @Test
    public void getText()
    {
        Localizer.getInstance().changeLocale(Locale.ENGLISH);
        String text = Localizer.getInstance().getText(LocalizedStrings.SHOW_SIDEBAR_LABEL);
        Assert.assertEquals("Show Sidebar", text);
    }

    @Test
    public void changeLocale()
    {
        Localizer.getInstance().changeLocale(Locale.GERMAN);
        String text = Localizer.getInstance().getText(LocalizedStrings.SHOW_SIDEBAR_LABEL);
        Assert.assertEquals("Sidebar anzeigen", text);
    }

    @Test
    public void changeLocaleToUnknown()
    {
        Localizer.getInstance().changeLocale(Locale.CHINESE);
        String text = Localizer.getInstance().getText(LocalizedStrings.SHOW_SIDEBAR_LABEL);
        Assert.assertEquals("Show Sidebar", text);
    }

    @Test
    public void testEncoding()
    {
        Localizer.getInstance().changeLocale(Locale.GERMAN);
        String text = Localizer.getInstance().getText(LocalizedStrings.SHOW_SIDEBAR_TOOLTIP);
        Assert.assertEquals("Sidebar anzeigen", text);
    }
}
