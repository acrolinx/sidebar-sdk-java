/**
 *  Copyright (c) 2020-present Acrolinx GmbH
 */
package com.acrolinx.sidebar;

import com.acrolinx.sidebar.reuse.ReuseState;


public interface AcrolinxReuseComponentInterface {
    void queryCurrentSentence();

    void setReuseState(ReuseState reuseState);
}
