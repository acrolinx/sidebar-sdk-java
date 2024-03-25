/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.settings;

import com.acrolinx.sidebar.pojo.document.IntRange;
import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContent;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CheckOptionsTest {
  @Test
  void toStringTest() {
    CheckOptions checkOptions = createCheckOptions();

    Assertions.assertEquals(
        "{\"inputFormat\":\"AUTO\",\"requestDescription\":{\"documentReference\":\"foo\"},\"selection\":{\"ranges\":[[0,1]]},\"externalContent\":{\"textReplacements\":[],\"entities\":[],\"ditaReferences\":[],\"xincludeReferences\":[]}}",
        checkOptions.toString());
  }

  private static CheckOptions createCheckOptions() {
    RequestDescription requestDescription = new RequestDescription("foo");

    DocumentSelection documentSelection = new DocumentSelection(List.of(new IntRange(0, 1)));

    ExternalContent externalContent =
        new ExternalContent(List.of(), List.of(), List.of(), List.of());

    return new CheckOptions(
        requestDescription, InputFormat.AUTO, documentSelection, externalContent);
  }
}
