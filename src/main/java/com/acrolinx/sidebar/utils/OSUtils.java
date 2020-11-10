/* Copyright (c) 2018-present Acrolinx GmbH */

package com.acrolinx.sidebar.utils;

/**
 * Retrieves the current OS.
 */

@SuppressWarnings("unused")
public class OSUtils
{
    public enum EnumOS
    {
        LINUX, MACOS, UNKNOWN, WINDOWS;

        public boolean isLinux()
        {

            return this == LINUX;
        }

        public boolean isMac()
        {

            return this == MACOS;
        }

        public boolean isWindows()
        {

            return this == WINDOWS;
        }
    }

    public static EnumOS getOS()
    {
        String s = System.getProperty("os.name").toLowerCase();

        if (s.contains("mac"))
            return EnumOS.MACOS;
        else if (s.contains("win"))
            return EnumOS.WINDOWS;
        else if (s.contains("linux") || s.contains("sunos") || s.contains("solaris") || s.contains("unix"))
            return EnumOS.LINUX;
        else
            return EnumOS.UNKNOWN;
    }

}
