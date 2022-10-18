package com.acrolinx.sidebar.reuse;

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
