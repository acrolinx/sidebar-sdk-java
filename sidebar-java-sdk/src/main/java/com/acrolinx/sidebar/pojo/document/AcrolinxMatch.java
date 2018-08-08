/* Copyright (c) 2016-2018 Acrolinx GmbH */

package com.acrolinx.sidebar.pojo.document;

@SuppressWarnings({"unused", "WeakerAccess"})
public class AcrolinxMatch extends AbstractMatch
{
    private final String content;
    private IntRange extractedRange;
    private IntRange range;

    public AcrolinxMatch(final IntRange range, final String content)
    {
        this.content = content;
        this.range = range;
    }

    public AcrolinxMatch(final IntRange range, final IntRange extractedRange, final String content)
    {
        this.range = range;
        this.extractedRange = extractedRange;
        this.content = content;
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

            return new AcrolinxMatch(range, new IntRange(eRangeMin, eRangeMax), content);
        } else {
            return new AcrolinxMatch(range, content);
        }
    }

    @Override
    public AbstractMatch copy()
    {
        final int rangeMin = this.range.getMinimumInteger();
        final int rangeMax = this.range.getMaximumInteger();
        if (this.extractedRange != null) {
            final int eRangeMin = this.extractedRange.getMinimumInteger();
            final int eRangeMax = this.extractedRange.getMaximumInteger();

            return new AcrolinxMatch(new IntRange(rangeMin, rangeMax), new IntRange(eRangeMin, eRangeMax), content);
        } else {
            return new AcrolinxMatch(new IntRange(rangeMin, rangeMax), content);
        }
    }
}
