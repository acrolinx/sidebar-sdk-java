/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.acrolinx.sidebar.pojo.document.AbstractMatch;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatch;
import com.acrolinx.sidebar.pojo.document.IntRange;
import java.util.List;
import org.junit.jupiter.api.Test;

class MatchUtilsTest {
  @Test
  void testSortByOffsetDesc() {
    final List<AcrolinxMatch> acrolinxMatches =
        List.of(
            new AcrolinxMatch(new IntRange(7, 4), "4"),
            new AcrolinxMatch(new IntRange(0, 5), "2"),
            new AcrolinxMatch(new IntRange(3, 7), "3"),
            new AcrolinxMatch(new IntRange(0, 4), "1"));

    final List<AcrolinxMatch> sortedMatches = MatchUtils.sortByOffsetDesc(acrolinxMatches);

    assertEquals("4", sortedMatches.get(0).getContent());
    assertEquals("3", sortedMatches.get(1).getContent());
    assertEquals("2", sortedMatches.get(2).getContent());
    assertEquals("1", sortedMatches.get(3).getContent());
  }

  @Test
  void testShouldRemoveWhiteSpaceMatch() {
    final List<AcrolinxMatch> acrolinxMatches =
        List.of(
            new AcrolinxMatch(new IntRange(0, 3), "abc"),
            new AcrolinxMatch(new IntRange(3, 6), " "),
            new AcrolinxMatch(new IntRange(6, 9), "pqr"));
    String lastCheckedContent = "abc\n\t\tpqr";

    verifyMatches(
        List.of(acrolinxMatches.get(0), acrolinxMatches.get(2)),
        MatchUtils.filterInsignificantWhitespaceMatches(acrolinxMatches, lastCheckedContent));
  }

  @Test
  void testShouldNotRemoveWhiteSpaceMatch() {
    final List<AcrolinxMatch> acrolinxMatches =
        List.of(
            new AcrolinxMatch(new IntRange(0, 3), "abc"),
            new AcrolinxMatch(new IntRange(3, 4), " "),
            new AcrolinxMatch(new IntRange(4, 7), "pqr"));
    String lastCheckedContent = "abc pqr";

    verifyMatches(
        acrolinxMatches,
        MatchUtils.filterInsignificantWhitespaceMatches(acrolinxMatches, lastCheckedContent));
  }

  @Test
  void testShouldNotRemoveWhiteSpacesIfRepresentedCorrectlyByOffsets() {
    final List<AcrolinxMatch> acrolinxMatches =
        List.of(
            new AcrolinxMatch(new IntRange(0, 3), "abc"),
            new AcrolinxMatch(new IntRange(3, 7), "    "),
            new AcrolinxMatch(new IntRange(7, 10), "pqr"));
    String lastCheckedContent = "abc    pqr";

    verifyMatches(
        acrolinxMatches,
        MatchUtils.filterInsignificantWhitespaceMatches(acrolinxMatches, lastCheckedContent));
  }

  private static void verifyMatches(
      List<? extends AbstractMatch> expectedAbstractMatches,
      List<? extends AbstractMatch> actualAbstractMatches) {
    assertEquals(expectedAbstractMatches.size(), actualAbstractMatches.size());
    for (int i = 0; i < expectedAbstractMatches.size(); i++) {
      verifyMatch(expectedAbstractMatches.get(i), actualAbstractMatches.get(i));
    }
  }

  private static void verifyMatch(
      AbstractMatch expectedAbstractMatch, AbstractMatch actualAbstractMatch) {
    assertTrue(expectedAbstractMatch.getContent().equals(actualAbstractMatch.getContent()));

    final IntRange expectedMatchRange = expectedAbstractMatch.getRange();
    final IntRange actualMatchRange = actualAbstractMatch.getRange();

    assertEquals(expectedMatchRange.getMinimumInteger(), actualMatchRange.getMinimumInteger());
    assertEquals(expectedMatchRange.getMaximumInteger(), actualMatchRange.getMaximumInteger());
  }
}
