/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.document.externalcontent;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExternalContentTest {
  @Test
  void toStringTest() {
    ExternalContent externalContent =
        new ExternalContent(List.of(), List.of(), List.of(), List.of());

    Assertions.assertEquals(
        "{\"textReplacements\":[],\"entities\":[],\"ditaReferences\":[],\"xincludeReferences\":[]}",
        externalContent.toString());
  }
}
