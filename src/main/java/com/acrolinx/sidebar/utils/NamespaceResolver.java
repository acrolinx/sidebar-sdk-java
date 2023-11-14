/* Copyright (c) 2023-present Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import java.util.Iterator;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import org.w3c.dom.Document;

public class NamespaceResolver implements NamespaceContext {
  private final Document sourceDocument;

  public NamespaceResolver(Document document) {
    sourceDocument = document;
  }

  @Override
  public String getNamespaceURI(String prefix) {
    if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
      return sourceDocument.lookupNamespaceURI(null);
    } else {
      return sourceDocument.lookupNamespaceURI(prefix);
    }
  }

  @Override
  public String getPrefix(String namespaceURI) {
    return sourceDocument.lookupPrefix(namespaceURI);
  }

  @Override
  public Iterator getPrefixes(String namespaceURI) {
    return null;
  }
}
