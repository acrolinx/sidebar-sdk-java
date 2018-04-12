/*
 * Copyright (c) 2016-2017 Acrolinx GmbH
 */

package com.acrolinx.sidebar.lookup;

import java.util.*;

import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;

import com.acrolinx.sidebar.LookupRanges;
import com.acrolinx.sidebar.pojo.document.AbstractMatch;
import com.acrolinx.sidebar.pojo.document.IntRange;

@SuppressWarnings("WeakerAccess")
public class LookupRangesDiff extends LookupRanges
{

    @Override
    public Optional<List<? extends AbstractMatch>> getMatchesWithCorrectedRanges(String checkedText, String changedText,
            List<? extends AbstractMatch> matches)
    {
        List<DiffMatchPatch.Diff> diffs = Lookup.getDiffs(checkedText, changedText);
        List<OffsetAlign> offsetMappingArray = Lookup.createOffsetMappingArray(diffs);
        List<AbstractMatch> returnValues = new ArrayList<>();
        boolean anyEmpty = matches.stream().anyMatch(match -> {
            Optional<IntRange> correctedMatch = Lookup.getCorrectedMatch(diffs, offsetMappingArray,
                    match.getRange().getMinimumInteger(), match.getRange().getMaximumInteger());
            if (correctedMatch.isPresent()) {
                AbstractMatch copy = match.setRange(correctedMatch.get());
                returnValues.add(copy);
                return false;
            } else {
                return true;
            }
        });

        if (anyEmpty) {
            return Optional.empty();
        }
        return Optional.of(Collections.unmodifiableList(returnValues));
    }

    public static Optional<Integer> getOffSetDiffStart(String originalVersion, String changedVersion,
            int offsetInOriginalVersion)
    {
        List<DiffMatchPatch.Diff> diffs = Lookup.getDiffs(originalVersion, changedVersion);
        List<OffsetAlign> offsetMappingArray = Lookup.createOffsetMappingArray(diffs);
        return Lookup.getDiffOffsetPositionStart(offsetMappingArray, offsetInOriginalVersion);
    }

    public static Optional<Integer> getOffSetDiffEnd(String originalVersion, String changedVersion,
            int offsetInOrignalVersion)
    {
        List<DiffMatchPatch.Diff> diffs = Lookup.getDiffs(originalVersion, changedVersion);
        List<OffsetAlign> offsetMappingArray = Lookup.createOffsetMappingArray(diffs);
        return Lookup.getDiffOffsetPositionEnd(offsetMappingArray, offsetInOrignalVersion);
    }
}
