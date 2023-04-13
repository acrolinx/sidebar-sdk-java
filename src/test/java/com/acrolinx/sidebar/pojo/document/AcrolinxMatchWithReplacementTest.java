/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.pojo.document;

import org.junit.Assert;
import org.junit.Test;

public class AcrolinxMatchWithReplacementTest
{
    @Test
    public void copy()
    {
        AcrolinxMatchWithReplacement acrolinxMatchWithReplacement = new AcrolinxMatchWithReplacement("test",
                new IntRange(1, 4), "lala");
        AbstractMatch match = acrolinxMatchWithReplacement.setRange(new IntRange(5, 8));
        Assert.assertEquals(4, acrolinxMatchWithReplacement.getRange().getMaximumInteger());
        Assert.assertEquals(8, match.getRange().getMaximumInteger());
    }
}