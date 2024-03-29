/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.document;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContentMatch;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CheckedDocumentPartTest {
  @Test
  void getAsJSTest() {
    CheckedDocumentPart checkedDocumentPart = new CheckedDocumentPart("id0", new IntRange(2, 3));
    assertEquals("{checkId: \"id0\", range:[2,3]}", checkedDocumentPart.getAsJS());

    ExternalContentMatch reference1 =
        new ExternalContentMatch("chapter11.dita", "ditaReferences", 75, 90);
    List<ExternalContentMatch> externalContentMatches = new ArrayList<>();
    externalContentMatches.add(
        new ExternalContentMatch(
            "overview.dita", "ditaReferences", 25, 35, new ArrayList<>(Arrays.asList(reference1))));

    CheckedDocumentPart partWithExternalContent =
        new CheckedDocumentPart("id0", new IntRange(2, 3), externalContentMatches);
    assertEquals(
        "{checkId: \"id0\", range:[2,3], externalContent:[{"
            + "\"id\":\"overview.dita\","
            + "\"type\":\"ditaReferences\",\"originalBegin\":25,\"originalEnd\":35,\"externalContentMatches\":[{\"id\":\"chapter11.dita\",\"type\":\"ditaReferences\",\"originalBegin\":75,\"originalEnd\":90,\"externalContentMatches\":[]}]}]}",
        partWithExternalContent.getAsJS());
  }

  @Test
  void toStringTest() {
    CheckedDocumentPart checkedDocumentPart = new CheckedDocumentPart("foo", new IntRange(0, 1));

    Assertions.assertEquals(
        "{\"checkId\":\"foo\",\"intRange\":{\"min\":0,\"max\":1}}", checkedDocumentPart.toString());
  }
}
