/* Copyright (c) 2016-present Acrolinx GmbH */

package com.acrolinx.sidebar.jfx;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acrolinx.sidebar.pojo.SidebarError;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatch;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatchWithReplacement;
import com.acrolinx.sidebar.pojo.document.CheckResult;
import com.acrolinx.sidebar.pojo.document.CheckedDocumentPart;
import com.acrolinx.sidebar.pojo.document.IntRange;
import com.acrolinx.sidebar.pojo.settings.AcrolinxPluginConfiguration;
import com.google.common.collect.Lists;

import netscape.javascript.JSObject;

@SuppressWarnings("unused")
class JSToJavaConverter
{
    static final Logger logger = LoggerFactory.getLogger(JSToJavaConverter.class);
    public static final String LENGTH = "length";
    public static final String RANGE = "range";
    public static final String CONTENT = "content";
    public static final String REPLACEMENT = "replacement";
    public static final String CHECKED_PART = "checkedPart";
    public static final String CHECK_ID = "checkId";
    public static final String EMBED_CHECK_INFORMATION = "embedCheckInformation";
    public static final String MESSAGE = "message";
    public static final String ERROR = "error";
    public static final String UNDEFINED = "undefined";
    public static final String INPUT_FORMAT = "inputFormat";
    public static final String BASE_64_ENCODED_GZIPPED_DOCUMENT_CONTENT = "base64EncodedGzippedDocumentContent";
    public static final String CODE = "code";
    public static final String SUPPORTED = "supported";
    public static final String KEY = "key";
    public static final String VALUE = "value";

    static List<AcrolinxMatch> getAcrolinxMatchFromJSObject(final JSObject o)
    {
        final String length = "" + o.getMember(LENGTH);
        final List<AcrolinxMatch> acrolinxMatches = Lists.newArrayList();
        for (int i = 0; i < Integer.parseInt(length); i++) {
            final IntRange range = getIntRangeFromJSString(((JSObject) o.getSlot(i)).getMember(RANGE).toString());
            final String surface = "" + ((JSObject) o.getSlot(i)).getMember(CONTENT);
            acrolinxMatches.add(new AcrolinxMatch(range, surface));
        }
        return Collections.unmodifiableList(acrolinxMatches);
    }

    static List<AcrolinxMatchWithReplacement> getAcrolinxMatchWithReplacementFromJSObject(final JSObject o)
    {
        final String length = "" + o.getMember(LENGTH);
        final List<AcrolinxMatchWithReplacement> acrolinxMatches = Lists.newArrayList();
        for (int i = 0; i < Integer.parseInt(length); i++) {
            final IntRange range = getIntRangeFromJSString(((JSObject) o.getSlot(i)).getMember(RANGE).toString());
            final String surface = "" + ((JSObject) o.getSlot(i)).getMember(CONTENT);
            final String replacement = "" + ((JSObject) o.getSlot(i)).getMember(REPLACEMENT);
            acrolinxMatches.add(new AcrolinxMatchWithReplacement(surface, range, replacement));
        }
        return Collections.unmodifiableList(acrolinxMatches);
    }

    private static IntRange getIntRangeFromJSString(final String range)
    {
        final String[] parts = range.split(",");
        IntRange intRange;
        if (parts.length == 2) {
            intRange = new IntRange(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        } else {
            intRange = null;
        }
        return intRange;
    }

    static CheckResult getCheckResultFromJSObject(final JSObject o)
    {
        final JSObject checkedDocumentParts = (JSObject) o.getMember(CHECKED_PART);
        final String checkId = checkedDocumentParts.getMember(CHECK_ID).toString();
        final IntRange range = getIntRangeFromJSString(checkedDocumentParts.getMember(RANGE).toString());
        String inputFormat = null;
        final Object checkError = o.getMember(ERROR);
        if ((checkError != null) && !checkError.toString().equals(UNDEFINED)) {
            logger.warn(((JSObject) checkError).getMember(MESSAGE).toString());
            return null;
        }
        Map<String, String> embedCheckInformation = null;
        final Object checkInformation = o.getMember(EMBED_CHECK_INFORMATION);
        if ((checkInformation != null) && !checkInformation.toString().equals(UNDEFINED)) {
            embedCheckInformation = getEmbedCheckInformationFromJSString((JSObject) checkInformation);
        }
        final Object inputFormatString = o.getMember(INPUT_FORMAT);
        if ((inputFormatString != null) && !inputFormatString.toString().equals(UNDEFINED)) {
            inputFormat = inputFormatString.toString();
        }
        return new CheckResult(new CheckedDocumentPart(checkId, range), embedCheckInformation, inputFormat);
    }

    private static Map<String, String> getEmbedCheckInformationFromJSString(final JSObject embedCheckInformation)
    {
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

    static AcrolinxPluginConfiguration getAcrolinxPluginConfigurationFromJSObject(final JSObject o)
    {
        final JSObject pluginConf = (JSObject) o.getMember(SUPPORTED);
        if (pluginConf != null) {
            final boolean isBase64EncodedGzippedDocumentContent = (Boolean) pluginConf.getMember(
                    BASE_64_ENCODED_GZIPPED_DOCUMENT_CONTENT);
            return new AcrolinxPluginConfiguration(isBase64EncodedGzippedDocumentContent);
        }
        return new AcrolinxPluginConfiguration(false);
    }

    static Optional<SidebarError> getAcrolinxInitResultFromJSObject(final JSObject o)
    {
        final Object hasError = o.getMember(ERROR);
        if ((hasError != null) && !hasError.toString().equals(UNDEFINED)) {
            final JSObject error = (JSObject) hasError;
            final String code = error.getMember(CODE).toString();
            final String message = error.getMember(MESSAGE).toString();
            return Optional.of(new SidebarError(message, code));
        } else {
            return Optional.empty();
        }
    }
}
