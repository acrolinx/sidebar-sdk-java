/*
 * Copyright (c) 2016-2017 Acrolinx GmbH
 */

package com.acrolinx.sidebar.pojo.document;

@SuppressWarnings({"MismatchedReadAndWriteOfArray", "unused"})
public class AcrolinxMatchFromJSON
{
    private String content;
    private int[] extractedRange;
    private int[] range;
    private String replacement;

    public AcrolinxMatchFromJSON()
    {
        //
    }

    public AcrolinxMatch getAsAcrolinxMatch()
    {
        if (extractedRange != null) {
            return new AcrolinxMatch(new IntRange(range[0], range[1]),
                    new IntRange(extractedRange[0], extractedRange[1]), content);
        }
        return new AcrolinxMatch(new IntRange(range[0], range[1]), content);
    }

    public AcrolinxMatchWithReplacement getAsAcrolinxMatchWithReplacement()
    {
        if (replacement != null && extractedRange != null) {
            return new AcrolinxMatchWithReplacement(new IntRange(range[0], range[1]),
                    new IntRange(extractedRange[0], extractedRange[1]), content, replacement);
        } else if (replacement != null) {
            return new AcrolinxMatchWithReplacement(content, new IntRange(range[0], range[1]), replacement);
        }
        return null;
    }

}
