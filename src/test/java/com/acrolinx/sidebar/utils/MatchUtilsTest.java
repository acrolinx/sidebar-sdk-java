/*
 * Copyright (c) 2020-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.utils;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.acrolinx.sidebar.pojo.document.AcrolinxMatch;
import com.acrolinx.sidebar.pojo.document.IntRange;
import com.google.common.collect.Lists;

public class MatchUtilsTest
{
    @Test
    public void testSortByOffsetDesc()
    {
        final List<AcrolinxMatch> matches = Lists.newArrayList();

        matches.add(new AcrolinxMatch(new IntRange(7, 4), "4"));
        matches.add(new AcrolinxMatch(new IntRange(0, 5), "2"));
        matches.add(new AcrolinxMatch(new IntRange(3, 7), "3"));
        matches.add(new AcrolinxMatch(new IntRange(0, 4), "1"));

        final List<AcrolinxMatch> sortedMatches = MatchUtils.sortByOffsetDesc(matches);

        assertEquals("4", sortedMatches.get(0).getContent());
        assertEquals("3", sortedMatches.get(1).getContent());
        assertEquals("2", sortedMatches.get(2).getContent());
        assertEquals("1", sortedMatches.get(3).getContent());

    }

}