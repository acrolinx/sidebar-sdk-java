/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.document;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CheckResultTest {
  @Test
  void testConvertToProcessingInstructionXML() {
    final Map<String, String> map = new LinkedHashMap<>();
    map.put("score", "67");
    map.put("TimeStarted", "=2017-08-09T10:39:39Z");
    map.put("scorecardURL", "https://test-ssl.acrolinx.com/output/en/something_report.html");
    map.put("status", "yellow");
    final String inputFormat = "xml";
    final CheckResult checkResult =
        new CheckResult(new CheckedDocumentPart("id", new IntRange(1, 1)), map, inputFormat);
    final String embedCheckDataAsProcessingInstruction =
        checkResult.getEmbedCheckDataAsEmbeddableString().get();
    Assertions.assertEquals(
        "<?acrolinxCheckData score=\"67\" TimeStarted=\"=2017-08-09T10:39:39Z\" scorecardURL=\"https://test-ssl.acrolinx.com/output/en/something_report.html\" status=\"yellow\" ?>",
        embedCheckDataAsProcessingInstruction);
  }

  @Test
  void testConvertToProcessingInstructionHTML() {
    final Map<String, String> map = new LinkedHashMap<>();
    map.put("TimeStarted", "=2017-08-09T10:39:39Z");
    map.put("scorecardURL", "https://test-ssl.acrolinx.com/output/en/something_report.html");
    map.put("status", "yellow");
    map.put("score", "67");
    final String inputFormat = "html";
    final CheckResult checkResult =
        new CheckResult(new CheckedDocumentPart("id", new IntRange(1, 1)), map, inputFormat);
    final String embedCheckDataAsProcessingInstruction =
        checkResult.getEmbedCheckDataAsEmbeddableString().get();
    Assertions.assertEquals(
        "<meta name=\"acrolinxCheckData\" TimeStarted=\"=2017-08-09T10:39:39Z\" scorecardURL=\"https://test-ssl.acrolinx.com/output/en/something_report.html\" status=\"yellow\" score=\"67\" />",
        embedCheckDataAsProcessingInstruction);
  }

  @Test
  void testConvertToProcessingInstructionMarkdown() {
    final Map<String, String> map = new LinkedHashMap<>();
    map.put("score", "67");
    map.put("status", "yellow");
    map.put("TimeStarted", "=2017-08-09T10:39:39Z");
    map.put("scorecardURL", "https://test-ssl.acrolinx.com/output/en/something_report.html");
    final String inputFormat = "markdown";
    final CheckResult checkResult =
        new CheckResult(new CheckedDocumentPart("id", new IntRange(1, 1)), map, inputFormat);
    final String embedCheckDataAsProcessingInstruction =
        checkResult.getEmbedCheckDataAsEmbeddableString().get();
    Assertions.assertEquals(
        "<!-- acrolinxCheckData score=\"67\" status=\"yellow\" TimeStarted=\"=2017-08-09T10:39:39Z\" scorecardURL=\"https://test-ssl.acrolinx.com/output/en/something_report.html\" -->",
        embedCheckDataAsProcessingInstruction);
  }

  @Test
  void testConvertToProcessingInstructionWhenCheckDataIsNull() {
    final CheckResult checkResult =
        new CheckResult(new CheckedDocumentPart("id", new IntRange(1, 1)), null, null);
    final Optional<String> embedCheckDataAsProcessingInstruction =
        checkResult.getEmbedCheckDataAsEmbeddableString();
    Assertions.assertFalse(embedCheckDataAsProcessingInstruction.isPresent());
  }
}
