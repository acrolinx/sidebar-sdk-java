/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

import com.acrolinx.sidebar.pojo.document.AbstractMatch;
import com.acrolinx.sidebar.pojo.document.ExternalAbstractMatch;
import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContentField;
import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContentMatch;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MatchUtils {
  private static final Logger logger = LoggerFactory.getLogger(MatchUtils.class);

  private MatchUtils() {
    throw new IllegalStateException();
  }

  public static List<? extends AbstractMatch> filterDangerousMatches(
      List<? extends AbstractMatch> abstractMatches, String lastCheckedContent) {
    return filterDangerousMatches(abstractMatches, lastCheckedContent, new ArrayList<>());
  }

  public static List<? extends AbstractMatch> filterDangerousMatches(
      List<? extends AbstractMatch> abstractMatches,
      String lastCheckedContent,
      List<ExternalContentField> lastCheckedExternalContent) {
    return abstractMatches.stream()
        .filter(
            abstractMatch -> {
              // todo: does the external content contain tags that are not external content matches?
              if (abstractMatch instanceof ExternalAbstractMatch
                  && ((ExternalAbstractMatch) abstractMatch).hasExternalContentMatches()) {
                return isExternalContentMatchAXMLTag(
                    (ExternalAbstractMatch) abstractMatch, lastCheckedExternalContent);
              }

              String matchContent =
                  lastCheckedContent.substring(
                      abstractMatch.getRange().getMinimumInteger(),
                      abstractMatch.getRange().getMaximumInteger());
              logger.debug("Checking if match is a tag");

              boolean isTag = matchContent.matches("</?\\w+.*?>");
              logger.debug("Is match a tag: {}", isTag);
              return !isTag;
            })
        .collect(Collectors.toList());
  }

  public static boolean isExternalContentMatchAXMLTag(
      ExternalAbstractMatch externalAbstractMatch,
      List<ExternalContentField> lastCheckedExternalContent) {
    ExternalContentMatch externalContentMatch =
        externalAbstractMatch.getExternalContentMatches().get(0);

    while (!externalContentMatch.getExternalContentMatches().isEmpty()) {
      externalContentMatch = externalContentMatch.getExternalContentMatches().get(0);
    }

    final String externalContentId = externalContentMatch.getId();
    Optional<ExternalContentField> optionalExternalContentField =
        lastCheckedExternalContent.stream()
            .filter(externalContentField -> externalContentField.getId().equals(externalContentId))
            .findFirst();

    if (optionalExternalContentField.isEmpty()) {
      logger.warn("Field for externalContentMatch has not been found");
      return false;
    }

    ExternalContentField externalContentField = optionalExternalContentField.get();
    String content = externalContentField.getContent();
    String matchContent =
        content.substring(
            externalContentMatch.getRange().getMinimumInteger(),
            externalContentMatch.getRange().getMaximumInteger());
    return !matchContent.matches("</?\\w+.*?>");
  }

  public static <T extends AbstractMatch> List<T> sortByOffsetDesc(final List<T> matches) {
    final List<T> sortedMatches = new ArrayList<>(matches);
    sortedMatches.sort(
        (first, second) -> {
          final int start =
              first.getRange().getMinimumInteger() - second.getRange().getMinimumInteger();

          if (start == 0) {
            final int end =
                first.getRange().getMaximumInteger() - second.getRange().getMaximumInteger();

            if (end == 0) {
              return 0;
            }

            return (end > 0) ? -1 : +1;
          }

          return start > 0 ? -1 : +1;
        });

    return sortedMatches;
  }
}
