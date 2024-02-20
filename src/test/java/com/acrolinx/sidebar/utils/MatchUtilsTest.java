/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
