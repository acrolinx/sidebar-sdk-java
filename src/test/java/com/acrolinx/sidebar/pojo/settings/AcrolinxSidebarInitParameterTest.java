/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.settings;

import java.util.Collections;
import java.util.Properties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AcrolinxSidebarInitParameterTest {
  private static final String OS_NAME = "os.name";
  private static final String OS_VERSION = "os.version";
  private static final String JAVA_VENDOR = "java.vendor";
  private static final String JAVA_VM_NAME = "java.vm.name";
  private static final String JAVA_RUNTIME_NAME = "java.runtime.name";
  private static final String JAVA_RUNTIME_VERSION = "java.runtime.version";

  @Test
  void toStringTest() {
    final Properties properties = (Properties) System.getProperties().clone();

    try {
      setSystemProperties();

      AcrolinxSidebarInitParameter acrolinxSidebarInitParameter = createAcrolinxInitParameters();

      Assertions.assertEquals(
          "{\"clientSignature\":\"foo\",\"clientComponents\":[{\"id\":\"foo\",\"name\":\"bar\",\"version\":\"buzz\"},{\"id\":\"com.acrolinx.sidebar.java\",\"name\":\"Java SDK\",\"version\":\"2.5.12.123\",\"category\":\"DETAIL\"},{\"id\":\"os.Win\",\"name\":\"Win\",\"version\":\"13.5.1\",\"category\":\"DETAIL\"},{\"id\":\"java.runtime.Zuul\",\"name\":\"OpenJDK Runtime Environment\",\"version\":\"1.8.0_382-c05\",\"category\":\"DETAIL\"}],\"showServerSelector\":false,\"enableSingleSignOn\":false,\"enforceHTTPS\":false,\"logFileLocation\":\"\",\"minimumJavaVersion\":0,\"logger\":{\"name\":\"com.acrolinx.sidebar.pojo.settings.AcrolinxSidebarInitParameter\"}}",
          acrolinxSidebarInitParameter.toString());
    } finally {
      System.setProperties(properties);
    }
  }

  private static AcrolinxSidebarInitParameter createAcrolinxInitParameters() {
    SoftwareComponent softwareComponent = new SoftwareComponent("foo", "bar", "buzz");
    AcrolinxSidebarInitParameter.AcrolinxSidebarInitParameterBuilder
        acrolinxSidebarInitParameterBuilder =
            new AcrolinxSidebarInitParameter.AcrolinxSidebarInitParameterBuilder(
                "foo", Collections.singletonList(softwareComponent));

    return new AcrolinxSidebarInitParameter(acrolinxSidebarInitParameterBuilder);
  }

  private static void setSystemProperties() {
    System.setProperty(OS_NAME, "Win");
    System.setProperty(OS_VERSION, "13.5.1");
    System.setProperty(JAVA_VENDOR, "Zuul");
    System.setProperty(JAVA_VM_NAME, "OpenJDK Runtime Environment");
    System.setProperty(JAVA_RUNTIME_NAME, "OpenJDK Runtime Environment");
    System.setProperty(JAVA_RUNTIME_VERSION, "1.8.0_382-c05");
  }
}
