/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class FragmentContentHandler extends DefaultHandler {
  private static List<String> documentXpaths = new ArrayList<>();
  private static String markerXpath;

  private static String createXPathForNodeInCustomNamespace(
      String nodeNameWithNamespacePrefix, int index) {
    return '/' + nodeNameWithNamespacePrefix + '[' + index + ']';
  }

  private static String createXPathForNodeInDefaultNamespace(String nodeName, int index) {
    return "/*[name()='" + nodeName + "'][" + index + ']';
  }

  private final Map<String, Integer> elementNameCount = new HashMap<>();
  private FragmentContentHandler parent;
  private String xPath = "";
  private final XMLReader xmlReader;

  public FragmentContentHandler(XMLReader xmlReader) {
    this.xmlReader = xmlReader;
  }

  private FragmentContentHandler(String xPath, XMLReader xmlReader, FragmentContentHandler parent) {
    this(xmlReader);
    this.xPath = xPath;
    this.parent = parent;
  }

  @Override
  public void characters(char[] ch, int start, int length) {
    // nothing to do here
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    xmlReader.setContentHandler(parent);
  }

  public List<String> getDocumentXpaths() {
    return documentXpaths;
  }

  public String getMarkerXpath() {
    return markerXpath;
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes)
      throws FragmentContentException {
    Integer count = elementNameCount.get(qName);

    if (null == count) {
      count = 1;
    } else {
      count++;
    }

    String childXPath = createChildXpath(localName, qName, count);
    elementNameCount.put(qName, count);

    if (xPath.isEmpty()) {
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

  private String createChildXpath(String localName, String qName, Integer count) {
    if (localName.equals(qName)) {
      return xPath + createXPathForNodeInDefaultNamespace(qName, count);
    }

    return xPath + createXPathForNodeInCustomNamespace(qName, count);
  }
}
