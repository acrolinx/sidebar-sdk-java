
package com.acrolinx.sidebar.localization;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;

public class LocalizerTest
{
    @Test
    public void getText() throws Exception
    {
        Localizer.getInstance().changeLocale(Locale.ENGLISH);
        String text = Localizer.getInstance().getText(LocalizedStrings.SHOW_SIDEBAR_LABEL);
        Assert.assertTrue(text.equalsIgnoreCase("Show Sidebar"));
    }

    @Test
    public void changeLocale() throws Exception
    {
        Localizer.getInstance().changeLocale(Locale.GERMAN);
        String text = Localizer.getInstance().getText(LocalizedStrings.SHOW_SIDEBAR_LABEL);
        Assert.assertTrue(text.equalsIgnoreCase("Sidebar anzeigen"));
    }

    @Test
    public void changeLocaleToUnknown() throws Exception
    {
        Localizer.getInstance().changeLocale(Locale.CHINESE);
        String text = Localizer.getInstance().getText(LocalizedStrings.SHOW_SIDEBAR_LABEL);
        System.out.println(text);
        Assert.assertTrue(text.equalsIgnoreCase("Show Sidebar"));
    }

    @Test
    public void testEncoding()
    {
        Localizer.getInstance().changeLocale(Locale.GERMAN);
        String text = Localizer.getInstance().getText(LocalizedStrings.SHOW_SIDEBAR_TOOLTIP);
        Assert.assertTrue(text.equalsIgnoreCase("Sidebar anzeigen"));
    }
}