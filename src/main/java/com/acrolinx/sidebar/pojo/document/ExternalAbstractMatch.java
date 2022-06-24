package com.acrolinx.sidebar.pojo.document;

import com.acrolinx.sidebar.pojo.document.externalContent.ExternalContentMatch;

import java.util.List;

public abstract class ExternalAbstractMatch extends AbstractMatch {
    public abstract boolean hasExternalContentMatches();

    public abstract List<ExternalContentMatch> getExternalContentMatches();
}
