/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.settings;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SoftwareComponentTest
{

    @Test
    void testToString()
    {
        SoftwareComponent softwareComponent = new SoftwareComponent("foo", "bar", "buzz");

        Assertions.assertEquals("{\"id\":\"foo\",\"name\":\"bar\",\"version\":\"buzz\"}", softwareComponent.toString());
    }
}