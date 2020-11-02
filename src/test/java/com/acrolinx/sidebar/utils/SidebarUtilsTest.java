/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SidebarUtilsTest
{
    @Test
    public void isValidURLNullTest() throws Exception
    {
        boolean validURL = SidebarUtils.isValidURL(null);
        assertTrue(!validURL);
    }

    @Test
    public void isValidURLLocalhostTest() throws Exception
    {
        boolean validURL = SidebarUtils.isValidURL("http://sifnos");
        assertTrue(validURL);
    }

    @Test
    public void isValidURLTEST() throws Exception
    {
        boolean validURL = SidebarUtils.isValidURL("https://us-demo.acrolinx.cloud:443/dashboard.html");
        assertTrue(validURL);
    }

    @Test
    public void isValidURLTEST2() throws Exception
    {
        boolean validURL = SidebarUtils.isValidURL(
                "https://acrolinxiq.wdf.sap.corp/output/en/czv1533128749082_xml_d020143_810d34842a633047_601823388_report.html");
        assertTrue(validURL);
    }

    @Test
    public void isNOTValidURLTEST() throws Exception
    {
        boolean validURL = SidebarUtils.isValidURL(
                "https:/acrolinxiq.wdf.sap.corp/output/en/czv1533128749082_xml_d020143_810d34842a633047_601823388_report.html");
        assertTrue(!validURL);
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