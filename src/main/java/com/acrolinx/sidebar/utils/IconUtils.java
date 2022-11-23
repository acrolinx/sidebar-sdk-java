/* Copyright (c) 2017-present Acrolinx GmbH */

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
        return IconUtils.class.getResourceAsStream("/icons/acrolinxIcon24px@2x.png");
    }
    public static InputStream getAcrolinxLiveIcon_16_16_AsStream()
    {
        return IconUtils.class.getResourceAsStream("/icons/acrolinxLiveIcon16px.png");
    }

    public static InputStream getAcrolinxLiveIcon_24_24_AsStream()
    {
        return IconUtils.class.getResourceAsStream("/icons/acrolinxLiveIcon24px.png");
    }
    public static InputStream getAcrolinxLiveIcon_32_32_AsStream()
    {
        return IconUtils.class.getResourceAsStream("/icons/acrolinxLiveIcon16px@2x.png");
    }

    public static InputStream getAcrolinxLiveIcon_48_48_AsStream()
    {
        return IconUtils.class.getResourceAsStream("/icons/acrolinxLiveIcon24px@2x.png");
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

    public static URL getAcrolinxLiveIcon_16_16_URL()
    {
        return IconUtils.class.getResource("/icons/acrolinxLiveIcon16px.png");
    }

    public static URL getAcrolinxLiveIcon_24_24_URL()
    {
        return IconUtils.class.getResource("/icons/acrolinxLiveIcon24px.png");
    }

    public static URL getAcrolinxLiveIcon_32_32_URL()
    {
        return IconUtils.class.getResource("/icons/acrolinxLiveIcon16px@2x.png");
    }
    public static URL getAcrolinxLiveIcon_48_48_URL()
    {
        return IconUtils.class.getResource("/icons/acrolinxLiveIcon24px@2x.png");
    }


    public static URL getAcrolinxIcon_48_48_URL()
    {
        return IconUtils.class.getResource("/icons/acrolinxIcon24px@2x.png");
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

    /**
     * This method loads a scaled instance of the live image for retina displays.
     */
    public static Image getAcrolinxLiveIcon_16_16()
    {
        return Toolkit.getDefaultToolkit().getImage(getAcrolinxLiveIcon_16_16_URL());
    }

    /**
     * This method loads a scaled instance of the live image for retina displays.
     */
    public static Image getAcrolinxLiveIcon_24_24()
    {
        return Toolkit.getDefaultToolkit().getImage(getAcrolinxLiveIcon_24_24_URL());
    }

    /**
     * This method loads a scaled instance of the live image for retina displays.
     */
    public static Image getAcrolinxLiveIcon_32_32()
    {
        return Toolkit.getDefaultToolkit().getImage(getAcrolinxIcon_32_32_URL());
    }

    /**
     * This method loads a scaled instance of the live image for retina displays.
     */
    public static Image getAcrolinxLiveIcon_48_48()
    {
        return Toolkit.getDefaultToolkit().getImage(getAcrolinxLiveIcon_48_48_URL());
    }

}