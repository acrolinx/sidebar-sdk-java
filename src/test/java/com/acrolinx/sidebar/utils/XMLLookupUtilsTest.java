/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.acrolinx.sidebar.pojo.document.IntRange;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;

class XMLLookupUtilsTest {
  private static String readFileContent(String fileName) throws IOException {
    return Files.readString(Path.of("src/test/resources/xml/", fileName));
  }

  private static void verifyIntRange(IntRange intRange, int startOffset, int endOffset) {
    assertEquals(startOffset, intRange.getMinimumInteger());
    assertEquals(endOffset, intRange.getMaximumInteger());
  }

  static {
    System.setProperty("line.separator", "\n");
  }

  @Test
  void findOffsetInXmlStringByXpathWithNamespaceAndEmptyTagAndComment() throws IOException {
    final String xPathString = "//proc:procedure[1]/p[4]";
    final String xmlContent = readFileContent("xml-with-namespace-empty-tag-and-comment.xml");

    IntRange offsetForXPATH =
        XMLLookupUtils.findOffsetForNodeInXmlStringByXpath(xmlContent, xPathString);

    final int expectedStartOffset = 251;
    final int expectedEndOffset = 275;
    verifyIntRange(offsetForXPATH, expectedStartOffset, expectedEndOffset);
    assertEquals(
        "<p>That's how it is!</p>", xmlContent.substring(expectedStartOffset, expectedEndOffset));
  }

  @Test
  void findOffsetInXmlStringByXpathWithNamespaceAndEmptyTag() throws IOException {
    final String xPathString = "//proc:procedure[1]/p[3]";
    final String xmlContent = readFileContent("xml-with-namespace-and-empty-tag.xml");

    IntRange offsetForXPATH =
        XMLLookupUtils.findOffsetForNodeInXmlStringByXpath(xmlContent, xPathString);

    final int expectedStartOffset = 176;
    final int expectedEndOffset = 215;
    verifyIntRange(offsetForXPATH, expectedStartOffset, expectedEndOffset);
    assertEquals(
        "<p>This should be processed as well</p>",
        xmlContent.substring(expectedStartOffset, expectedEndOffset));
  }

  @Test
  void findOffsetInXmlStringByXpathWithNamespace() throws IOException {
    final String xPathString = "//proc:procedure[1]/p[1]";
    final String xmlContent = readFileContent("xml-with-namespace.xml");

    IntRange offsetForXPATH =
        XMLLookupUtils.findOffsetForNodeInXmlStringByXpath(xmlContent, xPathString);

    final int expectedStartOffset = 109;
    final int expectedEndOffset = 159;
    verifyIntRange(offsetForXPATH, expectedStartOffset, expectedEndOffset);
    assertEquals(
        "<p>Some text that we nees to check fot errors.</p>",
        xmlContent.substring(expectedStartOffset, expectedEndOffset));
  }

  @Test
  void findOffsetInXmlStringByXpath() throws IOException {
    final String xmlContent = readFileContent("xml-with-no-namespace.xml");
    IntRange offsetForXPATH =
        XMLLookupUtils.findOffsetForNodeInXmlStringByXpath(
            xmlContent, "//bookstore[1]/book[1]/genre[1]");

    final int expectedStartOffset = 414;
    final int expectedEndOffset = 446;
    verifyIntRange(offsetForXPATH, expectedStartOffset, expectedEndOffset);
    assertEquals(
        "<genre>Fantastic Fantasy</genre>",
        xmlContent.substring(expectedStartOffset, expectedEndOffset));
  }

  @Test
  void findOffsetInXmlStringWithDefaultNamespaceByXpath() throws IOException {
    final String xmlContent = readFileContent("xml-with-default-namespace.xml");
    IntRange offsetForXPATH =
        XMLLookupUtils.findOffsetForNodeInXmlStringByXpath(
            xmlContent, "//html[1]/head[1]/title[1]");

    final int expectedStartOffset = 102;
    final int expectedEndOffset = 129;
    verifyIntRange(offsetForXPATH, expectedStartOffset, expectedEndOffset);
    assertEquals(
        "<title>A good title</title>",
        xmlContent.substring(expectedStartOffset, expectedEndOffset));
  }

