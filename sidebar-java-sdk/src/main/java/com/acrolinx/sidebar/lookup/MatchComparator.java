/* Copyright (c) 2016-present Acrolinx GmbH */

package com.acrolinx.sidebar.lookup;

import java.io.Serializable;
import java.util.Comparator;

import com.acrolinx.sidebar.pojo.document.AbstractMatch;

public class MatchComparator implements Comparator<AbstractMatch>, Serializable
{
    private static final long serialVersionUID = 485264620726058219L;

    @Override
    public int compare(final AbstractMatch o1, final AbstractMatch o2)
    {
        return Integer.compare(o1.getRange().getMinimumInteger(), o2.getRange().getMinimumInteger());
    }
}
