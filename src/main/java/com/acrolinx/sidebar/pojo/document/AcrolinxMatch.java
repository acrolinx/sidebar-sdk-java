/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.document;

import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContentMatch;
import java.util.List;

public class AcrolinxMatch extends ExternalAbstractMatch {
  private final String content;
  private IntRange extractedRange;
  private IntRange intRange;
  private List<ExternalContentMatch> externalContentMatches;

  public AcrolinxMatch(final IntRange intRange, final String content) {
    this.content = content;
    this.intRange = intRange;
  }

  public AcrolinxMatch(
      final IntRange intRange, final IntRange extractedRange, final String content) {
    this(intRange, content);
    this.extractedRange = extractedRange;
  }

  public AcrolinxMatch(
      final IntRange intRange,
      final IntRange extractedRange,
      final String content,
      final List<ExternalContentMatch> externalContentMatches) {
    this(intRange, extractedRange, content);
    this.externalContentMatches = externalContentMatches;
  }

  public AcrolinxMatch(
      final IntRange intRange,
      final String content,
      final List<ExternalContentMatch> externalContentMatches) {
    this(intRange, content);
    this.externalContentMatches = externalContentMatches;
  }

  @Override
  public String getContent() {
    return content;
  }

  public IntRange getExtractedRange() {
    return extractedRange;
  }

  @Override
  public IntRange getRange() {
    return intRange;
  }

  @Override
  public AcrolinxMatch setRange(final IntRange intRange) {
    if (this.extractedRange != null) {
      final int eRangeMin = this.extractedRange.getMinimumInteger();
      final int eRangeMax = this.extractedRange.getMaximumInteger();

      if (this.getExternalContentMatches() != null) {
        final List<ExternalContentMatch> externalContentMatches = this.getExternalContentMatches();
        return new AcrolinxMatch(
            intRange, new IntRange(eRangeMin, eRangeMax), content, externalContentMatches);
      }

      return new AcrolinxMatch(intRange, new IntRange(eRangeMin, eRangeMax), content);
    }

    if (this.getExternalContentMatches() != null) {
      final List<ExternalContentMatch> externalContentMatches = this.getExternalContentMatches();
      return new AcrolinxMatch(intRange, content, externalContentMatches);
    }

    return new AcrolinxMatch(intRange, content);
  }

  @Override
  public boolean hasExternalContentMatches() {
    return this.externalContentMatches != null && !this.getExternalContentMatches().isEmpty();
  }

  @Override
  public List<ExternalContentMatch> getExternalContentMatches() {
    return this.externalContentMatches;
  }

  @Override
  public AbstractMatch copy() {
    final int rangeMin = this.intRange.getMinimumInteger();
    final int rangeMax = this.intRange.getMaximumInteger();

    if (this.extractedRange != null) {
      final int eRangeMin = this.extractedRange.getMinimumInteger();
      final int eRangeMax = this.extractedRange.getMaximumInteger();

      if (this.getExternalContentMatches() != null) {
        final List<ExternalContentMatch> externalContentMatches = this.getExternalContentMatches();
        return new AcrolinxMatch(
            new IntRange(rangeMin, rangeMax),
            new IntRange(eRangeMin, eRangeMax),
            content,
            externalContentMatches);
      }

      return new AcrolinxMatch(
          new IntRange(rangeMin, rangeMax), new IntRange(eRangeMin, eRangeMax), content);
    }

    if (this.getExternalContentMatches() != null) {
      final List<ExternalContentMatch> externalContentMatches = this.getExternalContentMatches();
      return new AcrolinxMatch(new IntRange(rangeMin, rangeMax), content, externalContentMatches);
    }

    return new AcrolinxMatch(new IntRange(rangeMin, rangeMax), content);
  }
}
