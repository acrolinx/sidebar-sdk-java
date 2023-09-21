/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.settings;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SidebarConfigurationTest
{
    @Test
    void toStringTest()
    {
        SidebarConfiguration sidebarConfiguration = new SidebarConfiguration(false);

        Assertions.assertEquals("{\"readOnlySuggestions\":false}", sidebarConfiguration.toString());
    }
}