/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

/** Retrieves the current OS. */
public final class OSUtils {
  public enum EnumOS {
    LINUX,

    MACOS,

    UNKNOWN,

    WINDOWS;

    public boolean isLinux() {
      return this == LINUX;
    }

    public boolean isMac() {
      return this == MACOS;
    }

    public boolean isWindows() {
      return this == WINDOWS;
    }
  }

  public static EnumOS getOS() {
    String osNameString = System.getProperty("os.name").toLowerCase();

    if (osNameString.contains("mac")) {
      return EnumOS.MACOS;
    } else if (osNameString.contains("win")) {
      return EnumOS.WINDOWS;
    } else if (osNameString.contains("linux")
        || osNameString.contains("sunos")
        || osNameString.contains("solaris")
        || osNameString.contains("unix")) {
      return EnumOS.LINUX;
    } else {
      return EnumOS.UNKNOWN;
    }
  }

  private OSUtils() {
    throw new IllegalStateException();
  }
}
