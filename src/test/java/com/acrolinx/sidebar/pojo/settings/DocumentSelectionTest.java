/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.pojo.settings;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.acrolinx.sidebar.pojo.document.IntRange;

public class DocumentSelectionTest
{
    @Test
    public void convertDocumentSelection()
    {
        List<IntRange> intRanges = new ArrayList<>();
        intRanges.add(new IntRange(12, 12));
        intRanges.add(new IntRange(13, 14));
        intRanges.add(new IntRange(15, 16));
        DocumentSelection selection = new DocumentSelection(intRanges);
        Assert.assertEquals("{\"ranges\":[[12,12],[13,14],[15,16]]}", selection.toString());
    }

    @Test
    public void testConvertToStringOneValueOnly()
    {
        List<IntRange> intRanges = new ArrayList<>();
        intRanges.add(new IntRange(12, 12));
        DocumentSelection selection = new DocumentSelection(intRanges);
        Assert.assertEquals("{\"ranges\":[[12,12]]}", selection.toString());
    }
}