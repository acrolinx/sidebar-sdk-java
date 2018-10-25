/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.pojo.document;

import org.junit.Assert;
import org.junit.Test;

public class AcrolinxMatchWithReplacementTest
{
    @Test
    public void copy() throws Exception
    {
        AcrolinxMatchWithReplacement acrolinxMatchWithReplacement = new AcrolinxMatchWithReplacement("test",
                new IntRange(1, 4), "lala");
        AbstractMatch match = acrolinxMatchWithReplacement.setRange(new IntRange(5, 8));
        Assert.assertTrue(acrolinxMatchWithReplacement.getRange().getMaximumInteger() == 4);
        Assert.assertTrue(match.getRange().getMaximumInteger() == 8);
    }
}