  @Test
  void findXpathByOffsetWithNamespace() throws Exception {
    final String xPathString = "//proc:procedure[1]/p[1]";
    final String xmlContent = readFileContent("xml-with-namespace.xml");

    String xpathByOffset = XMLLookupUtils.findXpathByOffset(xmlContent, 130, 134);
    assertEquals(xPathString, xpathByOffset);
  }

  @Test
  void findXpathByOffsetForXmlWithDefaultNamespace() throws Exception {
    final String xPathString = "//html[1]/head[1]/title[1]";
    final String xmlContent = readFileContent("xml-with-default-namespace.xml");

    String xpathByOffset = XMLLookupUtils.findXpathByOffset(xmlContent, 111, 115);
    assertEquals(xPathString, xpathByOffset);
  }

  @Test
  void findXpathByOffset() throws Exception {
    final String xmlContent = readFileContent("xml-with-no-namespace.xml");
    int index = xmlContent.indexOf("Fantastic");
    String xpathByOffset =
        XMLLookupUtils.findXpathByOffset(xmlContent, index, index + "Fantastic".length());
    assertEquals("//bookstore[1]/book[1]/genre[1]", xpathByOffset);

    index = xmlContent.indexOf("Some Name");
    xpathByOffset =
        XMLLookupUtils.findXpathByOffset(xmlContent, index, index + "Some Name".length());
    assertEquals("//bookstore[1]/book[2]/author[1]/last-name[1]", xpathByOffset);
  }

  @Test
  void testCommonXpath() {
    assertEquals(
        "//para[1]/p[2]/sub[1]",
        XMLLookupUtils.getCommonXpath("//para[1]/p[2]/sub[1]", "//para[1]/p[2]/sub[1]"));
    assertEquals(
        "//para[1]/p[2]", XMLLookupUtils.getCommonXpath("//para[1]/p[2]", "//para[1]/p[2]/sub[1]"));
    assertEquals(
        "//para[1]/p[2]", XMLLookupUtils.getCommonXpath("//para[1]/p[2]/sub[1]", "//para[1]/p[2]"));
    assertEquals(
        "//para[1]/p[2]",
        XMLLookupUtils.getCommonXpath("//para[1]/p[2]/sub[1]/", "//para[1]/p[2]/sub[2]"));
  }

  @Test
  void testXHTMLContent() throws Exception {
    String XHtmlContent =
        "<!--Arbortext, Inc., 1988-2019, v.4002-->\n"
            + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n"
            + " \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
            + "<head>\n"
            + "<title>TEST_CONTAINER</title><?Pub Caret -1>\n"
            + "<meta content=\"text/html; charset=iso-8859-1\" http-equiv=\"content-type\">\n"
            + "</head>\n"
            + "<body>\t\t\t \t\t\t<div>\t\t\t\t<img\n"
            + "alt=\"notice that all my quotes are in\n"
            + " place for attribute values\" src=\"logoBlackBlue.png\"> \t\t\t\t</div> \t\t\t</body>\n"
            + "</html>";
    int index = XHtmlContent.indexOf("TEST_CONTAINER");
    String xpathByOffset =
        XMLLookupUtils.findXpathByOffset(XHtmlContent, index, index + "TEST_CONTAINER".length());
    assertEquals("//html[1]/head[1]/title[1]", xpathByOffset);
  }

  @Test
  void testGetAllXpathsFromDocument() throws Exception {
    final String xmlContent = readFileContent("xml-with-no-namespace.xml");
    final List<String> allXpathInXmlDocument = XMLLookupUtils.getAllXpathInXmlDocument(xmlContent);
    assertEquals(12, allXpathInXmlDocument.size());
  }

