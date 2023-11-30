/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.settings;

import com.acrolinx.sidebar.pojo.document.IntRange;
import com.google.gson.Gson;
import java.util.List;

public class DocumentSelection {
  private final int[][] ranges;

  public DocumentSelection(List<IntRange> intRanges) {
    this.ranges = convertDocumentSelection(intRanges);
  }

  public int[][] convertDocumentSelection(List<IntRange> intRanges) {
    if (intRanges != null) {
      int size = intRanges.size();
      int[][] rangesArray = new int[size][2];
      int index = 0;

      for (IntRange intRange : intRanges) {
        rangesArray[index][0] = intRanges.get(index).getMinimumInteger();
        rangesArray[index][1] = intRanges.get(index).getMaximumInteger();
        index++;
      }

      return rangesArray;
    }

    return null;
  }

  @Override
  public String toString() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }
}
