/**
 * Copyright (c) 2012-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.utils;

import static org.junit.Assert.*;

import java.util.List;

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
        String xpathByOffset = XMLLookupUtils.findXpathByOffset(XML_CONTENT, index, index + "Fantastic".length());
        assertEquals("//bookstore[1]/book[1]/genre[1]", xpathByOffset);

        index = XML_CONTENT.indexOf("Some Name");
        xpathByOffset = XMLLookupUtils.findXpathByOffset(XML_CONTENT, index, index + "Some Name".length());
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

    @Test
    public void testXHTMLContent() throws Exception
    {

        String XHtmlContent = "<!--Arbortext, Inc., 1988-2019, v.4002-->\n"
                + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n"
                + " \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + "<head>\n"
                + "<title>TEST_CONTAINER</title><?Pub Caret -1>\n"
                + "<meta content=\"text/html; charset=iso-8859-1\" http-equiv=\"content-type\">\n" + "</head>\n"
                + "<body>\t\t\t \t\t\t<div>\t\t\t\t<img\n" + "alt=\"notice that all my quotes are in\n"
                + " place for attribute values\" src=\"logoBlackBlue.png\"> \t\t\t\t</div> \t\t\t</body>\n" + "</html>";
        int index = XHtmlContent.indexOf("TEST_CONTAINER");
        String xpathByOffset = XMLLookupUtils.findXpathByOffset(XHtmlContent, index, index + "TEST_CONTAINER".length());
        assertEquals("//html[1]/head[1]/title[1]", xpathByOffset);

    }

    @Test
    public void testGetAllXpathsFromDocument() throws Exception
    {
        final List<String> allXpathInXmlDocument = XMLLookupUtils.getAllXpathInXmlDocument(XML_CONTENT);
        assertEquals(12, allXpathInXmlDocument.size());
    }

    @Test
    public void testCleanXML()
    {
        String XHtmlContent = "<!--Arbortext, Inc., 1988-2019, v.4002-->\n"
                + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n"
                + " \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + "<head>\n"
                + "<title>TEST_CONTAINER</title><?Pub Caret -1>\n"
                + "<meta content=\"text/html; charset=iso-8859-1\" http-equiv=\"content-type\">\n" + "</head>\n"
                + "<body>\t\t\t \t\t\t<div>\t\t\t\t<img\n" + "alt=\"notice that all my quotes are in\n"
                + " place for attribute values\" src=\"logoBlackBlue.png\"> \t\t\t\t</div> \t\t\t</body>\n" + "</html>";
        final String cleanXML = XMLLookupUtils.cleanXML(XHtmlContent);
        assertTrue(cleanXML.contains("</meta>"));
    }
}
