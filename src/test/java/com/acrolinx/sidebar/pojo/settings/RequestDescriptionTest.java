/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.settings;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class RequestDescriptionTest {
  @Test
  void toStringTest() {
    RequestDescription requestDescription = new RequestDescription("foo");

    Assertions.assertEquals("{\"documentReference\":\"foo\"}", requestDescription.toString());
  }
}
