/* Copyright (c) 2016-present Acrolinx GmbH */

package com.acrolinx.sidebar.pojo.document;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acrolinx.sidebar.pojo.SidebarError;

@SuppressWarnings("unused")
public class CheckResultFromJSON
{
    private final Logger logger = LoggerFactory.getLogger(CheckResultFromJSON.class);

    private CheckedDocumentPartFromJSON checkedPart;
    private SidebarError error;
    @SuppressWarnings("MismatchedReadAndWriteOfArray")
    private CheckInformationKeyValuePairFromJSON[] embedCheckInformation;
    private String inputFormat;

    CheckResultFromJSON()
    {
        //
    }

    public CheckResult getAsCheckResult()
    {
        if (this.error != null) {
            logger.warn(error.getMessage());
            return null;
        }
        return new CheckResult(checkedPart.getAsCheckResult(), getEmbedCheckInformation(), inputFormat);
    }

    private Map<String, String> getEmbedCheckInformation()
    {
        final Map<String, String> map = new LinkedHashMap<>();
        if (embedCheckInformation == null) {
            return null;
        }
        for (final CheckInformationKeyValuePairFromJSON checkInformationKeyValuePairFromJSON : embedCheckInformation) {
            map.put(checkInformationKeyValuePairFromJSON.getKey(), checkInformationKeyValuePairFromJSON.getValue());
        }
        return map;
    }
}
