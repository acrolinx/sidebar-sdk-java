/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.jfx;

import com.acrolinx.sidebar.pojo.SidebarError;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatch;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatchWithReplacement;
import com.acrolinx.sidebar.pojo.document.CheckResult;
import com.acrolinx.sidebar.pojo.document.CheckedDocumentPart;
import com.acrolinx.sidebar.pojo.document.IntRange;
import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContentMatch;
import com.acrolinx.sidebar.pojo.settings.AcrolinxPluginConfiguration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import netscape.javascript.JSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class JSToJavaConverter {
  static final Logger logger = LoggerFactory.getLogger(JSToJavaConverter.class);
  static final String LENGTH = "length";
  static final String RANGE = "range";
  static final String CONTENT = "content";
  static final String REPLACEMENT = "replacement";
  static final String CHECKED_PART = "checkedPart";
  static final String CHECK_ID = "checkId";
  static final String EMBED_CHECK_INFORMATION = "embedCheckInformation";
  static final String MESSAGE = "message";
  static final String ERROR = "error";
  static final String UNDEFINED = "undefined";
  static final String INPUT_FORMAT = "inputFormat";
  static final String BASE_64_ENCODED_GZIPPED_DOCUMENT_CONTENT =
      "base64EncodedGzippedDocumentContent";
  static final String CODE = "code";
  static final String SUPPORTED = "supported";
  static final String KEY = "key";
  static final String VALUE = "value";
  static final String EXTERNAL_CONTENT_MATCHES = "externalContentMatches";
  static final String ID = "id";
  static final String TYPE = "type";
  static final String ORIGINAL_BEGIN = "originalBegin";
  static final String ORIGINAL_END = "originalEnd";

  private JSToJavaConverter() {
    throw new IllegalStateException();
  }

  static List<AcrolinxMatch> getAcrolinxMatchFromJSObject(final JSObject jsObject) {
    final String length = "" + jsObject.getMember(LENGTH);
    final List<AcrolinxMatch> acrolinxMatches = new ArrayList<>();

    for (int i = 0; i < Integer.parseInt(length); i++) {
      final IntRange intRange =
          getIntRangeFromJSString(((JSObject) jsObject.getSlot(i)).getMember(RANGE).toString());
      final String surface = "" + ((JSObject) jsObject.getSlot(i)).getMember(CONTENT);

      if (((JSObject) jsObject.getSlot(0))
          .getMember(EXTERNAL_CONTENT_MATCHES)
          .toString()
          .equals(UNDEFINED)) {
        acrolinxMatches.add(new AcrolinxMatch(intRange, surface));
      } else {
        final JSObject externalContentMatches =
            (JSObject) ((JSObject) jsObject.getSlot(i)).getMember(EXTERNAL_CONTENT_MATCHES);
        acrolinxMatches.add(
            new AcrolinxMatch(
                intRange, surface, getExternalContentMatchFromJSObject(externalContentMatches)));
      }
    }

    return Collections.unmodifiableList(acrolinxMatches);
  }

  static List<AcrolinxMatchWithReplacement> getAcrolinxMatchWithReplacementFromJSObject(
      final JSObject jsObject) {
    final String length = "" + jsObject.getMember(LENGTH);
    final List<AcrolinxMatchWithReplacement> acrolinxMatches = new ArrayList<>();

    for (int i = 0; i < Integer.parseInt(length); i++) {
      final IntRange intRange =
          getIntRangeFromJSString(((JSObject) jsObject.getSlot(i)).getMember(RANGE).toString());
      final String surface = "" + ((JSObject) jsObject.getSlot(i)).getMember(CONTENT);
      final String replacement = "" + ((JSObject) jsObject.getSlot(i)).getMember(REPLACEMENT);

      if (((JSObject) jsObject.getSlot(0))
          .getMember(EXTERNAL_CONTENT_MATCHES)
          .toString()
          .equals(UNDEFINED)) {
        acrolinxMatches.add(new AcrolinxMatchWithReplacement(surface, intRange, replacement));
      } else {
        final JSObject externalContentMatches =
            (JSObject) ((JSObject) jsObject.getSlot(i)).getMember(EXTERNAL_CONTENT_MATCHES);
        acrolinxMatches.add(
            new AcrolinxMatchWithReplacement(
                surface,
                intRange,
                replacement,
                getExternalContentMatchFromJSObject(externalContentMatches)));
      }
    }

    return Collections.unmodifiableList(acrolinxMatches);
  }

  private static IntRange getIntRangeFromJSString(final String range) {
    final String[] parts = range.split(",");

    if (parts.length == 2) {
      return new IntRange(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    return null;
  }

  static CheckResult getCheckResultFromJSObject(final JSObject jsObject) {
    final JSObject checkedDocumentParts = (JSObject) jsObject.getMember(CHECKED_PART);
    final String checkId = checkedDocumentParts.getMember(CHECK_ID).toString();
    final IntRange intRange =
        getIntRangeFromJSString(checkedDocumentParts.getMember(RANGE).toString());
    String inputFormat = null;
    final Object checkError = jsObject.getMember(ERROR);

    if ((checkError != null) && !checkError.toString().equals(UNDEFINED)) {
      logger.warn(((JSObject) checkError).getMember(MESSAGE).toString());
      return null;
    }

    Map<String, String> embedCheckInformation = null;
    final Object checkInformation = jsObject.getMember(EMBED_CHECK_INFORMATION);

    if ((checkInformation != null) && !checkInformation.toString().equals(UNDEFINED)) {
      embedCheckInformation = getEmbedCheckInformationFromJSString((JSObject) checkInformation);
    }

    final Object inputFormatString = jsObject.getMember(INPUT_FORMAT);

    if ((inputFormatString != null) && !inputFormatString.toString().equals(UNDEFINED)) {
      inputFormat = inputFormatString.toString();
    }

    return new CheckResult(
        new CheckedDocumentPart(checkId, intRange), embedCheckInformation, inputFormat);
  }

  private static Map<String, String> getEmbedCheckInformationFromJSString(
      final JSObject embedCheckInformation) {
    final String length = "" + embedCheckInformation.getMember(LENGTH);
    final Map<String, String> map = new LinkedHashMap<>();

    for (int i = 0; i < Integer.parseInt(length); i++) {
      final JSObject slot = (JSObject) embedCheckInformation.getSlot(i);
      final String key = slot.getMember(KEY).toString();
      final String value = slot.getMember(VALUE).toString();
      map.put(key, value);
    }

    return map;
  }

  static AcrolinxPluginConfiguration getAcrolinxPluginConfigurationFromJSObject(
      final JSObject jsObject) {
    final JSObject pluginConf = (JSObject) jsObject.getMember(SUPPORTED);

    if (pluginConf != null) {
      final boolean isBase64EncodedGzippedDocumentContent =
          (Boolean) pluginConf.getMember(BASE_64_ENCODED_GZIPPED_DOCUMENT_CONTENT);
      return new AcrolinxPluginConfiguration(isBase64EncodedGzippedDocumentContent);
    }

    return new AcrolinxPluginConfiguration(false);
  }

  static Optional<SidebarError> getAcrolinxInitResultFromJSObject(final JSObject jsObject) {
    final Object hasError = jsObject.getMember(ERROR);

    if ((hasError != null) && !hasError.toString().equals(UNDEFINED)) {
      final JSObject error = (JSObject) hasError;
      final String code = error.getMember(CODE).toString();
      final String message = error.getMember(MESSAGE).toString();
      return Optional.of(new SidebarError(message, code));
    }

    return Optional.empty();
  }

  static List<ExternalContentMatch> getExternalContentMatchFromJSObject(final JSObject jsObject) {
    final String length = "" + jsObject.getMember(LENGTH);
    final List<ExternalContentMatch> externalContentMatchesList = new ArrayList<>();
    List<ExternalContentMatch> nestedExternalContentMatches = new ArrayList<>();

    for (int i = 0; i < Integer.parseInt(length); i++) {
      JSObject slotI = (JSObject) jsObject.getSlot(i);

      final String id = "" + slotI.getMember(ID);
      final String type = "" + slotI.getMember(TYPE);
      final int originalBegin = Integer.parseInt((slotI.getMember(ORIGINAL_BEGIN).toString()));
      final int originalEnd = Integer.parseInt((slotI.getMember(ORIGINAL_END).toString()));

      final Object externalContentMatchesJSObj = slotI.getMember(EXTERNAL_CONTENT_MATCHES);

      if (!(externalContentMatchesJSObj.equals(UNDEFINED))
          && Integer.parseInt(((JSObject) externalContentMatchesJSObj).getMember(LENGTH).toString())
              > 0) {
        nestedExternalContentMatches =
            getExternalContentMatchFromJSObject((JSObject) externalContentMatchesJSObj);
      }

      externalContentMatchesList.add(
          new ExternalContentMatch(
              id, type, originalBegin, originalEnd, nestedExternalContentMatches));
    }

    return Collections.unmodifiableList(externalContentMatchesList);
  }
}
