/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.settings;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BatchCheckRequestOptionsTest {
  @Test
  void toStringTest() {
    BatchCheckRequestOptions batchCheckRequestOptions = new BatchCheckRequestOptions("foo", "bar");

    Assertions.assertEquals(
        "{\"documentIdentifier\":\"foo\",\"displayName\":\"bar\"}",
        batchCheckRequestOptions.toString());
  }
}
