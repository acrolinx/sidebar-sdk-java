/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.document;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CheckContentTest
{

    @Test
    void toStringTest()
    {
        CheckContent checkContent = new CheckContent("foo", null);

        Assertions.assertEquals(
                "{\"content\":\"foo\",\"externalContent\":{\"textReplacements\":[],\"entities\":[],\"ditaReferences\":[],\"xincludeReferences\":[]}}",
                checkContent.toString());
    }
}