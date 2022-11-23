/**
 *  Copyright (c) 2022-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.live;

@SuppressWarnings({"MismatchedReadAndWriteOfArray", "unused"})
public class LiveSuggestionFromJSON {
    private String preferredPhrase;
    private String description;

    public LiveSuggestionFromJSON() {
        //
    }

    public LiveSuggestion getSuggestion() {
        return new LiveSuggestion(preferredPhrase,description);
    }

}
