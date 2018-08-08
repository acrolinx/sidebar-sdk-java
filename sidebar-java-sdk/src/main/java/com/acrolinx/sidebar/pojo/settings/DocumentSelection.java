/* Copyright (c) 2017-present Acrolinx GmbH */


package com.acrolinx.sidebar.pojo.settings;

import java.util.List;

import com.acrolinx.sidebar.pojo.document.IntRange;
import com.google.gson.Gson;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class DocumentSelection
{
    private final int[][] ranges;

    public DocumentSelection(List<IntRange> ranges)
    {
        this.ranges = convertDocumentSelection(ranges);
    }

    @SuppressWarnings("WeakerAccess")
    public int[][] convertDocumentSelection(List<IntRange> ranges)
    {
        if (ranges != null) {
            int size = ranges.size();
            int[][] rangesArray = new int[size][2];
            int index = 0;
            for (IntRange i : ranges) {
                rangesArray[index][0] = ranges.get(index).getMinimumInteger();
                rangesArray[index][1] = ranges.get(index).getMaximumInteger();
                index++;
            }
            return rangesArray;
        }
        return null;
    }

    @Override
    public String toString()
    {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
