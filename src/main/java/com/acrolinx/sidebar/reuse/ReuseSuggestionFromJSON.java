/**
 *  Copyright (c) 2022-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.reuse;

@SuppressWarnings({"MismatchedReadAndWriteOfArray", "unused"})
public class ReuseSuggestionFromJSON {
    private String preferredPhrase;
    private String description;

    public ReuseSuggestionFromJSON() {
        //
    }

    public ReuseSuggestion getSuggestion() {
        return new ReuseSuggestion(preferredPhrase,description);
    }

}
