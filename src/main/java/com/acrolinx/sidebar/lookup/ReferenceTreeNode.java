/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.lookup;

import java.util.ArrayList;
import java.util.List;

public class ReferenceTreeNode {
  private String referencingTag = "";
  private String unresolvedContent = "";
  private String resolvedContent = "";
  private int startOffsetInParent = 0;
  public final List<ReferenceTreeNode> referenceChildren = new ArrayList<>();

  public ReferenceTreeNode() {
    this("");
  }

  public ReferenceTreeNode(String resolvedContent) {
    this(resolvedContent, resolvedContent);
  }

  public ReferenceTreeNode(String resolvedContent, String unresolvedContent) {
    this(resolvedContent, unresolvedContent, "");
  }

  public ReferenceTreeNode(
      String resolvedContent, String unresolvedContent, String referencingTag) {
    this(resolvedContent, unresolvedContent, referencingTag, 0);
  }

  public ReferenceTreeNode(
      String resolvedContent,
      String unresolvedContent,
      String referencingTag,
      int startOffsetInParent) {
    this.resolvedContent = resolvedContent;
    this.unresolvedContent = unresolvedContent;
    this.referencingTag = referencingTag;
    this.startOffsetInParent = startOffsetInParent;
  }

  public void setStartOffsetInParent(int newStartOffset) {
    startOffsetInParent = newStartOffset;
  }

  public int getStartOffsetInParent() {
    return startOffsetInParent;
  }

  public void setUnresolvedContent(String newString) {
    unresolvedContent = newString;
  }

  public void setResolvedContent(String newString) {
    resolvedContent = newString;
  }

  public String getReferencingTag() {
    return referencingTag;
  }

  public void setReferencingTag(String newString) {
    referencingTag = newString;
  }

  public String getUnresolvedContent() {
    return unresolvedContent;
  }

  public String getResolvedContent() {
    return resolvedContent;
  }

  public int getUnresolvedLength() {
    return unresolvedContent.length();
  }

  public int getResolvedLength() {
    return resolvedContent.length();
  }
}
