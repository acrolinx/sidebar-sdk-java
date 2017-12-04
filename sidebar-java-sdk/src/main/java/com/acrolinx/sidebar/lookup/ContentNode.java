
package com.acrolinx.sidebar.lookup;

public interface ContentNode
{
    int getStartOffset();

    int getEndOffset();

    String getContent();

    String getAsXMLFragment();
}
