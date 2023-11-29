/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.document.externalcontent;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/** Only supported with Acrolinx Platform 2019.10 (Sidebar version 14.16) and newer. */
public class ExternalContent {
  private final List<ExternalContentField> textReplacements;
  private final List<ExternalContentField> entities;
  private final List<ExternalContentField> ditaReferences;
  private final List<ExternalContentField> xincludeReferences;

  public ExternalContent(
      List<ExternalContentField> textReplacements,
      List<ExternalContentField> entities,
      List<ExternalContentField> ditaReferences,
      List<ExternalContentField> xincludeReferences) {
    this.textReplacements = textReplacements;
    this.entities = entities;
    this.ditaReferences = ditaReferences;
    this.xincludeReferences = xincludeReferences;
  }

  public List<ExternalContentField> getTextReplacements() {
    return textReplacements;
  }

  public List<ExternalContentField> getEntities() {
    return entities;
  }

  public List<ExternalContentField> getDitaReferences() {
    return ditaReferences;
  }

  public List<ExternalContentField> getXIncludeReferences() {
    return xincludeReferences;
  }

  public List<ExternalContentField> getAll() {
    List<ExternalContentField> flattenedList = new ArrayList<>();
    Stream.of(textReplacements, entities, ditaReferences, xincludeReferences)
        .forEach(flattenedList::addAll);
    return flattenedList;
  }

  public String getContentForReference(String reference) {
    String contentForReference = "";

    for (ExternalContentField externalContentField : ditaReferences) {
      if (externalContentField.getId().equals(reference)) {
        contentForReference = externalContentField.getContent();
      }
    }

    return contentForReference;
  }

  public void updateContentForReference(String reference, String newContent) {
    for (ExternalContentField externalContentField : ditaReferences) {
      if (externalContentField.getId().equals(reference)) {
        externalContentField.setContent(newContent);
      }
    }
  }

  @Override
  public String toString() {
    return new Gson().toJson(this);
  }
}
