/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.lookup;

import java.io.Serializable;
import java.util.Comparator;

import com.acrolinx.sidebar.pojo.document.AbstractMatch;

public class MatchComparator implements Comparator<AbstractMatch>, Serializable
{
    private static final long serialVersionUID = 485264620726058219L;

    @Override
    public int compare(final AbstractMatch abstractMatch1, final AbstractMatch abstractMatch2)
    {
        return Integer.compare(abstractMatch1.getRange().getMinimumInteger(),
                abstractMatch2.getRange().getMinimumInteger());
    }
}
