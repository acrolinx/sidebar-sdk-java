/**
 *  Copyright (c) 2022-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.reuse;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class ReuseState {


    private final boolean loading;
    private final List<String> preferredPhrases;
    private final String originalPhrase;
    private final String currentSentence;

    public ReuseState( List<String> preferredPhrases, String originalPhrase, String currentSentence,boolean loading) {
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

    public String toJSON() {
        String phrasesJSON = preferredPhrases.size() > 0 ? "['"+ StringUtils.join(preferredPhrases,"','")+"']":"[]";
        String preferredPhrasesJSON = "'phrases':"+phrasesJSON;
        String loadingJSON = "'loading':" + (loading ? "true":"false");
        String originalPhraseJSON = "'originalPhrase':'"+originalPhrase+"'";
        String currentSentenceJSON = "'currentSentence':'"+currentSentence+"'";
        String[] jsons = {preferredPhrasesJSON,loadingJSON,originalPhraseJSON,currentSentenceJSON};

        return "{"+StringUtils.join(jsons,",")+"}";
    }
}
