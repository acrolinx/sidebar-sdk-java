/**
 *  Copyright (c) 2022-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.reuse;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ReusePanelState {

    public static final ReusePanelState EMPTY_REUSE_PANEL_STATE = new ReusePanelState(ReuseMessage.NO_MESSAGE,new ArrayList<>(),"","",false, "en");
    private final ReuseMessage message;
    private final boolean loading;
    private final List<ReuseSuggestion> suggestions;
    private final String searchString;
    private final String potentialNextSearchString;
    private final String language;

    public ReusePanelState(ReuseMessage message, List<ReuseSuggestion> suggestions, String searchString, String potentialNextSearchString, boolean loading, String language) {
        this.message = message;
        this.loading = loading;
        this.suggestions = suggestions;
        this.searchString = searchString;
        this.potentialNextSearchString = potentialNextSearchString;
        this.language = language;
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

    public ReuseMessage getMessage() {
        return message;
    }

    public String toJSON() {
        return (new Gson()).toJson(this);
    }

    public String getLanguage() {
        return language;
    }
}
