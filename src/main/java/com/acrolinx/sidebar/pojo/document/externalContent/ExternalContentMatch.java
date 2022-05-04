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
    private IntRange range;
    private final List<ExternalContentMatch> externalContentMatches;


    public ExternalContentMatch(String id, String type, IntRange range, List<ExternalContentMatch> externalContentMatches) {
        this.id = id;
        this.type = type;
        this.range = range;
        this.externalContentMatches = externalContentMatches;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public IntRange getRange() {
        return range;
    }

    @Override
    public String toString()
    {
        return new Gson().toJson(this);
    }

    public List<ExternalContentMatch> getExternalContentMatches() {
        return externalContentMatches;
    }
}
