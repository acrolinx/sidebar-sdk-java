/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.settings;

import com.acrolinx.sidebar.pojo.document.IntRange;
import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContent;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CheckOptionsTest
{
    @Test
    void testToStringHasTheProperKeys()
    {
        CheckOptions checkOptions = getCheckOptions();

        Assertions.assertEquals(
                "{\"inputFormat\":\"AUTO\",\"requestDescription\":{\"documentReference\":\"foo\"},\"selection\":{\"ranges\":[[0,0]]},\"externalContent\":{}}",
                checkOptions.toString());
    }

    @Test
    void testToStringFailsIfNoValidKeysProvided()
    {
        CheckOptions checkOptions = getCheckOptions();

        Assertions.assertNotEquals(
                "{\"inputFormat\":\"AUTO\",\"requestDescription\":{\"documentReference\":\"foo\"},\"documentSelection\":{\"ranges\":[[0,0]]},\"externalContent\":{}}",
                checkOptions.toString());
        Assertions.assertNotEquals(
                "{\"inputFormat\":\"AUTO\",\"requestDescription\":{\"reference\":\"foo\"},\"selection\":{\"ranges\":[[0,0]]},\"externalContent\":{}}",
                checkOptions.toString());
        Assertions.assertNotEquals(
                "{\"format\":\"AUTO\",\"requestDescription\":{\"documentReference\":\"foo\"},\"selection\":{\"ranges\":[[0,0]]},\"externalContent\":{}}",
                checkOptions.toString());
        Assertions.assertNotEquals(
                "{\"inputFormat\":\"AUTO\",\"description\":{\"documentReference\":\"foo\"},\"selection\":{\"ranges\":[[0,0]]},\"externalContent\":{}}",
                checkOptions.toString());
        Assertions.assertNotEquals(
                "{\"inputFormat\":\"AUTO\",\"requestDescription\":{\"documentReference\":\"foo\"},\"documentSelection\":{\"ranges\":[[0,0]]},\"content\":{}}",
                checkOptions.toString());
    }

    private static CheckOptions getCheckOptions()
    {
        RequestDescription requestDescription = new RequestDescription("foo");

        DocumentSelection documentSelection = new DocumentSelection(Collections.singletonList(new IntRange(0, 0)));

        ExternalContent externalContent = new ExternalContent(null, null, null, null);

        return new CheckOptions(requestDescription, InputFormat.AUTO, documentSelection, externalContent);
    }
}
