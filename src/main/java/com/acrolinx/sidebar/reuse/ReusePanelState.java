/**
 *  Copyright (c) 2022-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.reuse;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReusePanelState {

    public static final ReusePanelState EMPTY_REUSE_PANEL_STATE = new ReusePanelState(ReuseMessage.NO_MESSAGE,new ArrayList<>(),"","",false, Locale.ENGLISH);
    private final ReuseMessage message;
    private final boolean loading;
    private final List<ReuseSuggestion> suggestions;
    private final String searchString;
    private final String currentString;
    private final Locale language;

    public ReusePanelState(ReuseMessage message, List<ReuseSuggestion> suggestions, String searchString, String currentString, boolean loading, Locale language) {
        this.message = message;
        this.loading = loading;
        this.suggestions = suggestions;
        this.searchString = searchString;
        this.currentString = currentString;
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

    public String getCurrentString() {
        return currentString;
    }

    public ReuseMessage getMessage() {
        return message;
    }

    public String toJSON() {
        return (new Gson()).toJson(this);
    }

    public Locale getLanguage() {
        return language;
    }

    public ReusePanelState changeMessage(ReuseMessage message) {
        return new ReusePanelState(message,this.suggestions,this.searchString,this.currentString,this.loading,this.language);
    }
    public ReusePanelState changeSuggestions(List<ReuseSuggestion> reuseSuggestions) {
        return new ReusePanelState(this.message,reuseSuggestions,this.searchString,this.currentString,this.loading,this.language);
    }
    public ReusePanelState changeSearchString(String searchString) {
        return new ReusePanelState(this.message,this.suggestions,searchString,this.currentString,this.loading,this.language);
    }
    public ReusePanelState changeCurrentString(String currentString) {
        return new ReusePanelState(this.message,this.suggestions,this.searchString,currentString,this.loading,this.language);
    }
    public ReusePanelState changeLoading(boolean loading) {
        return new ReusePanelState(this.message,this.suggestions,this.searchString,this.currentString,loading,this.language);
    }
    public ReusePanelState changeLanguage(Locale language) {
        return new ReusePanelState(this.message,this.suggestions,this.searchString,this.currentString,this.loading,language);
    }
}
