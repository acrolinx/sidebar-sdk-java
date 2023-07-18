/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.lookup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acrolinx.sidebar.LookupRanges;
import com.acrolinx.sidebar.pojo.document.AbstractMatch;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatch;
import com.acrolinx.sidebar.pojo.document.IntRange;
import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContent;
import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContentField;
import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContentMatch;
import com.acrolinx.sidebar.utils.DiffMatchPatch;

public class LookupRangesDiff extends LookupRanges
{
    private static final Logger logger = LoggerFactory.getLogger(LookupRangesDiff.class);

    @Override
    public Optional<List<? extends AbstractMatch>> getMatchesWithCorrectedRanges(String checkedText, String changedText,
            List<? extends AbstractMatch> abstractMatches)
    {
        List<DiffMatchPatch.Diff> diffs = Lookup.getDiffs(checkedText, changedText);
        List<OffsetAlign> offsetMappingArray = Lookup.createOffsetMappingArray(diffs);
        List<AbstractMatch> returnValues = new ArrayList<>();
        boolean anyEmpty = abstractMatches.stream().anyMatch(abstractMatch -> {
            Optional<IntRange> correctedMatch = Lookup.getCorrectedMatch(diffs, offsetMappingArray,
                    abstractMatch.getRange().getMinimumInteger(), abstractMatch.getRange().getMaximumInteger());
            if (correctedMatch.isPresent()) {
                AbstractMatch copy = abstractMatch.setRange(correctedMatch.get());
                returnValues.add(copy);
                return false;
            }

            return true;
        });

        if (anyEmpty) {
            return Optional.empty();
        }
        return Optional.of(Collections.unmodifiableList(returnValues));
    }

    public List<? extends AbstractMatch> getMatchesIncludingCorrectedExternalMatches(
            ExternalContent checkedExternalContent, ExternalContent changedExternalContent,
            List<? extends AbstractMatch> abstractMatches)
    {
        return abstractMatches.stream().map(abstractMatch -> {
            if (!(abstractMatch instanceof AcrolinxMatch))
                return abstractMatch;
            AcrolinxMatch acrolinxMatch = (AcrolinxMatch) abstractMatch;

            if (!acrolinxMatch.hasExternalContentMatches())
                return abstractMatch;

            List<ExternalContentMatch> externalContentMatches = acrolinxMatch.getExternalContentMatches();

            List<ExternalContentMatch> correctedMatches = getExternalContentMatchesWithCorrectedRanges(
                    externalContentMatches, checkedExternalContent, changedExternalContent);

            return new AcrolinxMatch(abstractMatch.getRange(), ((AcrolinxMatch) abstractMatch).getExtractedRange(),
                    abstractMatch.getContent(), correctedMatches);
        }).collect(Collectors.toList());
    }

    public List<ExternalContentMatch> getExternalContentMatchesWithCorrectedRanges(List<ExternalContentMatch> matches,
            ExternalContent checkedText, ExternalContent changedText)
    {
        List<ExternalContentField> checkedExternalContent = checkedText.getAll();
        List<ExternalContentField> changedExternalContent = changedText.getAll();

        return matches.stream().map(match -> {
            if (!match.getExternalContentMatches().isEmpty()) {
                List<ExternalContentMatch> newMatches = getExternalContentMatchesWithCorrectedRanges(
                        match.getExternalContentMatches(), checkedText, changedText);
                match.setExternalContentMatches(newMatches);
            }

            return adJustMatch(match, checkedExternalContent, changedExternalContent);

        }).collect(Collectors.toList());
    }

    public ExternalContentMatch adJustMatch(ExternalContentMatch match,
            List<ExternalContentField> checkedExternalContent, List<ExternalContentField> changedExternalContent)
    {
        Optional<ExternalContentField> optionalCheckedField = checkedExternalContent.stream().filter(
                (ExternalContentField old) -> old.getId().equals(match.getId())).findFirst();
        Optional<ExternalContentField> optionalChangedField = changedExternalContent.stream().filter(
                (ExternalContentField old) -> old.getId().equals(match.getId())).findFirst();

        if (!optionalCheckedField.isPresent() || !optionalChangedField.isPresent())
            return match;

        ExternalContentField checkedField = optionalCheckedField.get();
        ExternalContentField changedField = optionalChangedField.get();

        List<DiffMatchPatch.Diff> diffs = Lookup.getDiffs(checkedField.getContent(), changedField.getContent());
        List<OffsetAlign> offsetMappingArray = Lookup.createOffsetMappingArray(diffs);

        Optional<IntRange> correctedMatch = Lookup.getCorrectedMatch(diffs, offsetMappingArray,
                match.getRange().getMinimumInteger(), match.getRange().getMaximumInteger());

        if (correctedMatch.isPresent()) {
            return match.setRange(correctedMatch.get());
        }

        logger.warn("Could not adjust external Content Match");
        return match;
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
