/*
 * Copyright (c) 2016-2017 Acrolinx GmbH
 */

package com.acrolinx.sidebar.pojo.document;

import java.util.HashMap;

@SuppressWarnings("unused")
public class CheckResultFromJSON
{
    private CheckedDocumentPartFromJSON checkedPart;
    private CheckError error;
    @SuppressWarnings("MismatchedReadAndWriteOfArray")
    private CheckInformationKeyValuePairFromJSON[] embedCheckInformation;
    private String inputFormat;

    CheckResultFromJSON()
    {
        //
    }

    public CheckResult getAsCheckResult()
    {
        return new CheckResult(checkedPart.getAsCheckResult(), error, getEmbedCheckInformation(), inputFormat);
    }

    private HashMap<String, String> getEmbedCheckInformation()
    {
        HashMap<String, String> map = new HashMap<>();
        if (embedCheckInformation == null) {
            return null;
        }
        for (CheckInformationKeyValuePairFromJSON checkInformationKeyValuePairFromJSON : embedCheckInformation) {
            map.put(checkInformationKeyValuePairFromJSON.getKey(), checkInformationKeyValuePairFromJSON.getValue());
        }
        return map;
    }
}
