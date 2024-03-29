/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.document;

import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContent;
import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContentBuilder;
import com.google.gson.Gson;

public class CheckContent {
  private String content;
  private ExternalContent externalContent;

  public CheckContent(String content, ExternalContent externalContent) {
    this.content = content;
    this.externalContent =
        externalContent == null ? new ExternalContentBuilder().build() : externalContent;
  }

  public String getContent() {
    return this.content;
  }

  public ExternalContent getExternalContent() {
    return this.externalContent;
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
