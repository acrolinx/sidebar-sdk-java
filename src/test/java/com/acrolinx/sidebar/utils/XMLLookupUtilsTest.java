/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.acrolinx.sidebar.pojo.document.IntRange;
import java.util.List;
import org.junit.jupiter.api.Test;

class XMLLookupUtilsTest {
  private static final String XML_CONTENT =
      "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
          + "<bookstore xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
          + "           xsi:noNamespaceSchemaLocation=\"BookStore.xsd\">\n"
          + "    <book price=\"730.54\" ISBN=\"string\" publicationdate=\"2016-02-27\">\n"
          + "        <title>Some hideous story about monsters</title>\n"
          + "        <author>\n"
          + "            <first-name>Max</first-name>\n"
          + "            <last-name>Muster</last-name>\n"
          + "        </author>\n"
          + "        <genre>Fantastic Fantasy</genre>\n"
          + "    </book>\n"
          + "    <book price=\"6738.774\" ISBN=\"string\">\n"
          + "        <title>Little munchkins on adventures</title>\n"
          + "        <author>\n"
          + "            <first-name>Susi</first-name>\n"
          + "            <last-name>Some Name</last-name>\n"
          + "        </author>\n"
          + "    </book>\n"
          + "</bookstore>\n";

  private static final String XML_CONTENT_WITH_NAMESPACES =
      "<proc:procedure xmlns:proc=\"urn:com.asml.itms.procedure/1.0\" xmlns:chg=\"urn:com.asml.itms.change/1.0\" xmlns:prf=\"urn:com.asml.itms.profile/1.0\" xmlns:ssp=\"urn:com.asml.itms.ssp/1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" documentname=\"eht115.rem\" family=\"TWINSCAN_NXE\" lang=\"en\" proctype=\"Remove\" status=\"Provisional\" subsystem=\"HandlingAndTransport\" xsi:schemaLocation=\"urn:com.asml.itms.procedure/1.0 procedure.xsd\"><RSUITE:METADATA xmlns:RSUITE=\"http://www.reallysi.com\"><RSUITE:SYSTEM><RSUITE:ID>31915903</RSUITE:ID><RSUITE:DISPLAYNAME>eht115.rem</RSUITE:DISPLAYNAME><RSUITE:USER>fneto</RSUITE:USER><RSUITE:CREATEDATE>2022-08-03T14:19:37.224Z</RSUITE:CREATEDATE><RSUITE:LASTMODIFIED>2023-07-28T09:45:49.686Z</RSUITE:LASTMODIFIED></RSUITE:SYSTEM><RSUITE:LAYERED><RSUITE:DATA RSUITE:ID=\"31915905\" RSUITE:NAME=\"Status\">Incomplete</RSUITE:DATA><RSUITE:DATA RSUITE:ID=\"31915980\" RSUITE:NAME=\"beingCreated\">false</RSUITE:DATA></RSUITE:LAYERED></RSUITE:METADATA><p>Some text that we nees to check fot errors.</p></proc:procedure>";

