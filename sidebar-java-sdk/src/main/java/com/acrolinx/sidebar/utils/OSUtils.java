/* Copyright (c) 2018 Acrolinx GmbH */


package com.acrolinx.sidebar.utils;

/**
 * Retrieves the current OS.
 */

@SuppressWarnings("unused")
public class OSUtils
{
    public enum EnumOS
    {
        linux, macos, unknown, windows;

        public boolean isLinux()
        {

            return this == linux;
        }

        public boolean isMac()
        {

            return this == macos;
        }

        public boolean isWindows()
        {

            return this == windows;
        }
    }

    public static EnumOS getOS()
    {
        String s = System.getProperty("os.name").toLowerCase();

        if (s.contains("mac"))
            return EnumOS.macos;
        else if (s.contains("win"))
            return EnumOS.windows;
        else if (s.contains("linux") || s.contains("sunos") || s.contains("solaris") || s.contains("unix"))
            return EnumOS.linux;
        else
            return EnumOS.unknown;
    }

}
