/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.document.externalContent;

import java.util.ArrayList;
import java.util.List;

import com.acrolinx.sidebar.pojo.document.IntRange;
import com.google.gson.Gson;

public class ExternalContentMatch
{
    private final String id;
    private final String type;
    private final int originalBegin;
    private final int originalEnd;
    private List<ExternalContentMatch> externalContentMatches;

    public ExternalContentMatch(String id, String type, int originalBegin, int originalEnd,
            List<ExternalContentMatch> externalContentMatches)
    {
        this(id, type, originalBegin, originalEnd);
        if (externalContentMatches != null) {
            this.externalContentMatches = externalContentMatches;
        } else {
            this.externalContentMatches = new ArrayList<>();
        }
    }

    public ExternalContentMatch(String id, String type, int originalBegin, int originalEnd)
    {
        this.id = id;
        this.type = type;
        this.originalBegin = originalBegin;
        this.originalEnd = originalEnd;
        this.externalContentMatches = new ArrayList<>();
    }

    public String getId()
    {
        return id;
    }

    public String getType()
    {
        return type;
    }

    public IntRange getRange()
    {
        return new IntRange(this.originalBegin, this.originalEnd);
    }

    public ExternalContentMatch setRange(final IntRange range)
    {
        return new ExternalContentMatch(this.id, this.type, range.getMinimumInteger(), range.getMaximumInteger(),
                this.getExternalContentMatches());
    }

    public List<ExternalContentMatch> getExternalContentMatches()
    {
        if (externalContentMatches == null) {
            externalContentMatches = new ArrayList<>();
        }
        return externalContentMatches;
    }

    public void setExternalContentMatches(List<ExternalContentMatch> newList)
    {
        if (newList == null) {
            this.externalContentMatches = new ArrayList<>();
        } else {
            this.externalContentMatches = newList;
        }
    }

    @Override
    public String toString()
    {
        return new Gson().toJson(this);
    }
}
