
package com.acrolinx.sidebar.utils;

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
        return IconUtils.class.getResourceAsStream("/icons/acrolinx_16_16.png");
    }

    public static InputStream getAcrolinxIcon_32_32_AsStream()
    {
        return IconUtils.class.getResourceAsStream("/icons/acrolinx_32_32.png");
    }

    public static URL getAcrolinxIcon_16_16_URL()
    {
        return IconUtils.class.getResource("/icons/acrolinx_16_16.png");
    }

    public static URL getAcrolinxIcon_32_32_URL()
    {
        return IconUtils.class.getResource("/icons/acrolinx_32_32.png");
    }

}