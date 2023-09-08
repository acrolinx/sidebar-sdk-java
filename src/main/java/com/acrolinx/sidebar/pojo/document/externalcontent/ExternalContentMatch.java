/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.document.externalcontent;

import com.acrolinx.sidebar.pojo.document.IntRange;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

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

    public ExternalContentMatch setRange(final IntRange intRange)
    {
        return new ExternalContentMatch(this.id, this.type, intRange.getMinimumInteger(), intRange.getMaximumInteger(),
                this.getExternalContentMatches());
    }

    public List<ExternalContentMatch> getExternalContentMatches()
    {
        if (externalContentMatches == null) {
            externalContentMatches = new ArrayList<>();
        }

        return externalContentMatches;
    }

    public void setExternalContentMatches(List<ExternalContentMatch> externalContentMatches)
    {
        if (externalContentMatches == null) {
            this.externalContentMatches = new ArrayList<>();
        } else {
            this.externalContentMatches = externalContentMatches;
        }
    }

    @Override
    public String toString()
    {
        return new Gson().toJson(this);
    }
}
