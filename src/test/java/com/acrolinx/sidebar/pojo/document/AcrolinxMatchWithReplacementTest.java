/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.document;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AcrolinxMatchWithReplacementTest
{
    @Test
    void copy()
    {
        AcrolinxMatchWithReplacement acrolinxMatchWithReplacement = new AcrolinxMatchWithReplacement("test",
                new IntRange(1, 4), "lala");
        AbstractMatch match = acrolinxMatchWithReplacement.setRange(new IntRange(5, 8));
        Assertions.assertEquals(4, acrolinxMatchWithReplacement.getRange().getMaximumInteger());
        Assertions.assertEquals(8, match.getRange().getMaximumInteger());
    }
}
