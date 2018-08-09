/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.lookup;

import static com.acrolinx.sidebar.lookup.Lookup.createOffsetMappingArray;
import static com.acrolinx.sidebar.lookup.Lookup.getDiffs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.junit.Test;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class LookupTest
{
    @Test
    public void offsetDiffIsNullForEqualStrings()
    {
        String first = "TEST";
        String second = "TEST";
        List<DiffMatchPatch.Diff> diffs = getDiffs(first, second);
        List<OffsetAlign> offsetMappingArray = createOffsetMappingArray(diffs);
        assert offsetMappingArray.size() == 1;
        assertEquals(0, offsetMappingArray.get(0).getDiffOffset());
        assertEquals(4, offsetMappingArray.get(0).getOldPosition());
    }

    @Test
    public void offsetDiffIsCalculatedProperlyForInsertion()
    {
        String original = "This is a test.";
        String changed = "This is a manipulated test.";
        List<DiffMatchPatch.Diff> diffs = getDiffs(original, changed);
        List<OffsetAlign> offsetMappingArray = createOffsetMappingArray(diffs);
        assert offsetMappingArray.size() == 3;
        assertEquals(0, offsetMappingArray.get(0).getDiffOffset());
        assertEquals(10, offsetMappingArray.get(0).getOldPosition());
        assertEquals(12, offsetMappingArray.get(1).getDiffOffset());
        assertEquals(10, offsetMappingArray.get(1).getOldPosition());
        assertEquals(12, offsetMappingArray.get(2).getDiffOffset());
        assertEquals(15, offsetMappingArray.get(2).getOldPosition());
    }

    @Test
    public void offsetDiffIsCalculatedProperlyForDeletion()
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
    public void offsetDiffIsCalculatedNonWordBoundaries()
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
    public void testGetOffSetDiffStart()
    {
        String original = "This is a test.";
        String changed = "This isfgfg a Ateset.";
        Optional<Integer> offSetDiff = LookupRangesDiff.getOffSetDiffStart(original, changed, 10);
        assertTrue(offSetDiff.isPresent());
        assertEquals(0, offSetDiff.get().compareTo(5));
    }

    @Test
    public void testGetOffSetDiffStart2()
    {
        String original = "This is a test.";
        String changed = "qua67tkThis isfgfg a Ateset.";
        Optional<Integer> offSetDiff = LookupRangesDiff.getOffSetDiffStart(original, changed, 0);
        System.out.println(offSetDiff.get());
        assertTrue(offSetDiff.isPresent());
        assertEquals(0, offSetDiff.get().compareTo(7));
    }

    @Test
    public void testGetOffSetDiffStart3()
    {
        String original = "This is a test.";
        String changed = "Tghis isfgfg a Ateset.";
        Optional<Integer> offSetDiff = LookupRangesDiff.getOffSetDiffStart(original, changed, 0);
        System.out.println(offSetDiff.get());
        assertTrue(offSetDiff.isPresent());
        assertEquals(0, offSetDiff.get().compareTo(0));
    }

    @Test
    public void testGetOffSetDiffEnd()
    {
        String original = "This is a test.";
        String changed = "Thdfis is AnntestB.";
        Optional<Integer> offSetDiff = LookupRangesDiff.getOffSetDiffEnd(original, changed, 14);
        assertTrue(offSetDiff.isPresent());
        assertEquals(0, offSetDiff.get().compareTo(3));
    }

    @Test
    public void testGetOffSetDiffStart4()
    {
        String original = "qua67tkThis\n isfgfg a Ateset.";
        String changed = "This is a test.";
        Optional<Integer> offSetDiff = LookupRangesDiff.getOffSetDiffStart(original, changed, 7);
        Optional<Integer> offSetDiff1 = LookupRangesDiff.getOffSetDiffEnd(original, changed, 11);
        System.out.println(offSetDiff1.get());
        assertTrue(offSetDiff.isPresent());
        assertEquals(0, offSetDiff.get().compareTo(-7));
    }

    @Test
    public void testGetOffSetDiffEnd2()
    {
        String original = "This is a test.";
        String changed = "Thdfis is Anntest.";
        Optional<Integer> offSetDiff = LookupRangesDiff.getOffSetDiffEnd(original, changed, 14);
        assertTrue(offSetDiff.isPresent());
        assertEquals(0, offSetDiff.get().compareTo(3));
    }

    @Test
    public void testGetOffSetDiffEnd21()
    {
        String original = "dkfjsf lsdthkk This is a test.";
        String changed = "This is Anntest.";
        Optional<Integer> offSetDiff = LookupRangesDiff.getOffSetDiffStart(original, changed, 15);
        assertTrue(offSetDiff.isPresent());
        assertEquals(0, offSetDiff.get().compareTo(-15));
    }

}
