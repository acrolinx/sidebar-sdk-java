/**
 *  Copyright (c) 2022-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.live;

import java.util.List;
import java.util.stream.Collectors;

public class LiveResponseJSON {

    private List<LiveSuggestionJSON> results;
    private String requestId;

    public LiveResponse toLiveResponse() {
        List<LiveSuggestion> suggestions = null;
        if(results != null) {
            suggestions = results.stream().map(LiveSuggestionJSON::toLiveSuggestion).collect(Collectors.toList());
        }
        return new LiveResponse(requestId,suggestions);
    }
}
