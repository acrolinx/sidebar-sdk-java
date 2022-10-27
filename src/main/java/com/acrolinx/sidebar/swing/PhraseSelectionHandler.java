/**
 *  Copyright (c) 2020-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.swing;

public interface PhraseSelectionHandler {
    public void onPhraseSelected(String phrase);

    public void queryCurrentSentence();

    public void closeReusePanel();
}
