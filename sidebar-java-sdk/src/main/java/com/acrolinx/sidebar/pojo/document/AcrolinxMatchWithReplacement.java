/*
 * Copyright (c) 2016-2017 Acrolinx GmbH
 */

package com.acrolinx.sidebar.pojo.document;

public class AcrolinxMatchWithReplacement extends AcrolinxMatch
{
    private String replacement;

    public AcrolinxMatchWithReplacement(String content, IntRange range, String replacement)
    {
        super(range, content);
        this.replacement = replacement;
    }

    public AcrolinxMatchWithReplacement(IntRange range, IntRange extractedRange, String content, String replacement)
    {
        super(range, extractedRange, content);
        this.replacement = replacement;
    }

    public String getReplacement()
    {
        return replacement;
    }

    public void setReplacement(String replacement)
    {
        this.replacement = replacement;
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
            return new AcrolinxMatchWithReplacement(range, new IntRange(minRange, maxRange), getContent(),
                    getReplacement());
        }
        return new AcrolinxMatchWithReplacement(content, range, getReplacement());
    }
}
