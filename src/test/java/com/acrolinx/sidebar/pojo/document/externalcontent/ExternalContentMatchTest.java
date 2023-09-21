/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.document.externalcontent;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExternalContentMatchTest
{

    @Test
    void testToString()
    {
        ExternalContentMatch externalContentMatch = new ExternalContentMatch("foo", "bar", 0, 1,
                Collections.emptyList());

        Assertions.assertEquals(
                "{\"id\":\"foo\",\"type\":\"bar\",\"originalBegin\":0,\"originalEnd\":1,\"externalContentMatches\":[]}",
                externalContentMatch.toString());
    }
}