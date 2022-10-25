/**
 *  Copyright (c) 2022-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.reuse;

import com.google.gson.Gson;

public class ReuseSuggestion {
    private final String suggestion;
    private final String description;

    public ReuseSuggestion(String suggestion, String description) {
        this.suggestion = suggestion;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public String toJSON() {
        return (new Gson()).toJson(this);
    }
}
