package com.acrolinx.sidebar.reuse;

import java.util.List;

public class ReuseResponse {

    private final String requestID;
    private final List<ReuseSuggestion> suggestions;

    public ReuseResponse(String requestID, List<ReuseSuggestion> suggestions) {
        this.requestID = requestID;
        this.suggestions = suggestions;
    }

    public String getRequestID() {
        return requestID;
    }

    public List<ReuseSuggestion> getSuggestions() {
        return suggestions;
    }


}
