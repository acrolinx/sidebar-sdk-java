/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.settings;

import com.google.gson.Gson;

public class CheckSettings {
  private final String language;
  private final String ruleSetName;
  private final String[] termSets;
  private final Boolean checkSpelling;
  private final Boolean checkGrammar;
  private final Boolean checkStyle;
  private final Boolean checkReuse;
  private final Boolean harvestTerms;
  private final Boolean checkSeo;
  private final String[] termStatuses;

  /**
   * Settings used by the Acrolinx Server to check the text. The available options depend on the
   * configuration of the Acrolinx Server.
   */
  public CheckSettings(
      String language,
      String ruleSetName,
      String[] termSets,
      Boolean checkSpelling,
      Boolean checkGrammar,
      Boolean checkStyle,
      Boolean checkReuse,
      Boolean harvestTerms,
      Boolean checkSeo,
      String[] termStatuses) {
    this.language = language;
    this.ruleSetName = ruleSetName;
    this.termSets = termSets.clone();
    this.checkSpelling = checkSpelling;
    this.checkGrammar = checkGrammar;
    this.checkStyle = checkStyle;
    this.checkReuse = checkReuse;
    this.harvestTerms = harvestTerms;
    this.checkSeo = checkSeo;
    this.termStatuses = termStatuses.clone();
  }

  @Override
  public String toString() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }
}
