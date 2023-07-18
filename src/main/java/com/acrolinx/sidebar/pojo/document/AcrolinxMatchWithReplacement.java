/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.document;

import java.util.List;

import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContentMatch;

public class AcrolinxMatchWithReplacement extends AcrolinxMatch
{
    private String replacement;

    public AcrolinxMatchWithReplacement(String content, IntRange intRange, String replacement)
    {
        super(intRange, content);
        this.replacement = replacement;
    }

    public AcrolinxMatchWithReplacement(String content, IntRange intRange, String replacement,
            List<ExternalContentMatch> externalContentMatches)
    {
        super(intRange, content, externalContentMatches);
        this.replacement = replacement;
    }

    public AcrolinxMatchWithReplacement(IntRange intRange, IntRange extractedRange, String content, String replacement)
    {
        super(intRange, extractedRange, content);
        this.replacement = replacement;
    }

    public AcrolinxMatchWithReplacement(IntRange intRange, IntRange extractedRange, String content, String replacement,
            List<ExternalContentMatch> externalContentMatches)
    {
        super(intRange, extractedRange, content, externalContentMatches);
        this.replacement = replacement;
    }

    public String getReplacement()
    {
        return replacement;
    }

    @Override
    public AcrolinxMatch setRange(IntRange intRange)
    {
        if (getExtractedRange() != null) {
            int minRange = super.getExtractedRange().getMinimumInteger();
            int maxRange = super.getExtractedRange().getMaximumInteger();

            if (this.getExternalContentMatches() != null) {
                return new AcrolinxMatchWithReplacement(intRange, new IntRange(minRange, maxRange), getContent(),
                        replacement, this.getExternalContentMatches());
            }
            return new AcrolinxMatchWithReplacement(intRange, new IntRange(minRange, maxRange), getContent(),
                    replacement);
        }

        if (this.getExternalContentMatches() != null) {
            return new AcrolinxMatchWithReplacement(getContent(), intRange, replacement,
                    this.getExternalContentMatches());
        }
        return new AcrolinxMatchWithReplacement(getContent(), intRange, replacement);
    }

    public AcrolinxMatchWithReplacement setReplacement(String replacement)
    {
        int rangeMin = getRange().getMinimumInteger();
        int rangeMax = getRange().getMaximumInteger();
        IntRange intRange = new IntRange(rangeMin, rangeMax);

        if (getExtractedRange() != null) {
            int minRange = super.getExtractedRange().getMinimumInteger();
            int maxRange = super.getExtractedRange().getMaximumInteger();

            if (this.getExternalContentMatches() != null) {
                return new AcrolinxMatchWithReplacement(intRange, new IntRange(minRange, maxRange), getContent(),
                        replacement, this.getExternalContentMatches());
            }
            return new AcrolinxMatchWithReplacement(intRange, new IntRange(minRange, maxRange), getContent(),
                    replacement);
        }

        if (this.getExternalContentMatches() != null) {
            return new AcrolinxMatchWithReplacement(getContent(), intRange, replacement,
                    this.getExternalContentMatches());
        }
        return new AcrolinxMatchWithReplacement(getContent(), intRange, replacement);
    }

    @Override
    public AcrolinxMatchWithReplacement copy()
    {
        int rangeMin = getRange().getMinimumInteger();
        int rangeMax = getRange().getMaximumInteger();
        String content = getContent();
        IntRange intRange = new IntRange(rangeMin, rangeMax);

        if (getExtractedRange() != null) {
            int minRange = super.getExtractedRange().getMinimumInteger();
            int maxRange = super.getExtractedRange().getMaximumInteger();

            if (this.getExternalContentMatches() != null) {
                final List<ExternalContentMatch> externalContentMatches = this.getExternalContentMatches();
                return new AcrolinxMatchWithReplacement(intRange, new IntRange(minRange, maxRange), getContent(),
                        getReplacement(), externalContentMatches);
            }
            return new AcrolinxMatchWithReplacement(intRange, new IntRange(minRange, maxRange), getContent(),
                    getReplacement());
        }

        if (this.getExternalContentMatches() != null) {
            final List<ExternalContentMatch> externalContentMatches = this.getExternalContentMatches();
            return new AcrolinxMatchWithReplacement(content, intRange, getReplacement(), externalContentMatches);
        }

        return new AcrolinxMatchWithReplacement(content, intRange, getReplacement());
    }
}
