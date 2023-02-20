/**
 *  Copyright (c) 2022-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.live;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LivePanelState {

    public static final LivePanelState EMPTY_LIVE_PANEL_STATE = new LivePanelState(LiveMessage.NO_MESSAGE,new ArrayList<>(),"","",false, Locale.ENGLISH);
    private final LiveMessage message;
    private final boolean loading;
    private final List<LiveSuggestion> suggestions;
    private final String searchString;
    private final String currentString;
    private final Locale language;

    public LivePanelState(LiveMessage message, List<LiveSuggestion> suggestions, String searchString, String currentString, boolean loading, Locale language) {
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

    public List<LiveSuggestion> getSuggestions() {
        return suggestions;
    }

    public String getSearchString() {
        return searchString;
    }

    public String getCurrentString() {
        return currentString;
    }

    public LiveMessage getMessage() {
        return message;
    }

    public String toJSON() {
        return (new Gson()).toJson(this);
    }

    public Locale getLanguage() {
        return language;
    }

    public LivePanelState changeMessage(LiveMessage message) {
        return new LivePanelState(message,this.suggestions,this.searchString,this.currentString,this.loading,this.language);
    }
    public LivePanelState changeSuggestions(List<LiveSuggestion> liveSuggestions) {
        return new LivePanelState(this.message,liveSuggestions,this.searchString,this.currentString,this.loading,this.language);
    }
    public LivePanelState changeSearchString(String searchString) {
        return new LivePanelState(this.message,this.suggestions,searchString,this.currentString,this.loading,this.language);
    }
    public LivePanelState changeCurrentString(String currentString) {
        return new LivePanelState(this.message,this.suggestions,this.searchString,currentString,this.loading,this.language);
    }
    public LivePanelState changeLoading(boolean loading) {
        return new LivePanelState(this.message,this.suggestions,this.searchString,this.currentString,loading,this.language);
    }
    public LivePanelState changeLanguage(Locale language) {
        return new LivePanelState(this.message,this.suggestions,this.searchString,this.currentString,this.loading,language);
    }
}
