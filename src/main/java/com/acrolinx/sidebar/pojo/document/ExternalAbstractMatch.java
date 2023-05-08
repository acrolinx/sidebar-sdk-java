/* Copyright (c) 2022-present Acrolinx GmbH */

package com.acrolinx.sidebar.pojo.document;

import java.util.List;

import com.acrolinx.sidebar.pojo.document.externalContent.ExternalContentMatch;

public abstract class ExternalAbstractMatch extends AbstractMatch
{
    public abstract boolean hasExternalContentMatches();

    public abstract List<ExternalContentMatch> getExternalContentMatches();
}
