/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.settings;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SoftwareComponentTest
{
    @Test
    void toStringTest()
    {
        SoftwareComponent softwareComponent = new SoftwareComponent("foo", "bar", "buzz");

        Assertions.assertEquals("{\"id\":\"foo\",\"name\":\"bar\",\"version\":\"buzz\"}", softwareComponent.toString());
    }
}