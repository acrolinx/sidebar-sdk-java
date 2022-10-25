/**
 *  Copyright (c) 2022-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.reuse;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ReusePanelState {

    public static final ReusePanelState EMPTY_REUSE_PANEL_STATE = new ReusePanelState("",new ArrayList<>(),"","",false);
    private final String message;
    private final boolean loading;
    private final List<ReuseSuggestion> suggestions;
    private final String searchString;
    private final String potentialNextSearchString;

    public ReusePanelState(String message, List<ReuseSuggestion> suggestions, String searchString, String potentialNextSearchString, boolean loading) {
        this.message = message;
        this.loading = loading;
        this.suggestions = suggestions;
        this.searchString = searchString;
        this.potentialNextSearchString = potentialNextSearchString;
    }

    public boolean isLoading() {
        return loading;
    }

    public List<ReuseSuggestion> getSuggestions() {
        return suggestions;
    }

    public String getSearchString() {
        return searchString;
    }

    public String getPotentialNextSearchString() {
        return potentialNextSearchString;
    }

    public String getMessage() {
        return message;
    }

    public String toJSON() {
        return (new Gson()).toJson(this);
    }
}
