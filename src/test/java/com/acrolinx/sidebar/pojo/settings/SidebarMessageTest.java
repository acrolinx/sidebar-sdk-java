/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.settings;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SidebarMessageTest
{

    @Test
    void testToString()
    {
        SidebarMessage sidebarMessage = new SidebarMessage("foo", "bar", SidebarMessageType.SUCCESS);

        Assertions.assertEquals("{\"type\":\"success\",\"title\":\"foo\",\"text\":\"bar\"}", sidebarMessage.toString());
    }
}