/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.settings;

import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AcrolinxSidebarInitParameterTest
{
    @Test
    void toStringTest()
    {
        String osName = System.getProperty("os.name", "Win");
        String osVersion = System.getProperty("os.version", "13.5.1");
        String javaVendor = System.getProperty("java.vendor", "Zuul");
        String javaVmName = System.getProperty("java.vm.name", "OpenJDK Runtime Environment");
        String javaRuntimeName = System.getProperty("java.runtime.name", "OpenJDK Runtime Environment");
        String javaRuntimeVersion = System.getProperty("java.runtime.version", "1.8.0_382-c05");

        try {
            setTestProperties();

            AcrolinxSidebarInitParameter acrolinxSidebarInitParameter = createAcrolinxInitParameters();

            Assertions.assertEquals(
                    "{\"clientSignature\":\"foo\",\"clientComponents\":[{\"id\":\"foo\",\"name\":\"bar\",\"version\":\"buzz\"},{\"id\":\"com.acrolinx.sidebar.java\",\"name\":\"Java SDK\",\"category\":\"DETAIL\"},{\"id\":\"os.Win\",\"name\":\"Win\",\"version\":\"13.5.1\",\"category\":\"DETAIL\"},{\"id\":\"java.runtime.Zuul\",\"name\":\"OpenJDK Runtime Environment\",\"version\":\"1.8.0_382-c05\",\"category\":\"DETAIL\"}],\"showServerSelector\":false,\"enableSingleSignOn\":false,\"enforceHTTPS\":false,\"logFileLocation\":\"\",\"minimumJavaVersion\":0,\"logger\":{\"name\":\"com.acrolinx.sidebar.pojo.settings.AcrolinxSidebarInitParameter\"}}",
                    acrolinxSidebarInitParameter.toString());
        } finally {
            restoreProperties(osName, osVersion, javaVendor, javaVmName, javaRuntimeName, javaRuntimeVersion);
        }
    }

    private static AcrolinxSidebarInitParameter createAcrolinxInitParameters()
    {
        SoftwareComponent softwareComponent = new SoftwareComponent("foo", "bar", "buzz");
        AcrolinxSidebarInitParameter.AcrolinxSidebarInitParameterBuilder acrolinxSidebarInitParameterBuilder = new AcrolinxSidebarInitParameter.AcrolinxSidebarInitParameterBuilder(
                "foo", Collections.singletonList(softwareComponent));

        return new AcrolinxSidebarInitParameter(acrolinxSidebarInitParameterBuilder);
    }

    private static void restoreProperties(String osName, String osVersion, String javaVendor, String javaVmName,
            String javaRuntimeName, String javaRuntimeVersion)
    {
        System.setProperty("os.name", osName);
        System.setProperty("os.version", osVersion);
        System.setProperty("java.vendor", javaVendor);
        System.setProperty("java.vm.name", javaVmName);
        System.setProperty("java.runtime.name", javaRuntimeName);
        System.setProperty("java.runtime.version", javaRuntimeVersion);
    }

    private static void setTestProperties()
    {
        System.setProperty("os.name", "Win");
        System.setProperty("os.version", "13.5.1");
        System.setProperty("java.vendor", "Zuul");
        System.setProperty("java.vm.name", "OpenJDK Runtime Environment");
        System.setProperty("java.runtime.name", "OpenJDK Runtime Environment");
        System.setProperty("java.runtime.version", "1.8.0_382-c05");
    }
}