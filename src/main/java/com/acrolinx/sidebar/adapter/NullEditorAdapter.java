/* Copyright (c) 2016-present Acrolinx GmbH */

package com.acrolinx.sidebar.adapter;

import java.util.Collections;
import java.util.List;

import com.acrolinx.sidebar.InputAdapterInterface;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatch;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatchWithReplacement;
import com.acrolinx.sidebar.pojo.document.IntRange;
import com.acrolinx.sidebar.pojo.document.externalContent.ExternalContent;
import com.acrolinx.sidebar.pojo.settings.InputFormat;

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
    public void selectRanges(String s, List<AcrolinxMatch> list)
    {
        //
    }

    @Override
    public void replaceRanges(String s, List<AcrolinxMatchWithReplacement> list)
    {
        //
    }

    @Override
    public List<IntRange> getCurrentSelection()
    {
        return Collections.emptyList();
    }
}
