/**
 *  Copyright (c) 2020-present Acrolinx GmbH
 */
package com.acrolinx.sidebar;

import java.util.List;

public interface AcrolinxReuseComponentInterface {

    public void showPreferredPhrases(List<String> preferredPhrases);

    public void showPreferredPhrasesAndOriginal(List<String> preferredPhrases, String original);

    public void setLoading(boolean loading,String queriedPhrase);

    public void setCurrentSentence(String currentSentence);
    public void queryCurrentSentence();
}
