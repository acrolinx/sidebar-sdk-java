/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.adapter;

import com.acrolinx.sidebar.InputAdapterInterface;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatch;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatchWithReplacement;
import com.acrolinx.sidebar.pojo.document.IntRange;
import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContent;
import com.acrolinx.sidebar.pojo.settings.InputFormat;
import java.util.Collections;
import java.util.List;

/**
 * Use as fallback input adapter.
 * 
 * @see InputAdapterInterface
 */
public class NullEditorAdapter implements InputAdapterInterface
{
    @Override
    public InputFormat getInputFormat()
    {
        return null;
    }

    @Override
    public String getContent()
    {
        return null;
    }

    @Override
    public ExternalContent getExternalContent()
    {
        return null;
    }

    @Override
    public String getDocumentReference()
    {
        return null;
    }

    @Override
    public void selectRanges(String checkId, List<AcrolinxMatch> acrolinxMatches)
    {
        //
    }

    @Override
    public void replaceRanges(String checkId, List<AcrolinxMatchWithReplacement> matches)
    {
        //
    }

    @Override
    public List<IntRange> getCurrentSelection()
    {
        return Collections.emptyList();
    }
}
