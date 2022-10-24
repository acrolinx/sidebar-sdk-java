/**
 *  Copyright (c) 2022-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.reuse;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"MismatchedReadAndWriteOfArray", "unused"})
public class ReuseResponseFromJSON {

    private String requestID;
    private List<ReuseSuggestionFromJSON> suggestions;

    public ReuseResponseFromJSON() {
    }

    public ReuseResponse getReuseResponse() {
        List<ReuseSuggestion> suggestionList = suggestions.stream().map(ReuseSuggestionFromJSON::getSuggestion).collect(Collectors.toList());
        return new ReuseResponse(requestID,suggestionList);
    }
}
