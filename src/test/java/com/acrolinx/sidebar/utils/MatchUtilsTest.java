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
  void shouldRemoveWhiteSpaceMatch() {
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
  void shouldNotRemoveWhiteSpaceMatch() {
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
  void shouldNotRemoveWhiteSpacesIfRepresentedCorrectlyByOffsets() {
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

  private void verifyMatches(
      List<? extends AbstractMatch> expected, List<? extends AbstractMatch> actual) {
    assertEquals(expected.size(), actual.size());
    for (int i = 0; i < expected.size(); i++) {
      final AbstractMatch expectedMatch = expected.get(i);
      final AbstractMatch actualMatch = actual.get(i);

      assertTrue(expectedMatch.getContent().equals(actualMatch.getContent()));

      final IntRange expectedMatchRange = expectedMatch.getRange();
      final IntRange actualMatchRange = actualMatch.getRange();

      assertEquals(expectedMatchRange.getMinimumInteger(), actualMatchRange.getMinimumInteger());
      assertEquals(expectedMatchRange.getMaximumInteger(), actualMatchRange.getMaximumInteger());
    }
  }
}
