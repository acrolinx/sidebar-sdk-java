/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.acrolinx.sidebar.pojo.settings.SoftwareComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SidebarUtilsTest {
  @Test
  void getCurrentSdkImplementationVersionTest() {
    String currentSdkImplementationVersion = SidebarUtils.getCurrentSdkImplementationVersion();
    assertEquals("2.5.12", currentSdkImplementationVersion);
  }

  @Test
  void getJavaSdkSoftwareComponentTest() {
    final SoftwareComponent softwareComponent = SidebarUtils.getJavaSdkSoftwareComponent();
    Assertions.assertEquals(
        "{\"id\":\"com.acrolinx.sidebar.java\",\"name\":\"Java SDK\",\"version\":\"2.5.12\",\"category\":\"DETAIL\"}",
        softwareComponent.toString());
  }

  @Test
  void isNotValidUrlTest() {
    assertFalse(SidebarUtils.isValidUrl("https:/sidebar.acrolinx.com/index.html"));
  }

  @Test
  void isValidUrlLocalhostTest() {
    assertTrue(SidebarUtils.isValidUrl("http://sifnos"));
  }

  @Test
  void isValidUrlNullTest() {
    assertFalse(SidebarUtils.isValidUrl(null));
  }

  @Test
  void isValidUrlTest() {
    assertTrue(SidebarUtils.isValidUrl("https://us-demo.acrolinx.cloud:443/dashboard.html"));
  }

  @Test
  void isValidUrlTest2() {
    assertTrue(SidebarUtils.isValidUrl("https://sidebar.acrolinx.com/index.html"));
  }

  @Test
  void testSidebarSystemUtils() {
    assertNotNull(SidebarUtils.getSystemJavaVmName());
    assertTrue(SidebarUtils.getSystemJavaVersion() >= 8);
    assertNotNull(SidebarUtils.getPathOfCurrentJavaJre());
    assertNotNull(SidebarUtils.getFullCurrentJavaVersionString());
  }
}
