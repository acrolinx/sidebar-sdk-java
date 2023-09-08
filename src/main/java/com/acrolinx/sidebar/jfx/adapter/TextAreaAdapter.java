/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.jfx.adapter;

import com.acrolinx.sidebar.InputAdapterInterface;
import com.acrolinx.sidebar.lookup.MatchComparator;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatch;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatchWithReplacement;
import com.acrolinx.sidebar.pojo.document.IntRange;
import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContent;
import com.acrolinx.sidebar.pojo.settings.InputFormat;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.IndexRange;
import javafx.scene.control.TextArea;

public class TextAreaAdapter implements InputAdapterInterface
{
    private final TextArea textArea;
    private InputFormat inputFormat;
    private String documentReference;

    public TextAreaAdapter(final TextArea textArea, final InputFormat inputFormat, final String documentReference)
    {
        this.textArea = textArea;
        this.inputFormat = inputFormat;
        this.documentReference = documentReference;
    }

    public TextArea getTextArea()
    {
        return textArea;
    }

    @Override
    public synchronized InputFormat getInputFormat()
    {
        return inputFormat;
    }

    public synchronized void setInputFormat(final InputFormat inputFormat)
    {
        this.inputFormat = inputFormat;
    }

    @Override
    public synchronized String getContent()
    {
        return textArea.getText();
    }

    @Override
    public ExternalContent getExternalContent()
    {
        return null;
    }

    @Override
    public synchronized String getDocumentReference()
    {
        return documentReference;
    }

    public synchronized void setDocumentReference(final String documentReference)
    {
        this.documentReference = documentReference;
    }

    @Override
    public synchronized void selectRanges(final String checkId, final List<AcrolinxMatch> acrolinxMatches)
    {
        final int minRange = acrolinxMatches.get(0).getRange().getMinimumInteger();
        final int maxRange = acrolinxMatches.get(acrolinxMatches.size() - 1).getRange().getMaximumInteger();
        textArea.selectRange(minRange, maxRange);
    }

    @Override
    public synchronized void replaceRanges(final String checkId, final List<AcrolinxMatchWithReplacement> matches)
    {
        matches.stream().sorted(new MatchComparator().reversed()).forEach(match -> textArea.replaceText(
                match.getRange().getMinimumInteger(), match.getRange().getMaximumInteger(), match.getReplacement()));
    }

    @Override
    public List<IntRange> getCurrentSelection()
    {
        final IndexRange selection = this.textArea.getSelection();
        final int start = selection.getStart();
        final int end = selection.getEnd();
        final List<IntRange> intRanges = new ArrayList<>();
        intRanges.add(new IntRange(start, end));
        return intRanges;
    }
}
