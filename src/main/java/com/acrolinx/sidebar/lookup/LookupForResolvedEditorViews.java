/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.lookup;

import com.acrolinx.sidebar.pojo.document.AbstractMatch;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatchWithReplacement;
import com.acrolinx.sidebar.pojo.document.ExternalAbstractMatch;
import com.acrolinx.sidebar.pojo.document.IntRange;
import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContentMatch;
import com.acrolinx.sidebar.utils.DiffMatchPatch;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LookupForResolvedEditorViews {
  private static final Logger logger = LoggerFactory.getLogger(LookupForResolvedEditorViews.class);
  private static final String WHITESPACE_CHARACTER = "\\u0000";

  private final List<AbstractMatch> mappedRanges = new ArrayList<>();
  private final List<AbstractMatch> newRanges = new ArrayList<>();

  public Optional<List<? extends AbstractMatch>> matchRangesForResolvedEditorView(
      List<? extends AbstractMatch> adjustedRangesForCurrentDocument,
      String currentDocumentContent,
      String resolvedViewContent,
      LookupForResolvedViewsHelper utils) {
    AtomicReference<List<AbstractMatch>> adjustedRangesCopyRef =
        new AtomicReference<>(new ArrayList<>());

    adjustedRangesForCurrentDocument.forEach(
        match -> {
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
      adjustedRangesCopy =
          adjustedRangesCopy.stream()
              .filter(match -> !mappedRanges.contains(match))
              .collect(Collectors.toList());
    }

    if (!adjustedRangesCopy.isEmpty()) {
      // LookupMatches by finding occurrence only for matches greater than one character
      getMatchesByOccurrence(adjustedRangesCopy, currentDocumentContent, resolvedViewContent);
      adjustedRangesCopy =
          adjustedRangesCopy.stream()
              .filter(match -> !mappedRanges.contains(match))
              .collect(Collectors.toList());
    }

    if (!adjustedRangesCopy.isEmpty()) {
      // LookupMatches by diffing editor content and document content.
      List<DiffMatchPatch.Diff> diffs =
          Lookup.getDiffs(currentDocumentContent, resolvedViewContent);
      List<OffsetAlign> offsetMappingArray = Lookup.createOffsetMappingArray(diffs);
      getMatchesWithCorrectedRangesIgnoreNonMatched(diffs, offsetMappingArray, adjustedRangesCopy);
      adjustedRangesCopy =
          adjustedRangesCopy.stream()
              .filter(match -> !mappedRanges.contains(match))
              .collect(Collectors.toList());
    }

    if (!adjustedRangesCopy.isEmpty()) {
      List<AbstractMatch> filteredMatches =
          getCleanedMatchesWithoutIgnorableWhitespaces(adjustedRangesCopy);
      adjustedRangesCopy =
          adjustedRangesCopy.stream()
              .filter(
                  match -> {
                    boolean containsMatch = !filteredMatches.contains(match);

                    if (!containsMatch) {
                      logger.debug(
                          "Removing non found match because contains deleted whitespaces.");
                      logger.debug("{} ({})", match.getRange(), match.getContent());
                    }

                    return containsMatch;
                  })
              .collect(Collectors.toList());
    }

    if (!adjustedRangesCopy.isEmpty()) {
      return Optional.empty();
    }

    // Find duplicates.
    Set<AbstractMatch> matchSet = new TreeSet<>(new MatchComparator());
    List<AbstractMatch> duplicates =
        newRanges.stream().filter(m -> !matchSet.add(m)).collect(Collectors.toList());

    for (AbstractMatch abstractMatch : duplicates) {
      logger.debug(
          "Removing duplicate matched range: {} ({})",
          abstractMatch.getRange(),
          abstractMatch.getContent());
      newRanges.remove(abstractMatch);
    }

    // Strip whitespaces from replacement, as they are formatted for pretty print and copy.
    newRanges.stream()
        .sorted(new MatchComparator())
        .forEach(
            match -> {
              if (match instanceof AcrolinxMatchWithReplacement) {
                String replacement = ((AcrolinxMatchWithReplacement) match).getReplacement();
                replacement = replacement.replaceAll("[\t\n\r]", " ");
                replacement = replacement.replaceAll("\\s+", " ");
                AcrolinxMatchWithReplacement acrolinxMatchWithReplacement =
                    ((AcrolinxMatchWithReplacement) match).setReplacement(replacement);
                newRanges.set(newRanges.indexOf(match), acrolinxMatchWithReplacement);
              }
            });

    return Optional.of(Collections.unmodifiableList(newRanges));
  }

  private static List<AbstractMatch> getCleanedMatchesWithoutIgnorableWhitespaces(
      List<AbstractMatch> adjustedRangesCopy) {
    return adjustedRangesCopy.stream()
        .filter(
            match -> {
              logger.debug("Filter for ignorable whitespaces");
              boolean ignore = true;

              if (match instanceof AcrolinxMatchWithReplacement) {
                logger.debug(((AcrolinxMatchWithReplacement) match).getReplacement());
                ignore = "".equals(((AcrolinxMatchWithReplacement) match).getReplacement());
                logger.debug("Replacement is empty? {}", ignore);
              }

              logger.debug(match.getContent());
              logger.debug(
                  "match content is space or empty? {}",
                  ("".equals(match.getContent()) || match.getContent().equalsIgnoreCase(" ")));
              boolean matchLineBreakOrTab = match.getContent().matches("[\\n\\r\\t]+");
              logger.debug("match content is linebreak or tab? {}", matchLineBreakOrTab);
              return (("".equals(match.getContent())
                      || match.getContent().equals(" ")
                      || matchLineBreakOrTab)
                  && ignore);
            })
        .collect(Collectors.toList());
  }

  private void lookupMatchInContentNode(
      LookupForResolvedViewsHelper utils,
      List<? extends AbstractMatch> matches,
      String currentDocumentContent) {
    AtomicReference<String> nodeAsXml = new AtomicReference<>();
    AtomicReference<String> oldRelativFragment = new AtomicReference<>();
    AtomicReference<List<DiffMatchPatch.Diff>> diffs = new AtomicReference<>();
    AtomicReference<List<OffsetAlign>> offsetAligns = new AtomicReference<>();

    matches.forEach(
        match -> {
          logger.debug("Mapping range for: {} ({})", match.getRange(), match.getContent());
          ContentNode contentNode =
              utils.getContentNodeForOffsetInCurrentDocument(match.getRange().getMinimumInteger());

          if (contentNode == null || contentNode.getContent() == null) {
            return;
          }

          int startOffset = contentNode.getStartOffset();
          logger.debug("Lookup in node: {}", contentNode.getContent());
          logger.debug("Get node startOffset: {}", contentNode.getStartOffset());
          String textContent = contentNode.getContent();
          String rangeContent = match.getContent();

          if (StringUtils.countMatches(textContent, rangeContent) == 1
              && (!(match instanceof ExternalAbstractMatch)
                  || !((ExternalAbstractMatch) match).hasExternalContentMatches()
                  || ((ExternalAbstractMatch) match).getExternalContentMatches().size() < 2)) {
            int i = textContent.indexOf(rangeContent);
            AbstractMatch copy =
                match.setRange(
                    new IntRange(startOffset + i, startOffset + i + rangeContent.length()));
            logger.debug("Found range for content by matching Strings: {}", match.getContent());
            logForDebugCurrentRanges(copy);
            newRanges.add(copy);
            mappedRanges.add(match);
          } else {
            String contentNodeXmlString;
            // Try to lookup xml fragment in document.
            Optional<IntRange> correctedMatchRange;

            if (!(match instanceof ExternalAbstractMatch)
                || !((ExternalAbstractMatch) match).hasExternalContentMatches()) {
              contentNodeXmlString = contentNode.getAsXMLFragment();

              if (contentNodeXmlString == null || contentNodeXmlString.equals("")) {
                return;
              }

              final int fragmentStartOffsetInCurrentDocument =
                  findFragmentStartOffsetInCurrentDocument(contentNodeXmlString, match);
              final int fragmentEndOffsetInCurrentDocument =
                  findFragmentEndOffsetInCurrentDocument(
                      contentNodeXmlString, match, currentDocumentContent);
              final String relativeFragment =
                  currentDocumentContent.substring(
                      fragmentStartOffsetInCurrentDocument, fragmentEndOffsetInCurrentDocument);

              if (!contentNodeXmlString.equalsIgnoreCase(nodeAsXml.get())
                  || !relativeFragment.equalsIgnoreCase(oldRelativFragment.get())) {
                oldRelativFragment.set(relativeFragment);
                nodeAsXml.set(contentNodeXmlString);
                diffs.set(Lookup.getDiffs(relativeFragment, nodeAsXml.get()));
                offsetAligns.set(Lookup.createOffsetMappingArray(diffs.get()));
              }

              correctedMatchRange =
                  Lookup.getCorrectedMatch(
                      diffs.get(),
                      offsetAligns.get(),
                      match.getRange().getMinimumInteger() - fragmentStartOffsetInCurrentDocument,
                      match.getRange().getMaximumInteger() - fragmentStartOffsetInCurrentDocument);
            } else {
              if (contentNode instanceof ExternalContentNode) {
                ReferenceTreeNode xmlTree = ((ExternalContentNode) contentNode).getReferenceTree();
                contentNodeXmlString = xmlTree.getResolvedContent();
                correctedMatchRange = calcCorrectedMatch((ExternalAbstractMatch) match, xmlTree);
              } else {
                logger.error(
                    "Match has external content, but ContentNode is not of type ExternalContentNode");
                return;
              }
            }

            correctedMatchRange.ifPresent(
                range ->
                    diffXMLFragmentWithNodeContentFragment(
                        match,
                        contentNodeXmlString,
                        startOffset,
                        textContent,
                        rangeContent,
                        range));
          }
        });
  }

  /**
   * Calcs Match offset as when the match and external content would just be part of the document
   */
  private static Optional<IntRange> calcCorrectedMatch(
      ExternalAbstractMatch match, ReferenceTreeNode xmlTree) {
    if (!match.hasExternalContentMatches()) {
      logger.error("calcCorrectedMatch was called despite match not having ExternalContentMatches");
      return Optional.empty();
    }

    if (xmlTree.referenceChildren.isEmpty()) {
      logger.warn("No reference Children while match has external ContentMatches");
      return Optional.empty();
    }

    ExternalContentMatch nextExternalContentMatch = match.getExternalContentMatches().get(0);
    IntRange externalContentRange = nextExternalContentMatch.getRange();
    int totalDelta = 0;
    ReferenceTreeNode referencedContentTreeNode = xmlTree.referenceChildren.get(0);

    while (referencedContentTreeNode != null && nextExternalContentMatch != null) {
      externalContentRange = nextExternalContentMatch.getRange();
      int externalContentRangeMinimum = externalContentRange.getMinimumInteger();
      List<ReferenceTreeNode> referenceChildren = referencedContentTreeNode.referenceChildren;

      for (int i = 0;
          i < referenceChildren.size()
              && externalContentRangeMinimum > referenceChildren.get(i).getStartOffsetInParent();
          i++) {
        ReferenceTreeNode child = referencedContentTreeNode.referenceChildren.get(i);
        int referenceTagLength = child.getReferencingTag().length();
        int resolvedContentLength = child.getResolvedContent().length();
        int delta = resolvedContentLength - referenceTagLength;
        totalDelta += delta;
      }

      totalDelta += externalContentRangeMinimum;

      if (nextExternalContentMatch.getExternalContentMatches().isEmpty()) {
        nextExternalContentMatch = null;
      } else {
        Optional<ReferenceTreeNode> optionalReferenceTreeNode =
            referenceChildren.stream()
                .filter(
                    referenceTreeNode ->
                        referenceTreeNode.getStartOffsetInParent() == externalContentRangeMinimum)
                .findFirst();

        if (optionalReferenceTreeNode.isPresent()) {
          referencedContentTreeNode = optionalReferenceTreeNode.get();
        } else {
          return Optional.empty();
        }

        nextExternalContentMatch = nextExternalContentMatch.getExternalContentMatches().get(0);
      }
    }

    return Optional.of(
        new IntRange(
            totalDelta,
            totalDelta
                + externalContentRange.getMaximumInteger()
                - externalContentRange.getMinimumInteger()));
  }

  private static int findFragmentStartOffsetInCurrentDocument(
      String contentNodeXmlString, AbstractMatch abstractMatch) {
    final int contentFragmentLength = contentNodeXmlString.length();
    final int matchStartOffset = abstractMatch.getRange().getMinimumInteger();

    if (contentFragmentLength < matchStartOffset) {
      return matchStartOffset - contentFragmentLength;
    }

    return 0;
  }

  private static int findFragmentEndOffsetInCurrentDocument(
      String contentNodeXmlString, AbstractMatch abstractMatch, String currentDocumentContent) {
    final int contentFragmentLength = contentNodeXmlString.length();
    final int matchEndOffset = abstractMatch.getRange().getMaximumInteger();

    if (currentDocumentContent.length() > (matchEndOffset + contentFragmentLength)) {
      return matchEndOffset + contentFragmentLength;
    }

    return currentDocumentContent.length();
  }

  private void diffXMLFragmentWithNodeContentFragment(
      AbstractMatch abstractMatch,
      String contentNodeXmlString,
      int startOffset,
      String textContent,
      String rangeContent,
      IntRange intRange) {
    logger.debug(contentNodeXmlString);
    logger.debug(textContent);
    textContent = textContent.replace("\u0000", " ");
    List<DiffMatchPatch.Diff> diffsNode = Lookup.getDiffs(contentNodeXmlString, textContent);
    List<OffsetAlign> offsetMappingArray = Lookup.createOffsetMappingArray(diffsNode);

    String rangeContentEscaped = StringEscapeUtils.escapeXml10(rangeContent);
    logger.debug("Range Content escaped: {}", rangeContentEscaped);
    // Deal with HTML entity
    if (!rangeContent.equals(rangeContentEscaped)
        && abstractMatch.getRange().getMaximumInteger()
                - abstractMatch.getRange().getMinimumInteger()
            == rangeContentEscaped.length()) {
      findRangeForHTMLEntity(
          abstractMatch,
          contentNodeXmlString,
          startOffset,
          textContent,
          rangeContent,
          intRange,
          rangeContentEscaped);
    } else {
      Optional<IntRange> finalMatch =
          Lookup.getCorrectedMatch(
              diffsNode,
              offsetMappingArray,
              intRange.getMinimumInteger(),
              intRange.getMaximumInteger());
      finalMatch.ifPresent(
          rangeFinal ->
              addFoundRanges(
                  abstractMatch,
                  startOffset,
                  rangeFinal.getMinimumInteger(),
                  rangeFinal.getMaximumInteger(),
                  "Found range for content by diffing content nodes: "));
    }
  }

  private void findRangeForHTMLEntity(
      AbstractMatch abstractMatch,
      String contentNodeXmlString,
      int startOffset,
      String textContent,
      String rangeContent,
      IntRange intRange,
      String rangeContentEscaped) {
    logger.debug("Has to find HTML entity: {}", rangeContentEscaped);
    String cleanedAndEscapedTextContent =
        StringEscapeUtils.escapeXml10(textContent).replace(WHITESPACE_CHARACTER, "");

    logger.debug("Cleaned and escaped Text Content: {}", cleanedAndEscapedTextContent);

    List<DiffMatchPatch.Diff> diffsNodeForEntity =
        Lookup.getDiffs(contentNodeXmlString, cleanedAndEscapedTextContent);
    List<OffsetAlign> offsetMappingArrayForEntity =
        Lookup.createOffsetMappingArray(diffsNodeForEntity);

    Optional<Integer> diffOffsetPositionStart =
        Lookup.getDiffOffsetPositionStart(
            offsetMappingArrayForEntity, intRange.getMinimumInteger() - 1);
    diffOffsetPositionStart.ifPresent(
        value -> {
          logger.debug("Mapped to offset: {}", value);
          logger.debug("range min in is: {}", intRange.getMinimumInteger());

          if ((intRange.getMinimumInteger() + value) >= 0) {
            findRangeInResolvedText(
                abstractMatch,
                contentNodeXmlString,
                startOffset,
                textContent,
                rangeContent,
                intRange,
                value);
          }
        });
  }

  private void findRangeInResolvedText(
      AbstractMatch abstractMatch,
      String contentNodeXmlString,
      int startOffset,
      String textContent,
      String rangeContent,
      IntRange intRange,
      Integer value) {
    String textContentUpToMatch = contentNodeXmlString.substring(0, intRange.getMinimumInteger());
    logger.debug("Text Content up to Match: {}", textContentUpToMatch);
    String textContentUpToMatchUnescaped = StringEscapeUtils.unescapeXml(textContentUpToMatch);
    logger.debug("Text content up to match unescaped: {}", textContentUpToMatchUnescaped);
    int entityDifference = textContentUpToMatch.length() - textContentUpToMatchUnescaped.length();
    logger.debug("Entity difference is: {}", entityDifference);
    int offsetStart = intRange.getMinimumInteger() + value - entityDifference;
    String matchContent = textContent.substring(0, offsetStart + 1);
    logger.debug("Match Content: {}", matchContent);
    int leadingWhiteSpaces =
        matchContent.length() - matchContent.replace(WHITESPACE_CHARACTER, "").length();
    logger.debug("Leading whitespaces: {}", leadingWhiteSpaces);
    offsetStart += leadingWhiteSpaces;
    int differedNullOffset = 0;

    while (textContent
        .substring(offsetStart + differedNullOffset, offsetStart + differedNullOffset + 1)
        .matches(WHITESPACE_CHARACTER)) {
      logger.debug("Offsets are null characters");
      differedNullOffset++;
    }

    logger.debug("Differed null characters: {}", differedNullOffset);
    offsetStart += differedNullOffset;
    logger.debug("Recalculated start offset: {}", offsetStart);
    int offsetEnd = offsetStart + 1;
    logger.debug("Recalculated end offset: {}", offsetEnd);
    logger.debug("Text Content: {}", textContent.substring(offsetStart, offsetEnd));

    if (textContent.substring(offsetStart, offsetEnd).equals(rangeContent)) {
      addFoundRanges(
          abstractMatch,
          startOffset,
          offsetStart,
          offsetEnd,
          "Found range for html entity content by diffing content nodes: ");
    }
  }

  private void addFoundRanges(
      AbstractMatch abstractMatch,
      int startOffset,
      int offsetStart,
      int offsetEnd,
      String debugMessage) {
    AbstractMatch copy =
        abstractMatch.setRange(new IntRange(startOffset + offsetStart, startOffset + offsetEnd));
    logger.debug("{} : {}", debugMessage, abstractMatch.getContent());
    logForDebugCurrentRanges(copy);
    newRanges.add(copy);
    mappedRanges.add(abstractMatch);
  }

  private void getMatchesWithCorrectedRangesIgnoreNonMatched(
      List<DiffMatchPatch.Diff> diffs,
      List<OffsetAlign> offsetMappingArray,
      List<? extends AbstractMatch> matches) {
    matches.forEach(
        match -> {
          logger.debug(
              "Mapping range for by diffing editors: {} ({})",
              match.getRange(),
              match.getContent());
          Optional<IntRange> correctedMatch =
              Lookup.getCorrectedMatch(
                  diffs,
                  offsetMappingArray,
                  match.getRange().getMinimumInteger(),
                  match.getRange().getMaximumInteger());

          if (correctedMatch.isPresent()) {
            AbstractMatch copy = match.setRange(correctedMatch.get());
            logForDebugFoundContent(match);
            logForDebugCurrentRanges(copy);
            newRanges.add(copy);
            mappedRanges.add(match);
          }
        });
  }

  private static void logForDebugFoundContent(AbstractMatch abstractMatch) {
    logger.debug(
        "Found range for content by diffing content nodes: {}", abstractMatch.getContent());
  }

  private void getMatchesByOccurrence(
      List<? extends AbstractMatch> abstractMatches,
      String currentDocumentContent,
      String resolvedViewContent) {
    abstractMatches.forEach(
        abstractMatch -> {
          if (abstractMatch.getContent().length() <= 1) {
            return;
          }

          if (abstractMatch instanceof ExternalAbstractMatch
              && ((ExternalAbstractMatch) abstractMatch).hasExternalContentMatches()) {
            return;
          }

          String contentUpToMatch =
              currentDocumentContent
                  .substring(0, abstractMatch.getRange().getMaximumInteger())
                  .replaceAll("</?\\w+.*?>", "");
          int occurrence = StringUtils.countMatches(contentUpToMatch, abstractMatch.getContent());
          int ordinalIndex =
              StringUtils.ordinalIndexOf(
                  resolvedViewContent, abstractMatch.getContent(), occurrence);

          if (ordinalIndex > 0) {
            AbstractMatch copy =
                abstractMatch.setRange(
                    new IntRange(ordinalIndex, ordinalIndex + abstractMatch.getContent().length()));
            logForDebugFoundContent(abstractMatch);
            logForDebugCurrentRanges(copy);
            newRanges.add(copy);
            mappedRanges.add(abstractMatch);
          }
        });
  }

  private static void logForDebugCurrentRanges(AbstractMatch abstractMatch) {
    logger.debug("New range at: {}", abstractMatch.getRange());
  }
}
