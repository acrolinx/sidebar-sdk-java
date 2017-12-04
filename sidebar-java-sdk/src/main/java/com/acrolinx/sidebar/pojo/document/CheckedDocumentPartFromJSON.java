/*
 * Copyright (c) 2016-2017 Acrolinx GmbH
 */

package com.acrolinx.sidebar.pojo.document;

@SuppressWarnings({"WeakerAccess", "MismatchedReadAndWriteOfArray", "unused"})
class CheckedDocumentPartFromJSON
{
    private String checkId;
    private int[] range;

    public CheckedDocumentPartFromJSON()
    {
        //
    }

    public CheckedDocumentPart getAsCheckResult()
    {
        return new CheckedDocumentPart(checkId, new IntRange(range[0], range[1]));
    }
}
