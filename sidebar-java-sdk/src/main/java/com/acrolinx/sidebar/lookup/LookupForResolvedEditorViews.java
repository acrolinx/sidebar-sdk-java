
package com.acrolinx.sidebar.lookup;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.bitbucket.cowwoc.diffmatchpatch.DiffMatchPatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acrolinx.sidebar.pojo.document.AbstractMatch;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatchWithReplacement;
import com.acrolinx.sidebar.pojo.document.IntRange;

@SuppressWarnings("WeakerAccess")
public class LookupForResolvedEditorViews
{

    private final Logger logger = LoggerFactory.getLogger(LookupForResolvedEditorViews.class);

    private final List<AbstractMatch> mappedRanges = new ArrayList<>();
    private final List<AbstractMatch> newRanges = new ArrayList<>();

    public Optional<List<? extends AbstractMatch>> matchRangesForResolvedEditorView(
            List<? extends AbstractMatch> adjustedRangesForCurrentDocument, String currentDocumentContent,
            String resolvedViewContent, LookupForResolvedViewsHelper utils)
    {
        List<DiffMatchPatch.Diff> diffs = Lookup.getDiffs(currentDocumentContent, resolvedViewContent);
        List<OffsetAlign> offsetMappingArray = Lookup.createOffsetMappingArray(diffs);

        AtomicReference<List<AbstractMatch>> adjustedRangesCopyRef = new AtomicReference<>(new ArrayList<>());

        adjustedRangesForCurrentDocument.stream().forEach(match -> {
            AbstractMatch copy = match.copy();
            List<AbstractMatch> abstractMatches = adjustedRangesCopyRef.get();
            abstractMatches.add(copy);
            adjustedRangesCopyRef.set(abstractMatches);
        });

        List<AbstractMatch> adjustedRangesCopy = adjustedRangesCopyRef.get();

        adjustedRangesCopy.sort(new MatchComparator());

        if (utils != null) {
            // Try matching in content node.
            lookupMatchInContentNode(utils, adjustedRangesCopy, currentDocumentContent);
            adjustedRangesCopy = adjustedRangesCopy.stream().filter(match -> !mappedRanges.contains(match)).collect(
                    Collectors.toList());
        }

        if (adjustedRangesCopy.size() > 0) {
            // LookupMatches by diffing editor content and document content.
            getMatchesWithCorrectedRangesIgnoreNonMatched(diffs, offsetMappingArray, adjustedRangesCopy);
            adjustedRangesCopy = adjustedRangesCopy.stream().filter(match -> !mappedRanges.contains(match)).collect(
                    Collectors.toList());
        }

        if (adjustedRangesCopy.size() > 0) {
            List<AbstractMatch> filteredMatches = adjustedRangesCopy.stream().filter(match -> {
                logger.debug("Filter for ignorable whitespaces");
                boolean ignore = true;
                if (match instanceof AcrolinxMatchWithReplacement) {
                    logger.debug(((AcrolinxMatchWithReplacement) match).getReplacement());
                    ignore = "".equals(((AcrolinxMatchWithReplacement) match).getReplacement());
                    logger.debug("Replacement is empty? " + ignore);
                }
                logger.debug(match.getContent());
                logger.debug("match content is space or empty? "
                        + ("".equals(match.getContent()) || match.getContent().equalsIgnoreCase(" ")));
                boolean matchLineBreak = match.getContent().matches("[\\n\\r]+");
                logger.debug("match content is linebreak? " + matchLineBreak);
                return (("".equals(match.getContent()) || match.getContent().equals(" ") || matchLineBreak) && ignore);
            }).collect(Collectors.toList());

            adjustedRangesCopy = adjustedRangesCopy.stream().filter(match -> {
                boolean b = !filteredMatches.contains(match);
                if (!b) {
                    logger.debug("Removing non found match because contains deleted whitespaces.");
                    logger.debug(match.getRange().toString() + ", (" + match.getContent() + ")");
                }
                return b;
            }).collect(Collectors.toList());
        }

        if (adjustedRangesCopy.size() > 0) {
            return Optional.empty();
        }

        // Find duplicates.
        List<AbstractMatch> duplicates = new ArrayList<>();
        Set<AbstractMatch> matchSet = new TreeSet<>(new MatchComparator());
        duplicates.addAll(newRanges.stream().filter(m -> !matchSet.add(m)).collect(Collectors.toList()));
        for (AbstractMatch m : duplicates) {
            logger.debug("Removing duplicate matched range: " + m.getRange().toString() + ", (" + m.getContent() + ")");
            newRanges.remove(m);
        }

        // Strip whitespaces from replacement, as they are formatted for pretty print and copy.
        newRanges.stream().sorted(new MatchComparator()).forEach(match -> {
            if (match instanceof AcrolinxMatchWithReplacement) {
                String replacement = ((AcrolinxMatchWithReplacement) match).getReplacement();
                replacement = replacement.replaceAll("[\t\n\r]", " ");
                replacement = replacement.replaceAll("\\s+", " ");
                AcrolinxMatchWithReplacement acrolinxMatchWithReplacement = ((AcrolinxMatchWithReplacement) match).setReplacement(
                        replacement);
                newRanges.set(newRanges.indexOf(match), acrolinxMatchWithReplacement);
            }
        });

        return Optional.of(Collections.unmodifiableList(newRanges));
    }

