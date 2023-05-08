/*
 * Copyright (c) 2020-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class FragmentContentHandler extends DefaultHandler
{
    private static String markerXpath;
    private static List<String> documentXpaths = new ArrayList<>();

    private String xPath = "/";
    private final XMLReader xmlReader;
    private FragmentContentHandler parent;
    private final Map<String, Integer> elementNameCount = new HashMap<>();

    public FragmentContentHandler(XMLReader xmlReader)
    {
        this.xmlReader = xmlReader;
    }

    private FragmentContentHandler(String xPath, XMLReader xmlReader, FragmentContentHandler parent)
    {
        this(xmlReader);
        this.xPath = xPath;
        this.parent = parent;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts)
            throws FragmentContentException
    {
        Integer count = elementNameCount.get(qName);
        if (null == count) {
            count = 1;
        } else {
            count++;
        }
        elementNameCount.put(qName, count);
        String childXPath = xPath + "/" + qName + "[" + count + "]";

        if (xPath.equals("/")) {
            documentXpaths.clear();
        }

        documentXpaths.add(childXPath);

        if (childXPath.contains("acroseparator")) {
            markerXpath = childXPath;
            throw new FragmentContentException("Stop parser - xpath is prepared");
        }

        FragmentContentHandler child = new FragmentContentHandler(childXPath, xmlReader, this);
        xmlReader.setContentHandler(child);
    }

    @Override
    public void endElement(String uri, String localName, String qName)
    {
        xmlReader.setContentHandler(parent);
    }

    @Override
    public void characters(char[] ch, int start, int length)
    {
        // nothing to do here
    }

    public String getMarkerXpath()
    {
        return markerXpath;
    }

    public List<String> getDocumentXpaths()
    {
        return documentXpaths;
    }
}
