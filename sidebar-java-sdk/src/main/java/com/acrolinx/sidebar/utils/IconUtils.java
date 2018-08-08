/* Copyright (c) 2017-2018 Acrolinx GmbH */


package com.acrolinx.sidebar.utils;

import java.awt.*;
import java.io.InputStream;
import java.net.URL;

@SuppressWarnings({"WeakerAccess", "unused"})

/**
 * Use this class to retrieve the Acrolinx Logo from package resources.
 */
public class IconUtils
{
    public static InputStream getAcrolinxIcon_16_16_AsStream()
    {
        return IconUtils.class.getResourceAsStream("/icons/acrolinxIcon16px.png");
    }

    public static InputStream getAcrolinxIcon_32_32_AsStream()
    {
        return IconUtils.class.getResourceAsStream("/icons/acrolinxIcon16px@2x.png");
    }

    public static InputStream getAcrolinxIcon_24_24_AsStream()
    {
        return IconUtils.class.getResourceAsStream("/icons/acrolinxIcon24px.png");
    }

    public static InputStream getAcrolinxIcon_48_48_AsStream()
    {
        return IconUtils.class.getResourceAsStream("/icons/acrolinxIcon24px@2px.png");
    }

    public static URL getAcrolinxIcon_16_16_URL()
    {
        return IconUtils.class.getResource("/icons/acrolinxIcon16px.png");
    }

    public static URL getAcrolinxIcon_32_32_URL()
    {
        return IconUtils.class.getResource("/icons/acrolinxIcon16px@2x.png");
    }

    public static URL getAcrolinxIcon_24_24_URL()
    {
        return IconUtils.class.getResource("/icons/acrolinxIcon24px.png");
    }

    public static URL getAcrolinxIcon_48_48_URL()
    {
        return IconUtils.class.getResource("/icons/acrolinxIcon24px@2px.png");
    }

    /**
     * This method loads a scaled instance of the image for retina displays.
     */
    public static Image getAcrolinxIcon_16_16()
    {
        return Toolkit.getDefaultToolkit().getImage(getAcrolinxIcon_16_16_URL());
    }

    /**
     * This method loads a scaled instance of the image for retina displays.
     */
    public static Image getAcrolinxIcon_24_24()
    {
        return Toolkit.getDefaultToolkit().getImage(getAcrolinxIcon_24_24_URL());
    }
}