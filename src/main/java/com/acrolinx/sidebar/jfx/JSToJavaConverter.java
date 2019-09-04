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

    static List<AcrolinxMatch> getAcrolinxMatchFromJSObject(final JSObject o)
    {
        final String length = "" + o.getMember("length");
        final List<AcrolinxMatch> acrolinxMatches = Lists.newArrayList();
        for (int i = 0; i < Integer.parseInt(length); i++) {
            final IntRange range = getIntRangeFromJSString(((JSObject) o.getSlot(i)).getMember("range").toString());
            final String surface = "" + ((JSObject) o.getSlot(i)).getMember("content");
            acrolinxMatches.add(new AcrolinxMatch(range, surface));
        }
        return Collections.unmodifiableList(acrolinxMatches);
    }

    static List<AcrolinxMatchWithReplacement> getAcrolinxMatchWithReplacementFromJSObject(final JSObject o)
    {
        final String length = "" + o.getMember("length");
        final List<AcrolinxMatchWithReplacement> acrolinxMatches = Lists.newArrayList();
        for (int i = 0; i < Integer.parseInt(length); i++) {
            final IntRange range = getIntRangeFromJSString(((JSObject) o.getSlot(i)).getMember("range").toString());
            final String surface = "" + ((JSObject) o.getSlot(i)).getMember("content");
            final String replacement = "" + ((JSObject) o.getSlot(i)).getMember("replacement");
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
        final JSObject checkedDocumentParts = (JSObject) o.getMember("checkedPart");
        final String checkId = checkedDocumentParts.getMember("checkId").toString();
        final IntRange range = getIntRangeFromJSString(checkedDocumentParts.getMember("range").toString());
        String inputFormat = null;
        final Object checkError = o.getMember("error");
        if ((checkError != null) && !checkError.toString().equals("undefined")) {
            logger.warn(((JSObject) checkError).getMember("message").toString());
            return null;
        }
        Map<String, String> embedCheckInformation = null;
        final Object checkInformation = o.getMember("embedCheckInformation");
        if ((checkInformation != null) && !checkInformation.toString().equals("undefined")) {
            embedCheckInformation = getEmbedCheckInformationFromJSString((JSObject) checkInformation);
        }
        final Object inputFormatString = o.getMember("inputFormat");
        if ((inputFormatString != null) && !inputFormatString.toString().equals("undefined")) {
            inputFormat = inputFormatString.toString();
        }
        return new CheckResult(new CheckedDocumentPart(checkId, range), embedCheckInformation, inputFormat);
    }

    private static Map<String, String> getEmbedCheckInformationFromJSString(final JSObject embedCheckInformation)
    {
        final String length = "" + embedCheckInformation.getMember("length");
        final Map<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < Integer.parseInt(length); i++) {
            final JSObject slot = (JSObject) embedCheckInformation.getSlot(i);
            final String key = slot.getMember("key").toString();
            final String value = slot.getMember("value").toString();
            map.put(key, value);
        }
        return map;
    }

    static AcrolinxPluginConfiguration getAcrolinxPluginConfigurationFromJSObject(final JSObject o)
    {
        final JSObject pluginConf = (JSObject) o.getMember("supported");
        if (pluginConf != null) {
            final boolean isBase64EncodedGzippedDocumentContent = (Boolean) pluginConf.getMember(
                    "base64EncodedGzippedDocumentContent");
            return new AcrolinxPluginConfiguration(isBase64EncodedGzippedDocumentContent);
        }
        return new AcrolinxPluginConfiguration(false);
    }

    static Optional<SidebarError> getAcrolinxInitResultFromJSObject(final JSObject o)
    {
        final Object hasError = o.getMember("error");
        if ((hasError != null) && !hasError.toString().equals("undefined")) {
            final JSObject error = (JSObject) hasError;
            final String code = error.getMember("code").toString();
            final String message = error.getMember("message").toString();
            return Optional.of(new SidebarError(message, code));
        } else {
            return Optional.empty();
        }
    }
}