  @Test
  void testCleanXML() {
    String xhtmlContent =
        "<!--Arbortext, Inc., 1988-2019, v.4002-->\n"
            + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n"
            + " \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n"
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
            + "<head>\n"
            + "<title>TEST_CONTAINER</title><?Pub Caret -1>\n"
            + "<meta content=\"text/html; charset=iso-8859-1\" http-equiv=\"content-type\">\n"
            + "</head>\n"
            + "<body>\t\t\t \t\t\t<div>\t\t\t\t<img\n"
            + "alt=\"notice that all my quotes are in\n"
            + " place for attribute values\" src=\"logoBlackBlue.png\"> \t\t\t\t</div> \t\t\t</body>\n"
            + "</html>";
    final String cleanXml = XMLLookupUtils.cleanXML(xhtmlContent);
    assertTrue(cleanXml.contains("</meta>"));
  }

  @Test
  void testFindOffsetInXmlStringByXpathAdmonition() {
    String content =
        "<!DOCTYPE procedure PUBLIC \"-//Scania//DTD -//WINGS DTD 2.00//EN\" \"wings.dtd\"><procedure class=\"description\" original-language=\"sv-SE\" type=\"remove\" xml:lang=\"sv-SE\" xmlns:dctm=\"http://www.documentum.com\"><wsm-description-title its:translate=\"yes\" xmlns:its=\"http://www.w3.org/TR/its/\">9- och 13-litersmotor [XPI]</wsm-description-title><admonition class=\"admonition\" dctm:obj_id=\"09010d2e80b8d699\" dctm:obj_status=\"Read-Only\" dctm:version_label=\"CURRENT\" type=\"environment\" xml:lang=\"sv-SE\" xmlns:dctm=\"http://www.documentum.com\"><p its:translate=\"yes\" xmlns:its=\"http://www.w3.org/TR/its/\">firrst Tänk på miljön, undvik middle  spill och använd uppsamlingskärl lasst</p></admonition><admonition class=\"admonition\" dctm:obj_id=\"09010d2e80b8eb67\" dctm:obj_status=\"Read-Only\" dctm:version_label=\"CURRENT\" type=\"note\" xml:lang=\"sv-SE\" xmlns:dctm=\"http://www.documentum.com\"><p its:translate=\"yes\" xmlns:its=\"http://www.w3.org/TR/its/\">onee Använd hjullyftar för att förenkla twoo borttagningen fourr och ditsättningen av motorn threee</p></admonition></procedure>";
    IntRange offsetForXPATH =
        XMLLookupUtils.findOffsetForNodeInXmlStringByXpath(
            content, "//procedure[1]/admonition[1]/p[1]");
    assertEquals(531, offsetForXPATH.getMinimumInteger());
    assertEquals(672, offsetForXPATH.getMaximumInteger());
    assertEquals(
        "<p its:translate=\"yes\" xmlns:its=\"http://www.w3.org/TR/its/\">firrst Tänk på miljön, undvik middle  spill och använd uppsamlingskärl lasst</p>",
        content.substring(offsetForXPATH.getMinimumInteger(), offsetForXPATH.getMaximumInteger()));
  }

