/* Copyright (c) 2016-present Acrolinx GmbH */

package com.acrolinx.sidebar.pojo.document;

import com.acrolinx.sidebar.pojo.document.externalContent.ExternalContentMatch;

import java.util.List;

public abstract class AbstractMatch
{
    public abstract IntRange getRange();

    public abstract String getContent();

    public abstract AbstractMatch setRange(IntRange range);

    public abstract AbstractMatch copy();

    public abstract boolean hasExternalContentMatches();
    public abstract List<ExternalContentMatch> getExternalContentMatches();

}
