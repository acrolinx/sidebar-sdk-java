/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.document;

import java.io.Serializable;

public class IntRange implements Serializable {
  private static final long serialVersionUID = -2808883234003742075L;

  private final int min;
  private final int max;

  public IntRange(final int number1, final int number2) {
    if (number2 < number1) {
      this.min = number2;
      this.max = number1;
    } else {
      this.min = number1;
      this.max = number2;
    }
  }

  public int getMinimumInteger() {
    return min;
  }

  public int getMaximumInteger() {
    return max;
  }

  @Override
  public String toString() {
    return new StringBuilder(32)
        .append("Range[")
        .append(getMinimumInteger())
        .append(',')
        .append(getMaximumInteger())
        .append(']')
        .toString();
  }
}
