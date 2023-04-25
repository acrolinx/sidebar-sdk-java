/* Copyright (c) 2018-present Acrolinx GmbH */

package com.acrolinx.sidebar.utils;

/**
 * Retrieves the current OS.
 */

public final class OSUtils
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
        String string = System.getProperty("os.name").toLowerCase();

        if (string.contains("mac")) {
            return EnumOS.MACOS;
        } else if (string.contains("win")) {
            return EnumOS.WINDOWS;
        } else if (string.contains("linux") || string.contains("sunos") || string.contains("solaris")
                || string.contains("unix")) {
            return EnumOS.LINUX;
        } else {
            return EnumOS.UNKNOWN;
        }
    }

    private OSUtils()
    {
        throw new IllegalStateException();
    }
}
