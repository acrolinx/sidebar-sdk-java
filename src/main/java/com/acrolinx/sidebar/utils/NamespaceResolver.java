/* Copyright (c) 2023 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import java.util.Collections;
import java.util.Iterator;
import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import org.w3c.dom.Document;

public final class NamespaceResolver implements NamespaceContext {
  private final Document sourceDocument;

  public static NamespaceContext create(Document document) {
    return new NamespaceResolver(document);
  }

  private NamespaceResolver(Document document) {
    sourceDocument = document;
  }

  @Override
  public String getNamespaceURI(String prefix) {
    if (prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
      return sourceDocument.lookupNamespaceURI(null);
    }

    return sourceDocument.lookupNamespaceURI(prefix);
  }

  @Override
  public String getPrefix(String namespaceUri) {
    return sourceDocument.lookupPrefix(namespaceUri);
  }

  @Override
  public Iterator<String> getPrefixes(String namespaceUri) {
    return Collections.emptyIterator();
  }
}
