/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.pojo.document;

import static org.junit.Assert.assertEquals;

import com.acrolinx.sidebar.pojo.document.externalContent.ExternalContentMatch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckedDocumentPartTest
{
    @Test
    public void getAsJS() throws Exception
    {
        CheckedDocumentPart part = new CheckedDocumentPart("id0", new IntRange(2, 3));
        assertEquals("{checkId: \"id0\", range:[2,3]}", part.getAsJS());

        ExternalContentMatch reference1 = new ExternalContentMatch("chapter11.dita", "ditaReferences", 75, 90);
        List<ExternalContentMatch> externalContentMatches = new ArrayList<>();
        externalContentMatches.add(new ExternalContentMatch("overview.dita", "ditaReferences", 25, 35,
                new ArrayList<ExternalContentMatch>(Arrays.asList(reference1))));

        CheckedDocumentPart partWithExternalContent = new CheckedDocumentPart("id0", new IntRange(2, 3), externalContentMatches);
        assertEquals("{checkId: \"id0\", range:[2,3], externalContent:[{" +
                "\"id\":\"overview.dita\"," +
                "\"type\":\"ditaReferences\",\"originalBegin\":25,\"originalEnd\":35,\"externalContentMatches\":[{\"id\":\"chapter11.dita\",\"type\":\"ditaReferences\",\"originalBegin\":75,\"originalEnd\":90,\"externalContentMatches\":[]}]}]}", partWithExternalContent.getAsJS());
    }
}