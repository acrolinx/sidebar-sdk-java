/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.document.externalcontent;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExternalContentMatchTest {
  @Test
  void toStringTest() {
    ExternalContentMatch externalContentMatch =
        new ExternalContentMatch("foo", "bar", 0, 1, List.of());

    Assertions.assertEquals(
        "{\"id\":\"foo\",\"type\":\"bar\",\"originalBegin\":0,\"originalEnd\":1,\"externalContentMatches\":[]}",
        externalContentMatch.toString());
  }
}
