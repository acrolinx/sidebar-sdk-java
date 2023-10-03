/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import org.xml.sax.SAXException;

public class FragmentContentException extends SAXException {
  private static final long serialVersionUID = 1L;

  public FragmentContentException(String message) {
    super(message);
  }
}
