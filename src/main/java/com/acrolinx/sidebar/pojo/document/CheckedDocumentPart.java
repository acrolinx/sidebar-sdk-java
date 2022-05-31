/* Copyright (c) 2016-present Acrolinx GmbH */

package com.acrolinx.sidebar.pojo.document;

import com.acrolinx.sidebar.pojo.document.externalContent.ExternalContentMatch;
import com.google.gson.Gson;

import java.util.List;

@SuppressWarnings("unused")
public class CheckedDocumentPart
{
    private final String checkId;
    private final IntRange range;
    private List<ExternalContentMatch> externalContent;

    public CheckedDocumentPart(String checkId, IntRange range)
    {
        this.checkId = checkId;
        this.range = range;
    }

    public CheckedDocumentPart(String checkId, IntRange range, List<ExternalContentMatch> externalContent)
    {
        this(checkId, range);
        this.externalContent = externalContent;
    }

    public String getCheckId()
    {
        return checkId;
    }

    public IntRange getRange()
    {
        return range;
    }

    public List<ExternalContentMatch> getExternalContent() {
        return externalContent;
    }

    @Override
    public String toString()
    {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getAsJS()
    {
        if(externalContent != null) {
            return "{checkId: \"" + checkId + "\", range:[" + range.getMinimumInteger() + "," + range.getMaximumInteger()
                    + "], externalContent:" + externalContent.toString() + "}";
        }
        return "{checkId: \"" + checkId + "\", range:[" + range.getMinimumInteger() + "," + range.getMaximumInteger()
                + "]}";
    }
}
