/**
 * Copyright (c) 2012-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.acrolinx.sidebar.pojo.document.IntRange;

public class XMLLookupUtilsTest
{
    private static String XML_CONTENT = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
            + "<bookstore xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
            + "           xsi:noNamespaceSchemaLocation=\"BookStore.xsd\">\n"
            + "    <book price=\"730.54\" ISBN=\"string\" publicationdate=\"2016-02-27\">\n"
            + "        <title>Some hideous story about monsters</title>\n" + "        <author>\n"
            + "            <first-name>Max</first-name>\n" + "            <last-name>Muster</last-name>\n"
            + "        </author>\n" + "        <genre>Fantastic Fantasy</genre>\n" + "    </book>\n"
            + "    <book price=\"6738.774\" ISBN=\"string\">\n"
            + "        <title>Little munchkins on adventures</title>\n" + "        <author>\n"
            + "            <first-name>Susi</first-name>\n" + "            <last-name>Some Name</last-name>\n"
            + "        </author>\n" + "    </book>\n" + "</bookstore>\n";

    @Test
    public void findOffsetInXmlStringByXpath()
    {
        IntRange offsetForXPATH = XMLLookupUtils.findOffsetForNodeInXmlStringByXpath(XML_CONTENT,
                "//bookstore[1]/book[1]/genre[1]");
        assertEquals(414, offsetForXPATH.getMinimumInteger());
        assertEquals(446, offsetForXPATH.getMaximumInteger());
        assertEquals("<genre>Fantastic Fantasy</genre>", XML_CONTENT.substring(414, 446));
    }

    @Test
    public void findXpathByOffset() throws Exception
    {
        int index = XML_CONTENT.indexOf("Fantastic");
        String xpathByOffset = XMLLookupUtils.findXpathByOffset(XML_CONTENT, index);
        assertEquals("//bookstore[1]/book[1]/genre[1]", xpathByOffset);

        index = XML_CONTENT.indexOf("Some Name");
        xpathByOffset = XMLLookupUtils.findXpathByOffset(XML_CONTENT, index);
        assertEquals("//bookstore[1]/book[2]/author[1]/last-name[1]", xpathByOffset);
    }

    @Test
    public void testCommonXpath()
    {
        assertEquals("//para[1]/p[2]/sub[1]",
                XMLLookupUtils.getCommonXpath("//para[1]/p[2]/sub[1]", "//para[1]/p[2]/sub[1]"));
        assertEquals("//para[1]/p[2]", XMLLookupUtils.getCommonXpath("//para[1]/p[2]", "//para[1]/p[2]/sub[1]"));
        assertEquals("//para[1]/p[2]", XMLLookupUtils.getCommonXpath("//para[1]/p[2]/sub[1]", "//para[1]/p[2]"));
        assertEquals("//para[1]/p[2]",
                XMLLookupUtils.getCommonXpath("//para[1]/p[2]/sub[1]/", "//para[1]/p[2]/sub[2]"));
    }
}
