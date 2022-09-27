/**
 *  Copyright (c) 2022-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.reuse;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ReusePanelState {

    public static final ReusePanelState EMPTY_REUSE_PANEL_STATE = new ReusePanelState("",new ArrayList<>(),"","",false);

    private final String message;
    private final boolean loading;
    private final List<String> suggestions;
    private final String originalPhrase;
    private final String currentSentence;

    public ReusePanelState(String message, List<String> suggestions, String originalPhrase, String currentSentence, boolean loading) {
        this.message = message;
        this.loading = loading;
        this.suggestions = suggestions;
        this.originalPhrase = originalPhrase;
        this.currentSentence = currentSentence;
    }

    public boolean isLoading() {
        return loading;
    }

    public List<String> getSuggestions() {
        return suggestions;
    }

    public String getOriginalPhrase() {
        return originalPhrase;
    }

    public String getCurrentSentence() {
        return currentSentence;
    }

    public String getMessage() {
        return message;
    }

    public String toJSON() {
        String phrasesJSON = suggestions.size() > 0 ? "['"+ StringUtils.join(suggestions,"','")+"']":"[]";
        String preferredPhrasesJSON = "'phrases':"+phrasesJSON;
        String loadingJSON = "'loading':" + (loading ? "true":"false");
        String originalPhraseJSON = "'originalPhrase':'"+originalPhrase+"'";
        String currentSentenceJSON = "'currentSentence':'"+currentSentence+"'";
        String messageJSON = "'message':'"+message+"'";
        String[] jsons = {preferredPhrasesJSON,loadingJSON,originalPhraseJSON,currentSentenceJSON,messageJSON};

        return "{"+StringUtils.join(jsons,",")+"}";
    }
}
