/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.document;

import com.acrolinx.sidebar.pojo.SidebarError;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckResultFromJSON {
  private static final Logger logger = LoggerFactory.getLogger(CheckResultFromJSON.class);

  private CheckedDocumentPartFromJSON checkedPart;
  private SidebarError sidebarError;
  private CheckInformationKeyValuePairFromJSON[] embedCheckInformation;
  private String inputFormat;

  CheckResultFromJSON() {}

  public CheckResult getAsCheckResult() {
    if (this.sidebarError != null) {
      logger.warn("Message: {}, Code: {}", sidebarError.getMessage(), sidebarError.getErrorCode());
      return null;
    }

    return new CheckResult(checkedPart.getAsCheckResult(), getEmbedCheckInformation(), inputFormat);
  }

  private Map<String, String> getEmbedCheckInformation() {
    final Map<String, String> map = new LinkedHashMap<>();

    if (embedCheckInformation == null) {
      return new LinkedHashMap<>();
    }

    for (final CheckInformationKeyValuePairFromJSON checkInformationKeyValuePairFromJSON :
        embedCheckInformation) {
      map.put(
          checkInformationKeyValuePairFromJSON.getKey(),
          checkInformationKeyValuePairFromJSON.getValue());
    }

    return map;
  }
}
