/**
 *  Copyright (c) 2022-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.reuse;

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
        return "{'suggestion':'"+getSuggestion()+"','description':'"+description+"'}";
    }
}
