/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.document;

import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContentMatch;
import java.util.List;

public class AcrolinxMatchFromJSON {
  private String content;
  private int[] extractedRange;
  private int[] range;
  private String replacement;
  private List<ExternalContentMatch> externalContentMatches;

  public AcrolinxMatchFromJSON() {}

  public AcrolinxMatch getAsAcrolinxMatch() {
    if (extractedRange != null && externalContentMatches != null) {
      return new AcrolinxMatch(
          new IntRange(range[0], range[1]),
          new IntRange(extractedRange[0], extractedRange[1]),
          content,
          externalContentMatches);
    } else if (extractedRange != null) {
      return new AcrolinxMatch(
          new IntRange(range[0], range[1]),
          new IntRange(extractedRange[0], extractedRange[1]),
          content);
    } else if (externalContentMatches != null) {
      return new AcrolinxMatch(new IntRange(range[0], range[1]), content, externalContentMatches);
    }

    return new AcrolinxMatch(new IntRange(range[0], range[1]), content);
  }

  public AcrolinxMatchWithReplacement getAsAcrolinxMatchWithReplacement() {
    if (replacement != null && extractedRange != null) {
      if (externalContentMatches != null) {
        return new AcrolinxMatchWithReplacement(
            new IntRange(range[0], range[1]),
            new IntRange(extractedRange[0], extractedRange[1]),
            content,
            replacement,
            externalContentMatches);
      }

      return new AcrolinxMatchWithReplacement(
          new IntRange(range[0], range[1]),
          new IntRange(extractedRange[0], extractedRange[1]),
          content,
          replacement);
    } else if (replacement != null) {
      if (externalContentMatches != null) {
        return new AcrolinxMatchWithReplacement(
            content, new IntRange(range[0], range[1]), replacement, externalContentMatches);
      }

      return new AcrolinxMatchWithReplacement(
          content, new IntRange(range[0], range[1]), replacement);
    }

    return null;
  }
}
