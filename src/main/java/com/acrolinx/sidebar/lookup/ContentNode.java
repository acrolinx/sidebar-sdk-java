/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.lookup;

public interface ContentNode {
  int getStartOffset();

  int getEndOffset();

  String getContent();

  String getAsXMLFragment();
}
