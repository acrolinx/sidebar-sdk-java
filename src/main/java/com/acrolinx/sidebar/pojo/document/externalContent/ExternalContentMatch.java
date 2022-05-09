/**
 *  Copyright (c) 2020-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.pojo.document.externalContent;

import com.acrolinx.sidebar.pojo.document.IntRange;
import com.google.gson.Gson;

import java.util.List;

public class ExternalContentMatch {

    private final String id;
    private final String type;
    private final int originalBegin;
    private final int originalEnd;
    private final List<ExternalContentMatch> externalContentMatches;


    public ExternalContentMatch(String id, String type, int originalBegin, int originalEnd, List<ExternalContentMatch> externalContentMatches) {
        this.id = id;
        this.type = type;
        this.originalBegin = originalBegin;
        this.originalEnd = originalEnd;
        this.externalContentMatches = externalContentMatches;
    }

    public ExternalContentMatch(String id, String type, int originalBegin, int originalEnd) {
        this.id = id;
        this.type = type;
        this.originalBegin = originalBegin;
        this.originalEnd = originalEnd;
        this.externalContentMatches = null;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public IntRange getRange()
    {
        final IntRange range = new IntRange(this.originalBegin, this.originalEnd);
        return range;
    }

    public ExternalContentMatch setRange(final IntRange range)
    {
        final int originalBegin = range.getMinimumInteger();
        final int originalEnd = range.getMaximumInteger();

        if(this.externalContentMatches != null) {
            return new ExternalContentMatch(this.id, this.type, originalBegin, originalEnd, this.getExternalContentMatches());
        }
        return new ExternalContentMatch(this.id, this.type, originalBegin, originalEnd);
    }

    public List<ExternalContentMatch> getExternalContentMatches() {
        return externalContentMatches;
    }

    @Override
    public String toString()
    {
        return new Gson().toJson(this);
    }

}
