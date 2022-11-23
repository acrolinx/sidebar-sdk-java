/**
 *  Copyright (c) 2022-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.live;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"MismatchedReadAndWriteOfArray", "unused"})
public class LiveResponseFromJSON {

    private String requestID;
    private List<LiveSuggestionFromJSON> suggestions;

    public LiveResponseFromJSON() {
    }

    public LiveResponse getReuseResponse() {
        List<LiveSuggestion> suggestionList = suggestions.stream().map(LiveSuggestionFromJSON::getSuggestion).collect(Collectors.toList());
        return new LiveResponse(requestID,suggestionList);
    }
}
