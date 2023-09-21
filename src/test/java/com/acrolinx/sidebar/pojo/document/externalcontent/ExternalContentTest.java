/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.document.externalcontent;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExternalContentTest
{

    @Test
    void toStringTest()
    {
        ExternalContent externalContent = new ExternalContent(Collections.emptyList(), Collections.emptyList(),
                Collections.emptyList(), Collections.emptyList());

        Assertions.assertEquals(
                "{\"textReplacements\":[],\"entities\":[],\"ditaReferences\":[],\"xincludeReferences\":[]}",
                externalContent.toString());
    }
}