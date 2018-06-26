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
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
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
    public DitaBuilder(final URL editorLocation, final EntityResolver entityResolver)
            throws URISyntaxException, ParserConfigurationException, SAXException, IOException
    {
        this.editorLocation = editorLocation;
        parseXml(entityResolver);
    }

    private void parseXml(final EntityResolver entityResolver)
            throws ParserConfigurationException, IOException, SAXException, URISyntaxException
    {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        dbf.setNamespaceAware(true);
        dbf.setFeature("http://xml.org/sax/features/namespaces", false);
        dbf.setFeature("http://xml.org/sax/features/validation", false);
        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

        this.documentBuilder = dbf.newDocumentBuilder();
        documentBuilder.setEntityResolver(entityResolver);
        final Path path = Paths.get(editorLocation.toURI());
        if (!Files.isRegularFile(path)) {
            logger.error("Ditamap file " + editorLocation.toURI() + " does not exist.");
            return;
        }
        documentBuilder.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(final SAXParseException exception) throws SAXException
            {
                logger.warn("Warning: " + exception.getMessage());
            }

            @Override
            public void error(final SAXParseException exception) throws SAXException
            {
                logger.error("Error occurred while parsing document: " + exception.getMessage());
            }

            @Override
            public void fatalError(final SAXParseException exception) throws SAXException
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
        final NodeList topicrefs = this.document.getDocumentElement().getElementsByTagName("topicref");
        final int length = topicrefs.getLength();
        logger.debug("Found " + length + " topicrefs");
        for (int i = 0; i < length; i++) {
            final Node node = topicrefs.item(i);
            insertTopicRef(node);
        }
        return getDocumentAsString();
    }

    private void insertTopicRef(final Node ref) throws SAXException, IOException, URISyntaxException
    {
        if (this.document == null) {
            logger.error("No document found to insert topic refs.");
            return;
        }
        final Element item = (Element) ref;
        final String href = item.getAttribute("href");
        logger.debug("Start to insert topicref " + href);
        final Node hrefContentAsNode = getHrefContentAsNode(href);
        if (hrefContentAsNode != null) {
            final Node importNode = document.importNode(hrefContentAsNode, true);
            final Element topicresolved = document.createElement("topicrefcontent");
            topicresolved.appendChild(importNode);
            item.appendChild(topicresolved);
        }
    }

    private Node getHrefContentAsNode(final String href) throws URISyntaxException, IOException, SAXException
    {
        if (("" + href).startsWith("http://") || ("" + href).startsWith("https://")) {
            logger.warn("Not following href to '" + href + "'");
            return null;
        }

        Node fragmentNode = null;
        logger.debug("Creating content node ... ");
        final Path path = Paths.get(editorLocation.toURI());
        final Path parent = path.getParent();
        if (parent != null) {
            logger.debug("Parent path: " + parent.toString());

            final Path resolvedPath = parent.resolve(href);
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
        final StringWriter sw = new StringWriter();
        final TransformerFactory tf = TransformerFactory.newInstance();
        final Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        final DocumentType doctype = document.getDoctype();
        if (doctype != null) {
            final String systemId = doctype.getSystemId();
            final String publicId = doctype.getPublicId();
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, publicId);
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, systemId);
        }
        transformer.transform(new DOMSource(document), new StreamResult(sw));
        final String s = sw.toString();
        logger.debug("Transform resolved ditamap finished.");
        return s;
    }
}
