/* Copyright (c) 2017 Acrolinx GmbH */


package com.acrolinx.sidebar.lookup;

public interface ContentNode
{
    int getStartOffset();

    int getEndOffset();

    String getContent();

    String getAsXMLFragment();
}
