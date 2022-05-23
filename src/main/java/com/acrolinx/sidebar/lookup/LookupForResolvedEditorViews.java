/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.lookup;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import com.acrolinx.sidebar.pojo.document.AcrolinxMatch;
import com.acrolinx.sidebar.pojo.document.externalContent.ExternalContentMatch;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
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
    private final String whitespaceCharacter = "\\u0000";

    public Optional<List<? extends AbstractMatch>> matchRangesForResolvedEditorView(
            List<? extends AbstractMatch> adjustedRangesForCurrentDocument, String currentDocumentContent,
            String resolvedViewContent, LookupForResolvedViewsHelper utils)
    {

        AtomicReference<List<AbstractMatch>> adjustedRangesCopyRef = new AtomicReference<>(new ArrayList<>());

        adjustedRangesForCurrentDocument.forEach(match -> {
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
            // LookupMatches by finding occurrence only for matches greater than one character
            getMatchesByOccurrence(adjustedRangesCopy, currentDocumentContent, resolvedViewContent);
            adjustedRangesCopy = adjustedRangesCopy.stream().filter(match -> !mappedRanges.contains(match)).collect(
                    Collectors.toList());
        }

        if (adjustedRangesCopy.size() > 0) {
            // LookupMatches by diffing editor content and document content.
            List<DiffMatchPatch.Diff> diffs = Lookup.getDiffs(currentDocumentContent, resolvedViewContent);
            List<OffsetAlign> offsetMappingArray = Lookup.createOffsetMappingArray(diffs);
            getMatchesWithCorrectedRangesIgnoreNonMatched(diffs, offsetMappingArray, adjustedRangesCopy);
            adjustedRangesCopy = adjustedRangesCopy.stream().filter(match -> !mappedRanges.contains(match)).collect(
                    Collectors.toList());
        }

        if (adjustedRangesCopy.size() > 0) {
            List<AbstractMatch> filteredMatches = getCleanedMatchesWithoutIgnorableWhitespaces(adjustedRangesCopy);
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
        Set<AbstractMatch> matchSet = new TreeSet<>(new MatchComparator());
        List<AbstractMatch> duplicates = newRanges.stream().filter(m -> !matchSet.add(m)).collect(Collectors.toList());
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

    private List<AbstractMatch> getCleanedMatchesWithoutIgnorableWhitespaces(List<AbstractMatch> adjustedRangesCopy)
    {
        return adjustedRangesCopy.stream().filter(match -> {
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
            boolean matchLineBreakOrTab = match.getContent().matches("[\\n\\r\\t]+");
            logger.debug("match content is linebreak or tab? " + matchLineBreakOrTab);
            return (("".equals(match.getContent()) || match.getContent().equals(" ") || matchLineBreakOrTab) && ignore);
        }).collect(Collectors.toList());
    }

    private void lookupMatchInContentNode(LookupForResolvedViewsHelper utils, List<? extends AbstractMatch> matches,
            String currentDocumentContent)
    {
        AtomicReference<String> nodeAsXML = new AtomicReference<>();
        AtomicReference<List<DiffMatchPatch.Diff>> diffs = new AtomicReference<>();
        AtomicReference<List<OffsetAlign>> offsetAligns = new AtomicReference<>();

        matches.forEach(match -> {
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
                    logForDebugCurrentRanges(copy);
                    newRanges.add(copy);
                    mappedRanges.add(match);
                } else if (contentNode.getAsXMLFragment() != null) {
                    // Try to lookup xml fragment in document.
                    boolean hasExternalContentMatches = (match instanceof  AcrolinxMatch) && ((AcrolinxMatch) match).getExternalContentMatches() != null && ((AcrolinxMatch) match).getExternalContentMatches().size() > 0;
                    if(!hasExternalContentMatches) {
                        if (!contentNode.getAsXMLFragment().equalsIgnoreCase(nodeAsXML.get())) {
                            nodeAsXML.set(contentNode.getAsXMLFragment());
                            diffs.set(Lookup.getDiffs(currentDocumentContent, contentNode.getAsXMLFragment()));
                            offsetAligns.set(Lookup.createOffsetMappingArray(diffs.get()));
                        }
                        Optional<IntRange> correctedMatch = Lookup.getCorrectedMatch(diffs.get(), offsetAligns.get(),
                                match.getRange().getMinimumInteger(), match.getRange().getMaximumInteger());
                        // Diff xml fragment with node content fragment.

                        correctedMatch.ifPresent(range -> diffXMLFragmentWithNodeContentFragment(match, contentNode,
                                startOffset , textContent, rangeContent,range));
                    }
                    if(hasExternalContentMatches) {
                        ExternalContentMatch eC = ((AcrolinxMatch) match).getExternalContentMatches().get(0);

                        diffXMLFragmentWithNodeContentFragment(match, contentNode,
                                startOffset, textContent, rangeContent, eC.getRange());
                    }

                }
            }
        });
    }

    private void diffXMLFragmentWithNodeContentFragment(AbstractMatch match, ContentNode contentNode, int startOffset,
            String textContent, String rangeContent, IntRange range)
    {
        logger.debug(contentNode.getAsXMLFragment());
        logger.debug(textContent);
        List<DiffMatchPatch.Diff> diffsNode = Lookup.getDiffs(contentNode.getAsXMLFragment(), textContent);
        List<OffsetAlign> offsetMappingArray = Lookup.createOffsetMappingArray(diffsNode);

        String rangeContentEscaped = StringEscapeUtils.escapeXml(rangeContent);
        logger.debug("Range Content escaped:" + rangeContentEscaped);
        // Deal with HTML entity
        if (!rangeContent.equals(rangeContentEscaped) && match.getRange().getMaximumInteger()
                - match.getRange().getMinimumInteger() == rangeContentEscaped.length()) {
            findRangeForHTMLEntity(match, contentNode, startOffset, textContent, rangeContent, range,
                    rangeContentEscaped);
        } else {
            Optional<IntRange> finalMatch = Lookup.getCorrectedMatch(diffsNode, offsetMappingArray,
                    range.getMinimumInteger(), range.getMaximumInteger());
            finalMatch.ifPresent(rangeFinal -> addFoundRanges(match, startOffset, rangeFinal.getMinimumInteger(),
                    rangeFinal.getMaximumInteger(), "Found range for content by diffing content nodes: "));
        }
    }

    private void findRangeForHTMLEntity(AbstractMatch match, ContentNode contentNode, int startOffset,
            String textContent, String rangeContent, IntRange range, String rangeContentEscaped)
    {
        logger.debug("Has to find HTML entity " + rangeContentEscaped);
        String cleanedAndEscapedTextContent = StringEscapeUtils.escapeXml(textContent).replace(whitespaceCharacter, "");

        logger.debug("Cleaned and escaped Text Content:" + cleanedAndEscapedTextContent);

        List<DiffMatchPatch.Diff> diffsNodeForEntity = Lookup.getDiffs(contentNode.getAsXMLFragment(),
                cleanedAndEscapedTextContent);
        List<OffsetAlign> offsetMappingArrayForEntity = Lookup.createOffsetMappingArray(diffsNodeForEntity);

        Optional<Integer> diffOffsetPositionStart = Lookup.getDiffOffsetPositionStart(offsetMappingArrayForEntity,
                range.getMinimumInteger() - 1);
        diffOffsetPositionStart.ifPresent(value -> {
            logger.debug("Mapped to offset: " + value);
            logger.debug("range min in is: " + range.getMinimumInteger());
            if ((range.getMinimumInteger() + value) >= 0) {
                findRangeInResolvedText(match, contentNode, startOffset, textContent, rangeContent, range, value);
            }
        });
    }

    private void findRangeInResolvedText(AbstractMatch match, ContentNode contentNode, int startOffset,
            String textContent, String rangeContent, IntRange range, Integer value)
    {
        String textContentUpToMatch = contentNode.getAsXMLFragment().substring(0, range.getMinimumInteger());
        logger.debug("Text Content up to Match: " + textContentUpToMatch);
        String textContentUpToMatchUnescaped = StringEscapeUtils.unescapeXml(textContentUpToMatch);
        logger.debug("Text content up to match unescaped: " + textContentUpToMatchUnescaped);
        int entityDifference = textContentUpToMatch.length() - textContentUpToMatchUnescaped.length();
        logger.debug("Entity difference is: " + entityDifference);
        int offsetStart = range.getMinimumInteger() + value - entityDifference;
        String matchContent = textContent.substring(0, offsetStart + 1);
        logger.debug("Match Content: " + matchContent);
        int leadingWhiteSpaces = matchContent.length() - matchContent.replace(whitespaceCharacter, "").length();
        logger.debug("Leading whitespaces:  " + leadingWhiteSpaces);
        offsetStart += leadingWhiteSpaces;
        int differedNullOffset = 0;
        while (textContent.substring(offsetStart + differedNullOffset, offsetStart + differedNullOffset + 1).matches(
                whitespaceCharacter)) {
            logger.debug("Offsets are null characters");
            differedNullOffset++;
        }
        logger.debug("Differed null characters:" + differedNullOffset);
        offsetStart += differedNullOffset;
        logger.debug("Recalculated start offset: " + offsetStart);
        int offsetEnd = offsetStart + 1;
        logger.debug("Recalculated end offset: " + offsetEnd);
        logger.debug("Text Content : " + textContent.substring(offsetStart, offsetEnd));
        if (textContent.substring(offsetStart, offsetEnd).equals(rangeContent)) {
            addFoundRanges(match, startOffset, offsetStart, offsetEnd,
                    "Found range for html entity content by diffing content nodes: ");

        }
    }

    private void addFoundRanges(AbstractMatch match, int startOffset, int offsetStart, int offsetEnd,
            String debugMessage)
    {
        AbstractMatch copy = match.setRange(new IntRange(startOffset + offsetStart, startOffset + offsetEnd));
        logger.debug(debugMessage + match.getContent());
        logForDebugCurrentRanges(copy);
        newRanges.add(copy);
        mappedRanges.add(match);
    }

    private void getMatchesWithCorrectedRangesIgnoreNonMatched(List<DiffMatchPatch.Diff> diffs,
            List<OffsetAlign> offsetMappingArray, List<? extends AbstractMatch> matches)
    {
        matches.forEach(match -> {
            logger.debug("Mapping range for by diffing editors: " + match.getRange().toString() + ", ("
                    + match.getContent() + ")");
            Optional<IntRange> correctedMatch = Lookup.getCorrectedMatch(diffs, offsetMappingArray,
                    match.getRange().getMinimumInteger(), match.getRange().getMaximumInteger());
            if (correctedMatch.isPresent()) {
                AbstractMatch copy = match.setRange(correctedMatch.get());
                logForDebugFoundContent(match);
                logForDebugCurrentRanges(copy);
                newRanges.add(copy);
                mappedRanges.add(match);
            }
        });
    }

    private void logForDebugFoundContent(AbstractMatch match)
    {
        logger.debug("Found range for content by diffing content nodes: " + match.getContent());
    }

    private void getMatchesByOccurrence(List<? extends AbstractMatch> matches, String currentDocumentContent,
            String resolvedViewContent)
    {
        matches.forEach(match -> {
            if (match.getContent().length() > 1) {
                String contentUpToMatch = currentDocumentContent.substring(0,
                        match.getRange().getMaximumInteger()).replaceAll("</?\\w+.*?>", "");
                int occurrence = StringUtils.countMatches(contentUpToMatch, match.getContent());
                int ordinalIndex = StringUtils.ordinalIndexOf(resolvedViewContent, match.getContent(), occurrence);

                if (ordinalIndex > 0) {
                    AbstractMatch copy = match.setRange(
                            new IntRange(ordinalIndex, ordinalIndex + match.getContent().length()));
                    logForDebugFoundContent(match);
                    logForDebugCurrentRanges(copy);
                    newRanges.add(copy);
                    mappedRanges.add(match);
                }
            }
        });
    }

    private void logForDebugCurrentRanges(AbstractMatch copy)
    {
        logger.debug("New range at: " + copy.getRange().toString());
    }

}
