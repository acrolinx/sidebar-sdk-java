/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.utils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SidebarUtilsTest
{
    @Test
    public void isValidURLNullTest()
    {
        assertFalse(SidebarUtils.isValidURL(null));
    }

    @Test
    public void isValidURLLocalhostTest()
    {
        assertTrue(SidebarUtils.isValidURL("http://sifnos"));
    }

    @Test
    public void isValidURLTEST()
    {
        assertTrue(SidebarUtils.isValidURL("https://us-demo.acrolinx.cloud:443/dashboard.html"));
    }

    @Test
    public void isValidURLTEST2()
    {
        assertTrue(SidebarUtils.isValidURL(
                "https://acrolinxiq.wdf.sap.corp/output/en/czv1533128749082_xml_d020143_810d34842a633047_601823388_report.html"));
    }

    @Test
    public void isNOTValidURLTEST()
    {
        assertFalse(SidebarUtils.isValidURL(
                "https:/acrolinxiq.wdf.sap.corp/output/en/czv1533128749082_xml_d020143_810d34842a633047_601823388_report.html"));
    }

    @Test
    public void testSidebarSystemUtils()
    {
        assertNotNull(SidebarUtils.getSystemJavaVMName());
        assertTrue(SidebarUtils.getSystemJavaVersion() >= 8);
        assertNotNull(SidebarUtils.getPathOfCurrentJavaJRE());
        assertNotNull(SidebarUtils.getFullCurrentJavaVersionString());
    }
}