  public static final String XML_WITH_NAMESPACE_2 =
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
          + "<!--Arbortext, Inc., 1988-2021, v.4002-->\n"
          + "<!DOCTYPE task PUBLIC \"-//Scania//DTD -//WINGS DTD 1.00//EN\" \"wings.dtd\">\n"
          + "<task class=\"description\" original-language=\"sv-SE\" xml:lang=\"sv-SE\" xmlns:dctm=\"http://www.documentum.com\">\n"
          + "<metadata>\n"
          + "<prim_indexterm its:translate=\"yes\" xmlns:its=\"http://www.w3.org/TR/its/\"><p its:translate=\"yes\" xmlns:its=\"http://www.w3.org/TR/its/\"></p></prim_indexterm>\n"
          + "<keyword its:translate=\"yes\" xmlns:its=\"http://www.w3.org/TR/its/\"><term></term></keyword></metadata>\n"
          + "<description-title its:translate=\"yes\" xmlns:its=\"http://www.w3.org/TR/its/\">Visa oljenivån i kombinationsinstrumentet</description-title>\n"
          + "<shortdesc>\n"
          + "<p its:translate=\"yes\" xmlns:its=\"http://www.w3.org/TR/its/\">Den aktuella oljenivån visas i kombinationsinstrumentet efter att du slagit på spänningen. </p>\n"
          + "</shortdesc><?Pub Caret -1?>\n"
          + "<taskbody>\n"
          + "<xi:include href=\"x-wc://file=s0003944050.xml\" xmlns:xi=\"http://www.w3.org/2001/XInclude\">\n"
          + "<xi:fallback><!--Fallback markup for \"x-wc://file=s0003944050.xml\"--></xi:fallback>\n"
          + "</xi:include>\n"
          + "<p its:translate=\"yes\" xmlns:its=\"http://www.w3.org/TR/its/\">En korrekt visning av motorns oljenivå kan endast ske om all olja finns i oljesumpen. Därför är det lämpligt att avläsa oljenivån i kombinationsinstrumentet innan du startar motorn och kör iväg med fordonet.</p>\n"
          + "<stepsdrm type=\"ordered\">\n"
          + "<stepdrm>\n"
          + "<cmd><p its:translate=\"yes\" xmlns:its=\"http://www.w3.org/TR/its/\">Vrid startnyckeln till körläge.</p></cmd>\n"
          + "</stepdrm>\n"
          + "<stepdrm>\n"
          + "<cmd><p its:translate=\"yes\" xmlns:its=\"http://www.w3.org/TR/its/\">Tryck på OK-knappen och välj i menyn <i its:translate=\"yes\" xmlns:its=\"http://www.w3.org/TR/its/\"><ui-ref value=\"UI_ICL\">Inställning</ui-ref></i> > <i its:translate=\"yes\" xmlns:its=\"http://www.w3.org/TR/its/\"><ui-ref value=\"UI_ICL\">Startkontroll</ui-ref></i> > <i its:translate=\"yes\" xmlns:its=\"http://www.w3.org/TR/its/\"><ui-ref value=\"UI_ICL\">Visa oljenivå</ui-ref></i>.</p></cmd>\n"
          + "<info><p its:translate=\"yes\" xmlns:its=\"http://www.w3.org/TR/its/\">Oljenivån visas med en liggande stapel.</p>\n"
          + "<xi:include href=\"x-wc://file=s0004858393.xml\" xmlns:xi=\"http://www.w3.org/2001/XInclude\">\n"
          + "<xi:fallback><!--Fallback markup for \"x-wc://file=s0004858393.xml\"--></xi:fallback>\n"
          + "</xi:include>\n"
          + "</info>\n"
          + "</stepdrm>\n"
          + "</stepsdrm>\n"
          + "<!--JIRA DRM-1718-->\n"
          + "<p its:translate=\"yes\" xmlns:its=\"http://www.w3.org/TR/its/\">Om oljan behöver fyllas på visas det i kombinationsinstrumentet vilken mängd du ska fylla på med.</p>\n"
          + "<p its:translate=\"yes\" xmlns:its=\"http://www.w3.org/TR/its/\">Oljenivån visas så länge fordonet står stilla, men högst 30 minuter efter motorstart.</p>\n"
          + "</taskbody>\n"
          + "</task>";

  public static final String XML_PATH = "//proc:procedure[1]/p[1]";

  @Test
  void findOffsetInXmlStringByXpathWithNamespace() {
    IntRange offsetForXPATH =
        XMLLookupUtils.findOffsetForNodeInXmlStringByXpath(XML_CONTENT_WITH_NAMESPACES, XML_PATH);
    assertEquals(974, offsetForXPATH.getMinimumInteger());
    assertEquals(1024, offsetForXPATH.getMaximumInteger());
    assertEquals(
        "<p>Some text that we nees to check fot errors.</p>",
        XML_CONTENT_WITH_NAMESPACES.substring(974, 1024));
  }

  @Test
  void findOffsetInXmlStringByXpath() {
    IntRange offsetForXPATH =
        XMLLookupUtils.findOffsetForNodeInXmlStringByXpath(
            XML_CONTENT, "//bookstore[1]/book[1]/genre[1]");
    assertEquals(414, offsetForXPATH.getMinimumInteger());
    assertEquals(446, offsetForXPATH.getMaximumInteger());
    assertEquals("<genre>Fantastic Fantasy</genre>", XML_CONTENT.substring(414, 446));
  }

  @Test
  void findXpathByOffsetWithNamespace() throws Exception {
    String xpathByOffset = XMLLookupUtils.findXpathByOffset(XML_CONTENT_WITH_NAMESPACES, 995, 999);
    assertEquals(XML_PATH, xpathByOffset);
  }

  @Test
  void findXpathByOffset() throws Exception {
    int index = XML_CONTENT.indexOf("Fantastic");
    String xpathByOffset =
        XMLLookupUtils.findXpathByOffset(XML_CONTENT, index, index + "Fantastic".length());
    assertEquals("//bookstore[1]/book[1]/genre[1]", xpathByOffset);

    index = XML_CONTENT.indexOf("Some Name");
    xpathByOffset =
        XMLLookupUtils.findXpathByOffset(XML_CONTENT, index, index + "Some Name".length());
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
    final List<String> allXpathInXmlDocument = XMLLookupUtils.getAllXpathInXmlDocument(XML_CONTENT);
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
