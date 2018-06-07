/*
 * Copyright (c) 2016-2017 Acrolinx GmbH
 */

package com.acrolinx.sidebar.dita;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Parse a ditamap and resolve all references.
 */
@SuppressWarnings("WeakerAccess")
public class DitaBuilder
{
    private Document document = null;
    private final URL editorLocation;
    private DocumentBuilder documentBuilder;

    final private Logger logger = LoggerFactory.getLogger(com.acrolinx.sidebar.dita.DitaBuilder.class);

    @SuppressWarnings("SameParameterValue")
    public DitaBuilder(URL editorLocation, EntityResolver entityResolver)
            throws URISyntaxException, ParserConfigurationException, SAXException, IOException
    {
        this.editorLocation = editorLocation;
        parseXml(entityResolver);
    }

    private void parseXml(EntityResolver entityResolver)
            throws ParserConfigurationException, IOException, SAXException, URISyntaxException
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        dbf.setNamespaceAware(true);
        dbf.setFeature("http://xml.org/sax/features/namespaces", false);
        dbf.setFeature("http://xml.org/sax/features/validation", false);
        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

        this.documentBuilder = dbf.newDocumentBuilder();
        documentBuilder.setEntityResolver(entityResolver);
        Path path = Paths.get(editorLocation.toURI());
        if (!Files.isRegularFile(path)) {
            logger.error("Ditamap file " + editorLocation.toURI() + " does not exist.");
            return;
        }
        documentBuilder.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(SAXParseException exception) throws SAXException
            {
                logger.warn("Warning: " + exception.getMessage());
            }

            @Override
            public void error(SAXParseException exception) throws SAXException
            {
                logger.error("Error occurred while parsing document: " + exception.getMessage());
            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException
            {
                logger.error("Fatal error occurred while parsing document: " + exception.getMessage());
            }
        });
        logger.debug("Parsing " + path.toString());
        this.document = documentBuilder.parse(path.toString());
        logger.debug("Parsed main document.");
    }

    /**
     * Parse ditamap and resolve all references.
     * 
     * @return ditamap as string with topic references resolved and inserted, to enable Acrolinx to
     *         check those.
     * @throws SAXException
     * @throws IOException
     * @throws URISyntaxException
     * @throws TransformerException
     */
    public String findAndResolveAllTopicRefs()
            throws SAXException, IOException, URISyntaxException, TransformerException
    {
        logger.debug("Start finding topicrefs!");
        if (this.document == null) {
            logger.error("No document found to insert topic refs.");
            return null;
        }
        NodeList topicrefs = this.document.getDocumentElement().getElementsByTagName("topicref");
        int length = topicrefs.getLength();
        logger.debug("Found " + length + " topicrefs");
        for (int i = 0; i < length; i++) {
            Node node = topicrefs.item(i);
            insertTopicRef(node);
        }
        return getDocumentAsString();
    }

    private void insertTopicRef(Node ref) throws SAXException, IOException, URISyntaxException
    {
        if (this.document == null) {
            logger.error("No document found to insert topic refs.");
            return;
        }
        Element item = (Element) ref;
        String href = item.getAttribute("href");
        logger.debug("Start to insert topicref " + href);
        Node hrefContentAsNode = getHrefContentAsNode(href);
        if (hrefContentAsNode != null) {
            Node importNode = document.importNode(hrefContentAsNode, true);
            Element topicresolved = document.createElement("topicrefcontent");
            topicresolved.appendChild(importNode);
            item.appendChild(topicresolved);
        }
    }

    private Node getHrefContentAsNode(String href) throws URISyntaxException, IOException, SAXException
    {
        Node fragmentNode = null;
        logger.debug("Creating content node ... ");
        Path path = Paths.get(editorLocation.toURI());
        Path parent = path.getParent();
        if (parent != null) {
            logger.debug("Parent path: " + parent.toString());
            Path resolvedPath = parent.resolve(href);
            logger.debug("Resolving: " + resolvedPath);
            if (Files.isRegularFile(resolvedPath)) {
                fragmentNode = this.documentBuilder.parse(resolvedPath.toString()).getDocumentElement();
            }
        }
        return fragmentNode;
    }

    private String getDocumentAsString() throws TransformerException
    {
        if (document == null) {
            logger.error("Can not transform document to string, as document is not defined or null.");
            return null;
        }
        StringWriter sw = new StringWriter();
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        DocumentType doctype = document.getDoctype();
        if (doctype != null) {
            String systemId = doctype.getSystemId();
            String publicId = doctype.getPublicId();
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, publicId);
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, systemId);
        }
        transformer.transform(new DOMSource(document), new StreamResult(sw));
        String s = sw.toString();
        logger.debug("Transform resolved ditamap finished.");
        return s;
    }
}
