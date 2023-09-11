/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.lookup;

import static com.acrolinx.sidebar.lookup.Lookup.createOffsetMappingArray;
import static com.acrolinx.sidebar.lookup.Lookup.getDiffs;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.acrolinx.sidebar.utils.DiffMatchPatch;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class LookupTest
{
    @Test
    void offsetDiffIsNullForEqualStrings()
    {
        String first = "TEST";
        String second = "TEST";
        List<DiffMatchPatch.Diff> diffs = getDiffs(first, second);
        List<OffsetAlign> offsetMappingArray = createOffsetMappingArray(diffs);
        assertEquals(1, offsetMappingArray.size());

        OffsetAlign offsetAlign = offsetMappingArray.get(0);
        assertEquals(0, offsetAlign.getDiffOffset());
        assertEquals(4, offsetAlign.getOldPosition());
    }

    @Test
    void offsetDiffIsCalculatedProperlyForInsertion()
    {
        String original = "This is a test.";
        String changed = "This is a manipulated test.";
        List<DiffMatchPatch.Diff> diffs = getDiffs(original, changed);
        List<OffsetAlign> offsetMappingArray = createOffsetMappingArray(diffs);
        assertEquals(3, offsetMappingArray.size());

        assertEquals(0, offsetMappingArray.get(0).getDiffOffset());
        assertEquals(10, offsetMappingArray.get(0).getOldPosition());
        assertEquals(12, offsetMappingArray.get(1).getDiffOffset());
        assertEquals(10, offsetMappingArray.get(1).getOldPosition());
        assertEquals(12, offsetMappingArray.get(2).getDiffOffset());
        assertEquals(15, offsetMappingArray.get(2).getOldPosition());
    }

    @Test
    void offsetDiffIsCalculatedProperlyForDeletion()
    {
        String original = "This is a test.";
        String changed = "This is test.";
        List<DiffMatchPatch.Diff> diffs = getDiffs(original, changed);
        List<OffsetAlign> offsetMappingArray = createOffsetMappingArray(diffs);
        assertEquals(3, offsetMappingArray.size());
        assertEquals(0, offsetMappingArray.get(0).getDiffOffset());
        assertEquals(8, offsetMappingArray.get(0).getOldPosition());
        assertEquals(-2, offsetMappingArray.get(1).getDiffOffset());
        assertEquals(10, offsetMappingArray.get(1).getOldPosition());
        assertEquals(-2, offsetMappingArray.get(2).getDiffOffset());
        assertEquals(15, offsetMappingArray.get(2).getOldPosition());
    }

    @Test
    void offsetDiffIsCalculatedNonWordBoundaries()
    {
        String original = "This is a test.";
        String changed = "This is Ateset.";
        List<DiffMatchPatch.Diff> diffs = getDiffs(original, changed);
        List<OffsetAlign> offsetMappingArray = createOffsetMappingArray(diffs);
        assertEquals(6, offsetMappingArray.size());
        assertEquals(0, offsetMappingArray.get(0).getDiffOffset());
        assertEquals(8, offsetMappingArray.get(0).getOldPosition());
        assertEquals(-2, offsetMappingArray.get(1).getDiffOffset());
        assertEquals(10, offsetMappingArray.get(1).getOldPosition());
        assertEquals(-1, offsetMappingArray.get(2).getDiffOffset());
        assertEquals(10, offsetMappingArray.get(2).getOldPosition());
    }

    @Test
    void testGetOffSetDiffStart()
    {
        String original = "This is a test.";
        String changed = "This isfgfg a Ateset.";
        Optional<Integer> offSetDiff = LookupRangesDiff.getOffSetDiffStart(original, changed, 10);
        assertEquals(5, offSetDiff.get().intValue());
    }

    @Test
    void testGetOffSetDiffStart2()
    {
        String original = "This is a test.";
        String changed = "qua67tkThis isfgfg a Ateset.";
        Optional<Integer> offSetDiff = LookupRangesDiff.getOffSetDiffStart(original, changed, 0);
        assertEquals(7, offSetDiff.get().intValue());
    }

    @Test
    void testGetOffSetDiffStart3()
    {
        String original = "This is a test.";
        String changed = "Tghis isfgfg a Ateset.";
        Optional<Integer> offSetDiff = LookupRangesDiff.getOffSetDiffStart(original, changed, 0);
        assertEquals(0, offSetDiff.get().intValue());
    }

    @Test
    void testGetOffSetDiffEnd()
    {
        String original = "This is a test.";
        String changed = "Thdfis is AnntestB.";
        Optional<Integer> offSetDiff = LookupRangesDiff.getOffSetDiffEnd(original, changed, 14);
        assertEquals(3, offSetDiff.get().intValue());
    }

    @Test
    void testGetOffSetDiffStart4()
    {
        String original = "qua67tkThis\n isfgfg a Ateset.";
        String changed = "This is a test.";
        Optional<Integer> offSetDiff = LookupRangesDiff.getOffSetDiffStart(original, changed, 7);
        assertEquals(-7, offSetDiff.get().longValue());
    }

    @Test
    void testGetOffSetDiffEnd2()
    {
        String original = "This is a test.";
        String changed = "Thdfis is Anntest.";
        Optional<Integer> offSetDiff = LookupRangesDiff.getOffSetDiffEnd(original, changed, 14);
        assertEquals(3, offSetDiff.get().intValue());
    }

    @Test
    void testGetOffSetDiffEnd21()
    {
        String original = "dkfjsf lsdthkk This is a test.";
        String changed = "This is Anntest.";
        Optional<Integer> offSetDiff = LookupRangesDiff.getOffSetDiffStart(original, changed, 15);
        assertEquals(-15, offSetDiff.get().intValue());
    }
}
