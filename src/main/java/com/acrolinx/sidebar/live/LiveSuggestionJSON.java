/**
 *  Copyright (c) 2022-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.live;

public class LiveSuggestionJSON {

    private String preferredPhrase;
    private String description;

    public LiveSuggestion toLiveSuggestion() {
        return new LiveSuggestion(preferredPhrase,description);
    }
}
