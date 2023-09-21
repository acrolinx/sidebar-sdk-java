/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.settings;

public class SystemProperties
{
    String osName;
    String osVersion;
    String javaVendor;
    String javaVmName;
    String javaRuntimeName;
    String javaRuntimeVersion;

    public String getOsName()
    {
        return osName;
    }

    public void setOsName(String osName)
    {
        this.osName = osName;
    }

    public String getOsVersion()
    {
        return osVersion;
    }

    public void setOsVersion(String osVersion)
    {
        this.osVersion = osVersion;
    }

    public String getJavaVendor()
    {
        return javaVendor;
    }

    public void setJavaVendor(String javaVendor)
    {
        this.javaVendor = javaVendor;
    }

    public String getJavaVmName()
    {
        return javaVmName;
    }

    public void setJavaVmName(String javaVmName)
    {
        this.javaVmName = javaVmName;
    }

    public String getJavaRuntimeName()
    {
        return javaRuntimeName;
    }

    public void setJavaRuntimeName(String javaRuntimeName)
    {
        this.javaRuntimeName = javaRuntimeName;
    }

    public String getJavaRuntimeVersion()
    {
        return javaRuntimeVersion;
    }

    public void setJavaRuntimeVersion(String javaRuntimeVersion)
    {
        this.javaRuntimeVersion = javaRuntimeVersion;
    }
}
