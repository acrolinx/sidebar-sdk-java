/* Copyright (c) 2016-present Acrolinx GmbH */

package com.acrolinx.sidebar.lookup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.acrolinx.sidebar.pojo.document.AcrolinxMatch;
import com.acrolinx.sidebar.pojo.document.externalContent.ExternalContent;
import com.acrolinx.sidebar.pojo.document.externalContent.ExternalContentField;
import com.acrolinx.sidebar.pojo.document.externalContent.ExternalContentMatch;
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

    public List<? extends AbstractMatch> getMatchesWithCorrectedExternalMatches(List<? extends AbstractMatch> matches ,ExternalContent checkedExternalContent,ExternalContent changedExternalContent) {
        return matches.stream().map((match) -> {
            if (!(match instanceof AcrolinxMatch))
                return match;
            AcrolinxMatch acrolinxMatch = (AcrolinxMatch) match;

            if (!acrolinxMatch.hasExternalContentMatches())
                return match;

            //todo: this needs to become recursive for multiple levels of referenced content
            List<ExternalContentMatch> externalContentMatches = acrolinxMatch.getExternalContentMatches();
            List<ExternalContentMatch> correctedMatches = getExternalContentMatchesWithCorrectedRanges(externalContentMatches,
                    checkedExternalContent, changedExternalContent);

            return new AcrolinxMatch(match.getRange(), ((AcrolinxMatch) match).getExtractedRange(),
                    match.getContent(), correctedMatches);
        }).collect(Collectors.toList());
    }

    public List<ExternalContentMatch> getExternalContentMatchesWithCorrectedRanges( List<ExternalContentMatch> matches, ExternalContent checkedText, ExternalContent changedText
                                                                                              ) {
        List<ExternalContentField> checkedExternalContent = checkedText.getAll();
        List<ExternalContentField> changedExternalContent = changedText.getAll();

        return matches.stream().map((match) -> {
            Optional<ExternalContentField> optionalCheckedField = checkedExternalContent.stream().filter( (ExternalContentField old) -> old.getId().equals(match.getId())).findFirst();
            Optional<ExternalContentField> optionalChangedField = changedExternalContent.stream().filter( (ExternalContentField old) -> old.getId().equals(match.getId())).findFirst();

            if(!optionalCheckedField.isPresent() || !optionalChangedField.isPresent()) return match;

            ExternalContentField checkedField = optionalCheckedField.get();
            ExternalContentField changedField = optionalChangedField.get();

            List<DiffMatchPatch.Diff> diffs = Lookup.getDiffs(checkedField.getContent(), changedField.getContent());
            List<OffsetAlign> offsetMappingArray = Lookup.createOffsetMappingArray(diffs);

            Optional<IntRange> correctedMatch = Lookup.getCorrectedMatch(diffs, offsetMappingArray,
                    match.getRange().getMinimumInteger(), match.getRange().getMaximumInteger());

            if (correctedMatch.isPresent()) {
                return match.setRange(correctedMatch.get());
            } else {
                return match;
            }
        }).collect(Collectors.toList());
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
