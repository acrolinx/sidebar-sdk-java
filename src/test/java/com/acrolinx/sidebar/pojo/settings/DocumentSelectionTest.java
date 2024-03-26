/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.settings;

import com.acrolinx.sidebar.pojo.document.IntRange;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DocumentSelectionTest {
  @Test
  void convertDocumentSelection() {
    List<IntRange> intRanges =
        List.of(new IntRange(12, 12), new IntRange(13, 14), new IntRange(15, 16));
    DocumentSelection documentSelection = new DocumentSelection(intRanges);
    Assertions.assertEquals("{\"ranges\":[[12,12],[13,14],[15,16]]}", documentSelection.toString());
  }

  @Test
  void testConvertToStringOneValueOnly() {
    List<IntRange> intRanges = List.of(new IntRange(12, 12));
    DocumentSelection documentSelection = new DocumentSelection(intRanges);
    Assertions.assertEquals("{\"ranges\":[[12,12]]}", documentSelection.toString());
  }

  @Test
  void toStringTest() {
    DocumentSelection documentSelection = new DocumentSelection(List.of(new IntRange(0, 1)));

    Assertions.assertEquals("{\"ranges\":[[0,1]]}", documentSelection.toString());
  }
}
