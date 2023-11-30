/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.document.externalcontent;

import com.google.gson.Gson;

public class ExternalContentField {
  private final String id;
  private String content;

  public ExternalContentField(String id, String content) {
    this.id = id;
    this.content = content;
  }

  public String getId() {
    return id;
  }

  public String getContent() {
    return content;
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }

  public void setContent(String content) {
    this.content = content;
  }
}
