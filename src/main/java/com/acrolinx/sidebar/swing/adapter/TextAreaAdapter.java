/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.swing.adapter;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acrolinx.sidebar.InputAdapterInterface;
import com.acrolinx.sidebar.lookup.MatchComparator;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatch;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatchWithReplacement;
import com.acrolinx.sidebar.pojo.document.IntRange;
import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContent;
import com.acrolinx.sidebar.pojo.settings.InputFormat;

/**
 * Demo adapter for Swing JTextArea.
 * 
 * @see InputAdapterInterface
 */
public class TextAreaAdapter implements InputAdapterInterface
{
    private static final Logger logger = LoggerFactory.getLogger(TextAreaAdapter.class);

    private final JTextArea textArea;
    private InputFormat inputFormat;
    private String documentReference;

    public TextAreaAdapter(JTextArea textArea, InputFormat inputFormat, String documentReference)
    {
        this.textArea = textArea;
        this.inputFormat = inputFormat;
        this.documentReference = documentReference;
    }

    public JTextArea getTextArea()
    {
        return textArea;
    }

    @Override
    public InputFormat getInputFormat()
    {
        return inputFormat;
    }

    public void setInputFormat(InputFormat inputFormat)
    {
        this.inputFormat = inputFormat;
    }

    @Override
    public String getContent()
    {
        return textArea.getText();
    }

    @Override
    public ExternalContent getExternalContent()
    {
        return null;
    }

    @Override
    public String getDocumentReference()
    {
        return documentReference;
    }

    @Override
    public void selectRanges(String checkId, List<AcrolinxMatch> acrolinxMatches)
    {
        int minRange = acrolinxMatches.get(0).getRange().getMinimumInteger();
        int maxRange = acrolinxMatches.get(acrolinxMatches.size() - 1).getRange().getMaximumInteger();
        Highlighter h = textArea.getHighlighter();
        h.removeAllHighlights();
        try {
            h.addHighlight(minRange, maxRange, DefaultHighlighter.DefaultPainter);
        } catch (BadLocationException e) {
            logger.error("", e);
        }
    }

    @Override
    public void replaceRanges(String checkId, List<AcrolinxMatchWithReplacement> matches)
    {
        matches.stream().sorted(new MatchComparator().reversed()).forEach(match -> {
            int minRange = match.getRange().getMinimumInteger();
            int maxRange = match.getRange().getMaximumInteger();
            String replacement = match.getReplacement();
            Highlighter h = textArea.getHighlighter();
            h.removeAllHighlights();
            textArea.replaceRange(replacement, minRange, maxRange);
        });
    }

    @Override
    public List<IntRange> getCurrentSelection()
    {
        int selectionStart = this.textArea.getSelectionStart();
        int selectionEnd = this.textArea.getSelectionEnd();
        List<IntRange> ranges = new ArrayList<>();
        ranges.add(new IntRange(selectionStart, selectionEnd));
        return ranges;
    }
}
