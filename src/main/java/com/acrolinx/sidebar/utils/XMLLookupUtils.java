/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.*;
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

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.acrolinx.sidebar.lookup.LookupRangesDiff;
import com.acrolinx.sidebar.pojo.document.AbstractMatch;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatch;
import com.acrolinx.sidebar.pojo.document.IntRange;

public class XMLLookupUtils
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

            String selectionTag = "acrolinxnode";

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
                    parentNode.insertBefore(acrolinxSelection, node);
                    acrolinxSelection.appendChild(node);

                }
            }

            String documentXML = getDocumentXML(doc);
            String startTag = "<" + selectionTag + ">";
            String endTag = "</" + selectionTag + ">";
            startOffset = documentXML.indexOf(startTag);
            endOffset = documentXML.indexOf(endTag) - startTag.length();
            documentXML = documentXML.replace(startTag, "").replace(endTag, "");

            final LookupRangesDiff lookupRangesDiff = new LookupRangesDiff();
            // Adding/Subtracting 2 is a precautionary measure.
            final AcrolinxMatch acrolinxMatchStart = new AcrolinxMatch(new IntRange(startOffset, startOffset + 2), "");
            final AcrolinxMatch acrolinxMatchEnd = new AcrolinxMatch(new IntRange(endOffset - 2, endOffset), "");

            final ArrayList<AcrolinxMatch> acrolinxMatches = new ArrayList<>();
            acrolinxMatches.add(acrolinxMatchStart);
            acrolinxMatches.add(acrolinxMatchEnd);

            final Optional<List<? extends AbstractMatch>> matchesWithCorrectedRanges = lookupRangesDiff.getMatchesWithCorrectedRanges(
                    documentXML, xmlContent, acrolinxMatches);

            if (matchesWithCorrectedRanges.isPresent()) {
                final List<? extends AbstractMatch> abstractMatches = matchesWithCorrectedRanges.get();
                if (abstractMatches.size() == 2) {
                    startOffset = abstractMatches.get(0).getRange().getMinimumInteger();
                    endOffset = abstractMatches.get(1).getRange().getMaximumInteger();
                }
            }

            logger.info("Check Selection Offsets: (" + startOffset + "," + endOffset + ")");

        } catch (XPathExpressionException | ParserConfigurationException | IOException | SAXException e) {
            logger.warn("Unable to find offsets in XMl for xpath : " + xpath);
            logger.warn(e.getMessage());
        }

        if (startOffset < 0 || endOffset < 0) {
            return new IntRange(0, 0);
        }

        return new IntRange(startOffset, endOffset);
    }

    private static String getDocumentXML(Document document)
    {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            logger.debug("Applying transformation to XML.");
            transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "no");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            DocumentType doctype = document.getDoctype();
            if (doctype != null) {
                transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
                transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
            }

            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            return writer.getBuffer().toString();
        } catch (TransformerException e) {
            logger.debug("Creating XML string from document failed.");
        }
        return "";
    }

    private static Document buildDocument(String xmlContent)
            throws ParserConfigurationException, IOException, SAXException
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));

        try {
            ByteArrayInputStream input = new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8));
            return builder.parse(input);
        } catch (Exception e) {
            final String cleanXML = XMLLookupUtils.cleanXML(xmlContent);
            ByteArrayInputStream input = new ByteArrayInputStream(cleanXML.getBytes(StandardCharsets.UTF_8));
            return builder.parse(input);
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

        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            xr.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            FragmentContentHandler fragmentContentHandler = new FragmentContentHandler(xr);
            xr.setContentHandler(fragmentContentHandler);
            try {
                xr.parse(new InputSource(new StringReader(contentWithMarkerNode)));
            } catch (Exception e) {
                xr.parse(new InputSource(new StringReader(XMLLookupUtils.cleanXML(contentWithMarkerNode))));
            }
            String markerXpath = fragmentContentHandler.getMarkerXpath();
            String xpath = markerXpath.substring(0, markerXpath.lastIndexOf('/'));
            return xpath;

        } catch (Exception ex) {
            logger.error("Lookup For Offset Failed");
            logger.error(ex.getMessage());
            throw new Exception(ex.getMessage());
        }
    }

    public static List<String> getAllXpathInXmlDocument(String xml) throws Exception
    {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            xr.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            FragmentContentHandler fragmentContentHandler = new FragmentContentHandler(xr);
            xr.setContentHandler(fragmentContentHandler);
            xr.parse(new InputSource(new StringReader(xml)));
            return fragmentContentHandler.getDocumentXpaths();
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new Exception(ex.getMessage());
        }
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
}
