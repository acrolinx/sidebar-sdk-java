/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.document.externalcontent;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExternalContentFieldTest {
  @Test
  void toStringTest() {
    ExternalContentField externalContentField = new ExternalContentField("foo", "bar");

    Assertions.assertEquals(
        "{\"id\":\"foo\",\"content\":\"bar\"}", externalContentField.toString());
  }

  @Test
  void toStringWithNullContentTest() {
    ExternalContentField externalContentField = new ExternalContentField("foo", null);

    Assertions.assertEquals("{\"id\":\"foo\"}", externalContentField.toString());
  }

  @Test
  void toStringWithNullIdTest() {
    ExternalContentField externalContentField = new ExternalContentField(null, "bar");

    Assertions.assertEquals("{\"content\":\"bar\"}", externalContentField.toString());
  }

  @Test
  void toStringWithNullIdAndContentTest() {
    ExternalContentField externalContentField = new ExternalContentField(null, null);

    Assertions.assertEquals("{}", externalContentField.toString());
  }
}
