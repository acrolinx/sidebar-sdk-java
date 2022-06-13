/* Copyright (c) 2016-present Acrolinx GmbH */

package com.acrolinx.sidebar.pojo.document;

import com.acrolinx.sidebar.pojo.document.externalContent.ExternalContentMatch;

import java.util.List;

//todo: Refactor for Matches with External Matches
@SuppressWarnings({"unused", "WeakerAccess"})
public class AcrolinxMatch extends AbstractMatch
{
    private final String content;
    private IntRange extractedRange;
    private IntRange range;
    private List<ExternalContentMatch> externalContentMatches;

    public AcrolinxMatch(final IntRange range, final String content)
    {
        this.content = content;
        this.range = range;
    }

    public AcrolinxMatch(final IntRange range, final IntRange extractedRange, final String content)
    {
        this(range, content);
        this.extractedRange = extractedRange;
    }

    public AcrolinxMatch(final IntRange range, final IntRange extractedRange, final String content, final List<ExternalContentMatch> externalContentMatches)
    {
        this(range, extractedRange, content);
        this.externalContentMatches = externalContentMatches;
    }

    public AcrolinxMatch(final IntRange range, final String content, final List<ExternalContentMatch> externalContentMatches)
    {
        this(range, content);
        this.externalContentMatches = externalContentMatches;
    }

    @Override
    public String getContent()
    {
        return content;
    }

    public IntRange getExtractedRange()
    {
        return extractedRange;
    }

    @Override
    public IntRange getRange()
    {
        return range;
    }

    @Override
    public AcrolinxMatch setRange(final IntRange range)
    {
        if (this.extractedRange != null) {
            final int eRangeMin = this.extractedRange.getMinimumInteger();
            final int eRangeMax = this.extractedRange.getMaximumInteger();

            if(this.getExternalContentMatches() != null) {
                final List<ExternalContentMatch> externalMatches = this.getExternalContentMatches();
                return new AcrolinxMatch(range, new IntRange(eRangeMin, eRangeMax), content, externalMatches);
            }
            return new AcrolinxMatch(range, new IntRange(eRangeMin, eRangeMax), content);
        } else {

            if(this.getExternalContentMatches() != null) {
                final List<ExternalContentMatch> externalMatches = this.getExternalContentMatches();
                return new AcrolinxMatch(range, content, externalMatches);
            }
            return new AcrolinxMatch(range, content);
        }
    }
    @Override
    public boolean hasExternalContentMatches () {
        return this.externalContentMatches != null && this.getExternalContentMatches().size() > 0;
    }

    @Override
    public List<ExternalContentMatch> getExternalContentMatches() {
        return this.externalContentMatches;
    }

    @Override
    public AbstractMatch copy()
    {
        final int rangeMin = this.range.getMinimumInteger();
        final int rangeMax = this.range.getMaximumInteger();
        if (this.extractedRange != null) {
            final int eRangeMin = this.extractedRange.getMinimumInteger();
            final int eRangeMax = this.extractedRange.getMaximumInteger();

            if(this.getExternalContentMatches() != null) {
                final List<ExternalContentMatch> externalMatches = this.getExternalContentMatches();
                return new AcrolinxMatch(new IntRange(rangeMin, rangeMax), new IntRange(eRangeMin, eRangeMax), content, externalMatches);
            }
            return new AcrolinxMatch(new IntRange(rangeMin, rangeMax), new IntRange(eRangeMin, eRangeMax), content);
        } else {
            if(this.getExternalContentMatches() != null) {
                final List<ExternalContentMatch> externalMatches = this.getExternalContentMatches();
                return new AcrolinxMatch(new IntRange(rangeMin, rangeMax), content, externalMatches);
            }
            return new AcrolinxMatch(new IntRange(rangeMin, rangeMax), content);
        }
    }

}
