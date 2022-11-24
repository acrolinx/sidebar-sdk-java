/**
 *  Copyright (c) 2022-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.live;

import com.google.gson.Gson;

public class LiveSuggestion {
    private final String suggestion;
    private final String description;

    public LiveSuggestion(String suggestion, String description) {
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
