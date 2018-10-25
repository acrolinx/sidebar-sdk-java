/* Copyright (c) 2016-present Acrolinx GmbH */

package com.acrolinx.sidebar.pojo.document;

import com.google.gson.Gson;

@SuppressWarnings("unused")
public class CheckedDocumentPart
{
    private final String checkId;
    private final IntRange range;

    public CheckedDocumentPart(String checkId, IntRange range)
    {
        this.checkId = checkId;
        this.range = range;
    }

    public String getCheckId()
    {
        return checkId;
    }

    public IntRange getRange()
    {
        return range;
    }

    @Override
    public String toString()
    {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getAsJS()
    {
        return "{checkId: \"" + checkId + "\", range:[" + range.getMinimumInteger() + "," + range.getMaximumInteger()
                + "]}";
    }
}