    private void lookupMatchInContentNode(LookupForResolvedViewsHelper utils, List<? extends AbstractMatch> matches,
            String currentDocumentContent)
    {
        AtomicReference<String> nodeAsXML = new AtomicReference<>();
        AtomicReference<List<DiffMatchPatch.Diff>> diffs = new AtomicReference<>();
        AtomicReference<List<OffsetAlign>> offsetAligns = new AtomicReference<>();

        matches.stream().forEach(match -> {
            logger.debug("Mapping range for: " + match.getRange().toString() + ", (" + match.getContent() + ")");
            ContentNode contentNode = utils.getContentNodeForOffsetInCurrentDocument(
                    match.getRange().getMinimumInteger());
            if (contentNode != null && contentNode.getContent() != null) {
                int startOffset = contentNode.getStartOffset();
                logger.debug("Lookup in node: " + contentNode.getContent());
                logger.debug("Get node startOffset: " + contentNode.getStartOffset());
                String textContent = contentNode.getContent();
                String rangeContent = match.getContent();
                if (StringUtils.countMatches(textContent, rangeContent) == 1) {
                    int i = textContent.indexOf(rangeContent);
                    AbstractMatch copy = match.setRange(
                            new IntRange(startOffset + i, startOffset + i + rangeContent.length()));
                    logger.debug("Found range for content by matching Strings: " + match.getContent());
                    logger.debug("New range at: " + copy.getRange().toString());
                    newRanges.add(copy);
                    mappedRanges.add(match);
                } else if (contentNode.getAsXMLFragment() != null) {
                    // Try to lookup xml fragment in document.
                    if (!contentNode.getAsXMLFragment().equalsIgnoreCase(nodeAsXML.get())) {
                        nodeAsXML.set(contentNode.getAsXMLFragment());
                        diffs.set(Lookup.getDiffs(currentDocumentContent, contentNode.getAsXMLFragment()));
                        offsetAligns.set(Lookup.createOffsetMappingArray(diffs.get()));
                    }
                    Optional<IntRange> correctedMatch = Lookup.getCorrectedMatch(diffs.get(), offsetAligns.get(),
                            match.getRange().getMinimumInteger(), match.getRange().getMaximumInteger());
                    // Diff xml fragment with node content fragment.
                    correctedMatch.ifPresent(range -> {
                        List<DiffMatchPatch.Diff> diffsNode = Lookup.getDiffs(contentNode.getAsXMLFragment(),
                                textContent);
                        List<OffsetAlign> offsetMappingArray = Lookup.createOffsetMappingArray(diffsNode);
                        Optional<IntRange> finalMatch = Lookup.getCorrectedMatch(diffsNode, offsetMappingArray,
                                range.getMinimumInteger(), range.getMaximumInteger());
                        finalMatch.ifPresent(rangeFinal -> {
                            AbstractMatch copy = match.setRange(
                                    new IntRange(startOffset + rangeFinal.getMinimumInteger(),
                                            startOffset + rangeFinal.getMaximumInteger()));
                            logger.debug("Found range for content by diffing content nodes: " + match.getContent());
                            logger.debug("New range at: " + copy.getRange().toString());
                            newRanges.add(copy);
                            mappedRanges.add(match);
                        });
                    });
                }
            }
        });
    }

    private void getMatchesWithCorrectedRangesIgnoreNonMatched(List<DiffMatchPatch.Diff> diffs,
            List<OffsetAlign> offsetMappingArray, List<? extends AbstractMatch> matches)
    {
        matches.stream().forEach(match -> {
            logger.debug("Mapping range for by diffing editors: " + match.getRange().toString() + ", ("
                    + match.getContent() + ")");
            Optional<IntRange> correctedMatch = Lookup.getCorrectedMatch(diffs, offsetMappingArray,
                    match.getRange().getMinimumInteger(), match.getRange().getMaximumInteger());
            if (correctedMatch.isPresent()) {
                AbstractMatch copy = match.setRange(correctedMatch.get());
                logger.debug("Found range for content by diffing content nodes: " + match.getContent());
                logger.debug("New range at: " + copy.getRange().toString());
                newRanges.add(copy);
                mappedRanges.add(match);
            }
        });
    }
}
