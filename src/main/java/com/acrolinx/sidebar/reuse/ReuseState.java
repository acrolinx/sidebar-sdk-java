/**
 *  Copyright (c) 2022-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.reuse;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ReuseState {

    public static final ReuseState emptyReuseState = new ReuseState("",new ArrayList<>(),"","",false);

    private final String message;
    private final boolean loading;
    private final List<String> preferredPhrases;
    private final String originalPhrase;
    private final String currentSentence;

    public ReuseState(String message, List<String> preferredPhrases, String originalPhrase, String currentSentence, boolean loading) {
        this.message = message;
        this.loading = loading;
        this.preferredPhrases = preferredPhrases;
        this.originalPhrase = originalPhrase;
        this.currentSentence = currentSentence;
    }

    public boolean isLoading() {
        return loading;
    }

    public List<String> getPreferredPhrases() {
        return preferredPhrases;
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
        String phrasesJSON = preferredPhrases.size() > 0 ? "['"+ StringUtils.join(preferredPhrases,"','")+"']":"[]";
        String preferredPhrasesJSON = "'phrases':"+phrasesJSON;
        String loadingJSON = "'loading':" + (loading ? "true":"false");
        String originalPhraseJSON = "'originalPhrase':'"+originalPhrase+"'";
        String currentSentenceJSON = "'currentSentence':'"+currentSentence+"'";
        String messageJSON = "'message':'"+message+"'";
        String[] jsons = {preferredPhrasesJSON,loadingJSON,originalPhraseJSON,currentSentenceJSON,messageJSON};

        return "{"+StringUtils.join(jsons,",")+"}";
    }
}
