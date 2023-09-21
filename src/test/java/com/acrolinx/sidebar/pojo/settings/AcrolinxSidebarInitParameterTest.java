/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.settings;

import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AcrolinxSidebarInitParameterTest
{
    public static final String OS_NAME = "os.name";
    public static final String OS_VERSION = "os.version";
    public static final String JAVA_VENDOR = "java.vendor";
    public static final String JAVA_VM_NAME = "java.vm.name";
    public static final String JAVA_RUNTIME_NAME = "java.runtime.name";
    public static final String JAVA_RUNTIME_VERSION = "java.runtime.version";

    @Test
    void toStringTest()
    {
        SystemProperties systemProperties = createSystemProperties();

        try {
            setSystemProperties();

            AcrolinxSidebarInitParameter acrolinxSidebarInitParameter = createAcrolinxInitParameters();

            Assertions.assertEquals(
                    "{\"clientSignature\":\"foo\",\"clientComponents\":[{\"id\":\"foo\",\"name\":\"bar\",\"version\":\"buzz\"},{\"id\":\"com.acrolinx.sidebar.java\",\"name\":\"Java SDK\",\"category\":\"DETAIL\"},{\"id\":\"os.Win\",\"name\":\"Win\",\"version\":\"13.5.1\",\"category\":\"DETAIL\"},{\"id\":\"java.runtime.Zuul\",\"name\":\"OpenJDK Runtime Environment\",\"version\":\"1.8.0_382-c05\",\"category\":\"DETAIL\"}],\"showServerSelector\":false,\"enableSingleSignOn\":false,\"enforceHTTPS\":false,\"logFileLocation\":\"\",\"minimumJavaVersion\":0,\"logger\":{\"name\":\"com.acrolinx.sidebar.pojo.settings.AcrolinxSidebarInitParameter\"}}",
                    acrolinxSidebarInitParameter.toString());
        } finally {
            restoreSystemPropertiesProperties(systemProperties);
        }
    }

    private SystemProperties createSystemProperties()
    {
        String osName = System.getProperty(OS_NAME);
        String osVersion = System.getProperty(OS_VERSION);
        String javaVendor = System.getProperty(JAVA_VENDOR);
        String javaVmName = System.getProperty(JAVA_VM_NAME);
        String javaRuntimeName = System.getProperty(JAVA_RUNTIME_NAME);
        String javaRuntimeVersion = System.getProperty(JAVA_RUNTIME_VERSION);

        SystemProperties systemProperties = new SystemProperties();
        systemProperties.setOsName(osName);
        systemProperties.setOsVersion(osVersion);
        systemProperties.setJavaVendor(javaVendor);
        systemProperties.setJavaVmName(javaVmName);
        systemProperties.setJavaRuntimeName(javaRuntimeName);
        systemProperties.setJavaRuntimeVersion(javaRuntimeVersion);

        return systemProperties;
    }

    private static AcrolinxSidebarInitParameter createAcrolinxInitParameters()
    {
        SoftwareComponent softwareComponent = new SoftwareComponent("foo", "bar", "buzz");
        AcrolinxSidebarInitParameter.AcrolinxSidebarInitParameterBuilder acrolinxSidebarInitParameterBuilder = new AcrolinxSidebarInitParameter.AcrolinxSidebarInitParameterBuilder(
                "foo", Collections.singletonList(softwareComponent));

        return new AcrolinxSidebarInitParameter(acrolinxSidebarInitParameterBuilder);
    }

    private static void restoreSystemPropertiesProperties(SystemProperties systemProperties)
    {
        System.setProperty(OS_NAME, systemProperties.getOsName());
        System.setProperty(OS_VERSION, systemProperties.getOsVersion());
        System.setProperty(JAVA_VENDOR, systemProperties.getJavaVendor());
        System.setProperty(JAVA_VM_NAME, systemProperties.getJavaVmName());
        System.setProperty(JAVA_RUNTIME_NAME, systemProperties.getJavaRuntimeName());
        System.setProperty(JAVA_RUNTIME_VERSION, systemProperties.getJavaRuntimeVersion());
    }

    private static void setSystemProperties()
    {
        System.setProperty(OS_NAME, "Win");
        System.setProperty(OS_VERSION, "13.5.1");
        System.setProperty(JAVA_VENDOR, "Zuul");
        System.setProperty(JAVA_VM_NAME, "OpenJDK Runtime Environment");
        System.setProperty(JAVA_RUNTIME_NAME, "OpenJDK Runtime Environment");
        System.setProperty(JAVA_RUNTIME_VERSION, "1.8.0_382-c05");
    }
}