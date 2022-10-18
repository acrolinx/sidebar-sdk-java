/**
 *  Copyright (c) 2022-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.reuse;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<String> suggestionJSONList = suggestions.stream().map( ReuseSuggestion::toJSON).collect(Collectors.toList());
        String suggestionsJSONValue = suggestions.size() > 0 ? "["+ StringUtils.join(suggestionJSONList,",")+"]":"[]";
        String suggestionsJSON = "'suggestions':"+suggestionsJSONValue;
        String loadingJSON = "'loading':" + (loading ? "true":"false");
        String searchStringJSON = "'searchString':'"+ searchString +"'";
        String potentialNextSearchStringJSON = "'potentialNextSearchString':'"+ potentialNextSearchString +"'";
        String messageJSON = "'message':'"+message+"'";
        String[] jsons = {suggestionsJSON,loadingJSON,searchStringJSON,potentialNextSearchStringJSON,messageJSON};

        return "{"+StringUtils.join(jsons,",")+"}";
    }
}
