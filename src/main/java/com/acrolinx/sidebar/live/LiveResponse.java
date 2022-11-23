/**
 *  Copyright (c) 2022-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.live;

import java.util.List;

public class LiveResponse {

    private final String requestID;
    private final List<LiveSuggestion> suggestions;

    public LiveResponse(String requestID, List<LiveSuggestion> suggestions) {
        this.requestID = requestID;
        this.suggestions = suggestions;
    }

    public String getRequestID() {
        return requestID;
    }

    public List<LiveSuggestion> getSuggestions() {
        return suggestions;
    }


}
