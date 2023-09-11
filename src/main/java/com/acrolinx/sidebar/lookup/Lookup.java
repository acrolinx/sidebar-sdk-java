/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.lookup;

import com.acrolinx.sidebar.pojo.document.IntRange;
import com.acrolinx.sidebar.utils.DiffMatchPatch;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public final class Lookup
{
    private Lookup()
    {
        throw new IllegalStateException();
    }

    public static List<OffsetAlign> createOffsetMappingArray(List<DiffMatchPatch.Diff> diffs)
    {
        List<OffsetAlign> offsetAligns = new LinkedList<>();
        final AtomicInteger offsetCountOld = new AtomicInteger(0);
        final AtomicInteger currentDiffOffset = new AtomicInteger(0);

        diffs.forEach(diff -> {
            int offsetCountOldInt = offsetCountOld.get();
            int currentDiffOffsetInt = currentDiffOffset.get();
            int diffLengths = diff.text.length();

            switch (diff.operation) {
                case DELETE:
                    offsetCountOld.set(offsetCountOldInt + diffLengths);
                    currentDiffOffset.set(currentDiffOffsetInt - diffLengths);
                    break;
                case INSERT:
                    currentDiffOffset.set(currentDiffOffsetInt + diffLengths);
                    break;
                case EQUAL:
                    offsetCountOld.set(offsetCountOldInt + diffLengths);
                    break;
            }

            offsetAligns.add(new OffsetAlign(offsetCountOld.get(), currentDiffOffset.get()));
        });

        return Collections.unmodifiableList(offsetAligns);
    }

    protected static Optional<IntRange> getCorrectedMatch(List<DiffMatchPatch.Diff> diffs,
            List<OffsetAlign> offsetAligns, int offsetStart, int offsetEnd)
    {
        Optional<OffsetAlign> first = offsetAligns.stream().filter(
                offsetAlign -> offsetAlign.getOldPosition() >= offsetEnd).findFirst();

        if (first.isPresent()) {
            int index = offsetAligns.indexOf(first.get());

            if (index > 0 && offsetAligns.get(index - 1).getOldPosition() <= offsetStart
                    && diffs.get(index).operation == DiffMatchPatch.Operation.EQUAL) {
                final int diffOffset = offsetAligns.get(index).getDiffOffset();
                return Optional.of(new IntRange(offsetStart + diffOffset, offsetEnd + diffOffset));
            }

            if (index == 0 && diffs.get(0).operation == DiffMatchPatch.Operation.EQUAL) {
                return Optional.of(new IntRange(offsetStart, offsetEnd));
            }
        }

        return Optional.empty();
    }

    public static List<DiffMatchPatch.Diff> getDiffs(String checkedText, String changedText)
    {
        DiffMatchPatch diffMatchPatch = new DiffMatchPatch();
        diffMatchPatch.Diff_Timeout = 5;
        LinkedList<DiffMatchPatch.Diff> diffs = diffMatchPatch.diff_main(checkedText, changedText);
        diffMatchPatch.diff_cleanupSemanticLossless(diffs);
        return Collections.unmodifiableList(diffs);
    }

    public static Optional<Integer> getDiffOffsetPositionStart(List<OffsetAlign> offsetAligns, int offset)
    {
        Optional<OffsetAlign> first = offsetAligns.stream().filter(
                offsetAlign -> offsetAlign.getOldPosition() >= offset + 1).findFirst();

        if (first.isPresent()) {
            int index = offsetAligns.indexOf(first.get());

            if (index >= 0) {
                final int diffOffset = offsetAligns.get(index).getDiffOffset();
                return Optional.of(diffOffset);
            }
        }

        return Optional.empty();
    }

    public static Optional<Integer> getDiffOffsetPositionEnd(List<OffsetAlign> offsetAligns, int offset)
    {
        Optional<OffsetAlign> first = offsetAligns.stream().filter(a -> a.getOldPosition() <= offset - 1
                && offsetAligns.get(offsetAligns.indexOf(a) + 1).getOldPosition() >= offset).findFirst();

        if (first.isPresent()) {
            int index = offsetAligns.indexOf(first.get());

            if (index >= 0) {
                final int diffOffset = offsetAligns.get(index).getDiffOffset();
                return Optional.of(diffOffset);
            }
        }

        return Optional.empty();
    }
}
