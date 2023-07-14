/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.acrolinx.sidebar.pojo.document.IntRange;

public final class XMLLookupUtils
{
    private static final Logger logger = LoggerFactory.getLogger(XMLLookupUtils.class);

    public static IntRange findOffsetForNodeInXmlStringByXpath(String xmlContent, String xpath)
    {
        int startOffset = 0;
        int endOffset = 0;

        try {
            Document doc = buildDocument(xmlContent);

            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xPath.compile(xpath).evaluate(doc, XPathConstants.NODESET);

            if (nodeList.getLength() == 0) {
                throw new IllegalStateException("Xpath evaluation returned 0 nodes");
            }

            String selectionTag = "acrolinxnode";
            String nodeName = null;
            boolean nodeHasAttributes = false;

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                final Element acrolinxSelection = node.getOwnerDocument().createElement(selectionTag);
                final Node parentNode = node.getParentNode();

                if (parentNode != null) {
                    if (parentNode.isSameNode(doc)) {
                        String rootElement = doc.getDocumentElement().getTagName();
                        String rootElementBegin = "<" + rootElement + ">";
                        String rootElementEnd = "</" + rootElement + ">";
                        return new IntRange(xmlContent.indexOf(rootElementBegin),
                                xmlContent.lastIndexOf(rootElementEnd) + rootElementEnd.length());
                    }
                    nodeName = node.getNodeName();
                    nodeHasAttributes = node.hasAttributes();
                    parentNode.insertBefore(acrolinxSelection, node);
                    acrolinxSelection.appendChild(node);

                }
            }

            String documentXML = getDocumentXml(doc);
            String startTag = "<" + selectionTag + ">";
            String endTag = "</" + selectionTag + ">";
            startOffset = documentXML.indexOf(startTag);
            endOffset = documentXML.indexOf(endTag) - startTag.length();
            documentXML = documentXML.replace(startTag, "").replace(endTag, "");

            String startTagInXML = "<" + nodeName + (nodeHasAttributes ? " " : ">");
            final int occurrencesOfStartTag = StringUtils.countMatches(documentXML.substring(0, startOffset),
                    startTagInXML);

            String endTagInXML = "</" + nodeName + ">";
            final int occurrencesOfEndTag = StringUtils.countMatches(documentXML.substring(0, endOffset), endTagInXML)
                    - 1;

            startOffset = StringUtils.ordinalIndexOf(xmlContent, startTagInXML, occurrencesOfStartTag + 1);
            endOffset = StringUtils.ordinalIndexOf(xmlContent, endTagInXML, occurrencesOfEndTag + 1)
                    + endTagInXML.length();

        } catch (XPathExpressionException | ParserConfigurationException | IOException | SAXException e) {
            logger.error("Unable to find offsets in XMl for xpath: {}", xpath, e);
        } catch (IllegalStateException e) {
            logger.error("", e);
        }

        if (startOffset < 0 || endOffset < 0) {
            return new IntRange(0, 0);
        }
        return new IntRange(startOffset, endOffset);
    }

    private static XMLReader getSecureXMLReader() throws ParserConfigurationException, SAXException
    {
        SAXParserFactory saxParserFactory = javax.xml.parsers.SAXParserFactory.newInstance();
        SAXParser saxParser = saxParserFactory.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();
        try {
            xmlReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            xmlReader.setFeature("http://xml.org/sax/features/external-general-entities", false);
            xmlReader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        } catch (SAXException e) {
            logger.error("Some XXE preventing settings are not supported by the current XML Reader library.", e);
        }
        return xmlReader;
    }

    private static String getDocumentXml(Document document)
    {
        TransformerFactory transformerFactory = javax.xml.transform.TransformerFactory.newInstance();
        Transformer transformer;
        try {
            logger.debug("Applying transformation to XML.");
            transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "no");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            DocumentType doctype = document.getDoctype();
            if (doctype != null) {
                transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
                transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
            }

            StringWriter stringWriter = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
            return stringWriter.getBuffer().toString();
        } catch (TransformerException e) {
            logger.debug("Creating XML string from document failed.");
        }
        return "";
    }

    private static Document buildDocument(String xmlContent)
            throws ParserConfigurationException, IOException, SAXException
    {
        DocumentBuilderFactory documentBuilderFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        documentBuilder.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));

        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
            return documentBuilder.parse(inputStream);
        } catch (Exception e) {
            final String cleanXML = XMLLookupUtils.cleanXML(xmlContent);
            ByteArrayInputStream input = new ByteArrayInputStream(cleanXML.getBytes(StandardCharsets.UTF_8));
            return documentBuilder.parse(input);
        }
    }

    public static String cleanXML(String markup)
    {
        final org.jsoup.nodes.Document document = Jsoup.parse(markup, "", Parser.xmlParser());
        document.outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml);

        return document.toString();
    }

    public static String findXpathByOffset(String xmlContent, int offsetStart, int offsetEnd) throws Exception
    {
        String contentWithMarkerNode = xmlContent.substring(0, offsetStart) + "<acroseparator>"
                + xmlContent.substring(offsetStart, offsetEnd) + "</acroseparator>" + xmlContent.substring(offsetEnd);

        XMLReader xmlReader = XMLLookupUtils.getSecureXMLReader();
        boolean isMalformedXmlFound = false;
        FragmentContentHandler fragmentContentHandler = new FragmentContentHandler(xmlReader);
        xmlReader.setContentHandler(fragmentContentHandler);
        try {
            xmlReader.parse(new InputSource(new StringReader(contentWithMarkerNode)));
        } catch (FragmentContentException s) {
            logger.debug("Custom fragment content exception is received");
        } catch (Exception e) {
            isMalformedXmlFound = true;
        }
        if (isMalformedXmlFound) {
            try {
                xmlReader.parse(new InputSource(new StringReader(XMLLookupUtils.cleanXML(contentWithMarkerNode))));
            } catch (FragmentContentException s) {
                logger.debug("Custom fragment content exception is received");
            }
        }
        String markerXpath = fragmentContentHandler.getMarkerXpath();
        return markerXpath.substring(0, markerXpath.lastIndexOf('/'));
    }

    public static List<String> getAllXpathInXmlDocument(String xml) throws Exception
    {
        XMLReader xmlReader = getSecureXMLReader();
        FragmentContentHandler fragmentContentHandler = new FragmentContentHandler(xmlReader);
        xmlReader.setContentHandler(fragmentContentHandler);
        xmlReader.parse(new InputSource(new StringReader(xml)));
        return fragmentContentHandler.getDocumentXpaths();
    }

    public static String getCommonXpath(String xpath1, String xpath2)
    {
        String[] xpath1Elements = xpath1.split("/");
        String[] xpath2Elements = xpath2.split("/");

        int shortXpathCount = xpath1Elements.length;
        if (xpath2Elements.length < xpath1Elements.length) {
            shortXpathCount = xpath2Elements.length;
        }

        for (int i = 0; i < shortXpathCount; i++) {
            if (!xpath1Elements[i].equals(xpath2Elements[i])) {
                return String.join("/", Arrays.copyOfRange(xpath1Elements, 0, i));
            }
        }

        return String.join("/", Arrays.copyOfRange(xpath1Elements, 0, shortXpathCount));
    }

    private XMLLookupUtils()
    {
        throw new IllegalStateException();
    }
}
