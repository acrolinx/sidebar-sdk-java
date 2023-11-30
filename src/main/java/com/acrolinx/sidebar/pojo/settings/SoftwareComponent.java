/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.settings;

import com.google.gson.Gson;

public class SoftwareComponent {
  private final String id;
  private final String name;
  private final String version;
  private SoftwareComponentCategory category;

  public SoftwareComponent(String id, String name, String version) {
    this.id = id;
    this.name = name;
    this.version = version;
  }

  public SoftwareComponent(
      String id, String name, String version, SoftwareComponentCategory softwareComponentCategory) {
    this.id = id;
    this.name = name;
    this.version = version;
    this.category = softwareComponentCategory;
  }

  @Override
  public String toString() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }
}
