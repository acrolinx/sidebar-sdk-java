/**
 *  Copyright (c) 2020-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.pojo.document.externalContent;

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

    public int getOriginalBegin() {
        return originalBegin;
    }

    public int getOriginalEnd() {
        return originalEnd;
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
