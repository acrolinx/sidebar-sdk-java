/*
 * Copyright (c) 2016-2017 Acrolinx GmbH
 */

package com.acrolinx.sidebar.pojo.document;

@SuppressWarnings("unused")
public class AcrolinxMatch extends AbstractMatch
{
    private final String content;
    private IntRange extractedRange;
    private IntRange range;

    public AcrolinxMatch(IntRange range, String content)
    {
        this.content = content;
        this.range = range;
    }

    public AcrolinxMatch(IntRange range, IntRange extractedRange, String content)
    {
        this.range = range;
        this.extractedRange = extractedRange;
        this.content = content;
    }

    public static AcrolinxMatch newInstance(AcrolinxMatch match)
    {
        String content = match.getContent();
        IntRange intRange = match.getRange();
        IntRange range = new IntRange(intRange.getMinimumInteger(), intRange.getMaximumInteger());
        if (match.getExtractedRange() != null) {
            IntRange extRange = match.getExtractedRange();
            IntRange eRange = new IntRange(extRange.getMinimumInteger(), extRange.getMaximumInteger());
            return new AcrolinxMatch(range, eRange, content);
        } else {
            return new AcrolinxMatch(range, content);
        }
    }

    public String getContent()
    {
        return content;
    }

    public IntRange getExtractedRange()
    {
        return extractedRange;
    }

    public IntRange getRange()
    {
        return range;
    }

    @Override
    public void setRange(IntRange range)
    {
        this.range = range;
    }

    @Override
    public AbstractMatch copy()
    {
        int rangeMin = this.range.getMinimumInteger();
        int rangeMax = this.range.getMaximumInteger();
        if (this.extractedRange != null) {
            int eRangeMin = this.extractedRange.getMinimumInteger();
            int eRangeMax = this.extractedRange.getMaximumInteger();

            return new AcrolinxMatch(new IntRange(rangeMin, rangeMax), new IntRange(eRangeMin, eRangeMax), content);
        } else {
            return new AcrolinxMatch(new IntRange(rangeMin, rangeMax), content);
        }
    }
}
