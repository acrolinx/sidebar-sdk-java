/**
 *  Copyright (c) 2020-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.pojo.document.externalContent;

import com.acrolinx.sidebar.pojo.document.IntRange;

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

    public List<ExternalContentMatch> getExternalContentMatches() {
        return externalContentMatches;
    }
}
