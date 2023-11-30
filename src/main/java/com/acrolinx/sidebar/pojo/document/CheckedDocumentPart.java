/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.document;

import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContentMatch;
import com.google.gson.Gson;
import java.util.List;

public class CheckedDocumentPart {
  private final String checkId;
  private final IntRange intRange;
  private List<ExternalContentMatch> externalContent;

  public CheckedDocumentPart(String checkId, IntRange intRange) {
    this.checkId = checkId;
    this.intRange = intRange;
  }

  public CheckedDocumentPart(
      String checkId, IntRange intRange, List<ExternalContentMatch> externalContent) {
    this(checkId, intRange);
    this.externalContent = externalContent;
  }

  public String getCheckId() {
    return checkId;
  }

  public IntRange getRange() {
    return intRange;
  }

  public List<ExternalContentMatch> getExternalContent() {
    return externalContent;
  }

  @Override
  public String toString() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }

  public String getAsJS() {
    if (externalContent != null) {
      return "{checkId: \""
          + checkId
          + "\", range:["
          + intRange.getMinimumInteger()
          + ","
          + intRange.getMaximumInteger()
          + "], externalContent:"
          + externalContent.toString()
          + "}";
    }

    return "{checkId: \""
        + checkId
        + "\", range:["
        + intRange.getMinimumInteger()
        + ","
        + intRange.getMaximumInteger()
        + "]}";
  }
}