  @Test
  void testFindOffsetInXmlStringByXpathAdmonition2() {
    String content =
        "<!DOCTYPE procedure PUBLIC \"-//Scania//DTD -//WINGS DTD 2.00//EN\" \"wings.dtd\"><procedure class=\"description\" original-language=\"sv-SE\" type=\"remove\" xml:lang=\"sv-SE\" xmlns:dctm=\"http://www.documentum.com\"><wsm-description-title its:translate=\"yes\" xmlns:its=\"http://www.w3.org/TR/its/\">9- och 13-litersmotor [XPI]</wsm-description-title><admonition class=\"admonition\" dctm:obj_id=\"09010d2e80b8d699\" dctm:obj_status=\"Read-Only\" dctm:version_label=\"CURRENT\" type=\"environment\" xml:lang=\"sv-SE\" xmlns:dctm=\"http://www.documentum.com\"><p its:translate=\"yes\" xmlns:its=\"http://www.w3.org/TR/its/\">firrst Tänk på miljön, undvik middle  spill och använd uppsamlingskärl lasst</p></admonition><admonition class=\"admonition\" dctm:obj_id=\"09010d2e80b8eb67\" dctm:obj_status=\"Read-Only\" dctm:version_label=\"CURRENT\" type=\"note\" xml:lang=\"sv-SE\" xmlns:dctm=\"http://www.documentum.com\"><p its:translate=\"yes\" xmlns:its=\"http://www.w3.org/TR/its/\">onee Använd hjullyftar för att förenkla twoo borttagningen fourr och ditsättningen av motorn threee</p></admonition></procedure>";
    IntRange offsetForXPATH =
        XMLLookupUtils.findOffsetForNodeInXmlStringByXpath(
            content, "//procedure[1]/admonition[2]/p[1]");
    assertEquals(872, offsetForXPATH.getMinimumInteger());
    assertEquals(1036, offsetForXPATH.getMaximumInteger());
    assertEquals(
        "<p its:translate=\"yes\" xmlns:its=\"http://www.w3.org/TR/its/\">onee Använd hjullyftar för att förenkla twoo borttagningen fourr och ditsättningen av motorn threee</p>",
        content.substring(offsetForXPATH.getMinimumInteger(), offsetForXPATH.getMaximumInteger()));
  }

  @Test
  void testFindOffsetInXmlStringByXpathSimple() {
    String content = "<x><y>Thiss iss tesst.</y></x>";
    IntRange offsetForXPATH =
        XMLLookupUtils.findOffsetForNodeInXmlStringByXpath(content, "//x[1]/y[1]");
    assertEquals(
        "<y>Thiss iss tesst.</y>",
        content.substring(offsetForXPATH.getMinimumInteger(), offsetForXPATH.getMaximumInteger()));
  }

  @Test
  void testFindOffsetInXmlStringByXpathRootElement() {
    String content = "<x>The root element test</x>";
    IntRange offsetForXPATH = XMLLookupUtils.findOffsetForNodeInXmlStringByXpath(content, "//x[1]");
    assertEquals(
        content,
        content.substring(offsetForXPATH.getMinimumInteger(), offsetForXPATH.getMaximumInteger()));
  }

  @Test
  void testFindOffsetInXmlStringByXpathRepeatedElements() {
    String content = "<x>Root<x>Child1<x>Child2<x>Child3</x></x></x></x>";
    IntRange offsetForXPATH =
        XMLLookupUtils.findOffsetForNodeInXmlStringByXpath(content, "//x[1]/x[1]/x[1]/x[1]");
    assertEquals(
        "<x>Child3</x>",
        content.substring(offsetForXPATH.getMinimumInteger(), offsetForXPATH.getMaximumInteger()));
  }

  @Test
  void testFindOffsetInXmlStringByXpathSiblingInRepeatedElements() {
    String content = "<x>Root<x>Child1<x>Child2<x>Child3</x></x></x><x>Sibling</x></x>";
    IntRange offsetForXPATH =
        XMLLookupUtils.findOffsetForNodeInXmlStringByXpath(content, "//x[1]/x[2]");
    assertEquals(
        "<x>Sibling</x>",
        content.substring(offsetForXPATH.getMinimumInteger(), offsetForXPATH.getMaximumInteger()));
  }

  @Test
  void testFindOffsetInXmlStringByXpathAdditionalSpacesInAttributes() {
    String content =
        "<x>Root<x>Child1<x>Child2<x>Child3</x></x></x><x  a=\"abc\"   b=\"pqr\">Sibling</x></x>";
    IntRange offsetForXPATH =
        XMLLookupUtils.findOffsetForNodeInXmlStringByXpath(content, "//x[1]/x[2]");
    assertEquals(
        "<x  a=\"abc\"   b=\"pqr\">Sibling</x>",
        content.substring(offsetForXPATH.getMinimumInteger(), offsetForXPATH.getMaximumInteger()));
  }
}
