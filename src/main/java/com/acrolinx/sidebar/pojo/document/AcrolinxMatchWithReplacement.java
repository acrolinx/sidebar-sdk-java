/* Copyright (c) 2016-present Acrolinx GmbH */

package com.acrolinx.sidebar.pojo.document;

import com.acrolinx.sidebar.pojo.document.externalContent.ExternalContentMatch;

import java.util.List;

public class AcrolinxMatchWithReplacement extends AcrolinxMatch
{
    private String replacement;

    public AcrolinxMatchWithReplacement(String content, IntRange range, String replacement)
    {
        super(range, content);
        this.replacement = replacement;
    }

    public AcrolinxMatchWithReplacement(String content, IntRange range, String replacement, List<ExternalContentMatch> externalContentMatches)
    {
        super(range, content, externalContentMatches);
        this.replacement = replacement;
    }

    public AcrolinxMatchWithReplacement(IntRange range, IntRange extractedRange, String content, String replacement)
    {
        super(range, extractedRange, content);
        this.replacement = replacement;
    }

    public AcrolinxMatchWithReplacement(IntRange range, IntRange extractedRange, String content, String replacement, List<ExternalContentMatch> externalContentMatches)
    {
        super(range, extractedRange, content, externalContentMatches);
        this.replacement = replacement;
    }

    public String getReplacement()
    {
        return replacement;
    }

    @Override
    public AcrolinxMatch setRange(IntRange range)
    {
        if (getExtractedRange() != null) {
            int minRange = super.getExtractedRange().getMinimumInteger();
            int maxRange = super.getExtractedRange().getMaximumInteger();

            if(this.getExternalContentMatches() != null) {
                return new AcrolinxMatchWithReplacement(range, new IntRange(minRange, maxRange), getContent(), replacement, this.getExternalContentMatches());
            }
            return new AcrolinxMatchWithReplacement(range, new IntRange(minRange, maxRange), getContent(), replacement);
        }

        if(this.getExternalContentMatches() != null) {
            return new AcrolinxMatchWithReplacement(getContent(), range, replacement, this.getExternalContentMatches());
        }
        return new AcrolinxMatchWithReplacement(getContent(), range, replacement);
    }

    public AcrolinxMatchWithReplacement setReplacement(String replacement)
    {
        int rangeMin = getRange().getMinimumInteger();
        int rangeMax = getRange().getMaximumInteger();
        IntRange range = new IntRange(rangeMin, rangeMax);
        if (getExtractedRange() != null) {
            int minRange = super.getExtractedRange().getMinimumInteger();
            int maxRange = super.getExtractedRange().getMaximumInteger();

            if(this.getExternalContentMatches() != null) {
                return new AcrolinxMatchWithReplacement(range, new IntRange(minRange, maxRange), getContent(), replacement, this.getExternalContentMatches());
            }
            return new AcrolinxMatchWithReplacement(range, new IntRange(minRange, maxRange), getContent(), replacement);
        }

        if(this.getExternalContentMatches() != null) {
            return new AcrolinxMatchWithReplacement(getContent(), range, replacement, this.getExternalContentMatches());
        }
        return new AcrolinxMatchWithReplacement(getContent(), range, replacement);
    }

    @Override
    public AcrolinxMatchWithReplacement copy()
    {
        int rangeMin = getRange().getMinimumInteger();
        int rangeMax = getRange().getMaximumInteger();
        String content = getContent();
        IntRange range = new IntRange(rangeMin, rangeMax);
        if (getExtractedRange() != null) {
            int minRange = super.getExtractedRange().getMinimumInteger();
            int maxRange = super.getExtractedRange().getMaximumInteger();

            if(this.getExternalContentMatches() != null) {
                final List<ExternalContentMatch> externalContentMatches = this.getExternalContentMatches();
                return new AcrolinxMatchWithReplacement(range, new IntRange(minRange, maxRange), getContent(),
                        getReplacement(), externalContentMatches);
            }
            return new AcrolinxMatchWithReplacement(range, new IntRange(minRange, maxRange), getContent(),
                    getReplacement());
        }

        if(this.getExternalContentMatches() != null) {
            final List<ExternalContentMatch> externalContentMatches = this.getExternalContentMatches();
            return new AcrolinxMatchWithReplacement(content, range, getReplacement(), externalContentMatches);
        }
        return new AcrolinxMatchWithReplacement(content, range, getReplacement());
    }
}
