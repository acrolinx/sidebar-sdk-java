/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.pojo.document;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CheckedDocumentPartTest
{
    @Test
    public void getAsJS() throws Exception
    {
        CheckedDocumentPart part = new CheckedDocumentPart("id0", new IntRange(2, 3));
        assertEquals("{checkId: \"id0\", range:[2,3]}", part.getAsJS());
    }
}