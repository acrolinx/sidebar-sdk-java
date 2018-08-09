/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.utils;

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

}