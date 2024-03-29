/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.lookup;

public class OffsetAlign {
  private final int oldPosition;
  private final int diffOffset;

  public OffsetAlign(int oldPosition, int diffOffset) {
    this.oldPosition = oldPosition;
    this.diffOffset = diffOffset;
  }

  public int getOldPosition() {
    return oldPosition;
  }

  public int getDiffOffset() {
    return diffOffset;
  }
}
