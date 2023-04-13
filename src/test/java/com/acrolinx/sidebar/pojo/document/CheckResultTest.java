/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.pojo.document;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

public class CheckResultTest
{
    @Test
    public void testConvertToProcessingInstructionXML()
    {
        final Map<String, String> map = new LinkedHashMap<>();
        map.put("score", "67");
        map.put("TimeStarted", "=2017-08-09T10:39:39Z");
        map.put("scorecardURL", "https://test-ssl.acrolinx.com/output/en/something_report.html");
        map.put("status", "yellow");
        final String inputFormat = "xml";
        final CheckResult result = new CheckResult(new CheckedDocumentPart("id", new IntRange(1, 1)), map, inputFormat);
        final String embedCheckDataAsProcessingInstruction = result.getEmbedCheckDataAsEmbeddableString().get();
        Assert.assertEquals(
                "<?acrolinxCheckData score=\"67\" TimeStarted=\"=2017-08-09T10:39:39Z\" scorecardURL=\"https://test-ssl.acrolinx.com/output/en/something_report.html\" status=\"yellow\" ?>",
                embedCheckDataAsProcessingInstruction);
    }

    @Test
    public void testConvertToProcessingInstructionHTML()
    {
        final Map<String, String> map = new LinkedHashMap<>();
        map.put("TimeStarted", "=2017-08-09T10:39:39Z");
        map.put("scorecardURL", "https://test-ssl.acrolinx.com/output/en/something_report.html");
        map.put("status", "yellow");
        map.put("score", "67");
        final String inputFormat = "html";
        final CheckResult result = new CheckResult(new CheckedDocumentPart("id", new IntRange(1, 1)), map, inputFormat);
        final String embedCheckDataAsProcessingInstruction = result.getEmbedCheckDataAsEmbeddableString().get();
        Assert.assertEquals(
                "<meta name=\"acrolinxCheckData\" TimeStarted=\"=2017-08-09T10:39:39Z\" scorecardURL=\"https://test-ssl.acrolinx.com/output/en/something_report.html\" status=\"yellow\" score=\"67\" />",
                embedCheckDataAsProcessingInstruction);
    }

    @Test
    public void testConvertToProcessingInstructionMarkdown()
    {
        final Map<String, String> map = new LinkedHashMap<>();
        map.put("score", "67");
        map.put("status", "yellow");
        map.put("TimeStarted", "=2017-08-09T10:39:39Z");
        map.put("scorecardURL", "https://test-ssl.acrolinx.com/output/en/something_report.html");
        final String inputFormat = "markdown";
        final CheckResult result = new CheckResult(new CheckedDocumentPart("id", new IntRange(1, 1)), map, inputFormat);
        final String embedCheckDataAsProcessingInstruction = result.getEmbedCheckDataAsEmbeddableString().get();
        Assert.assertEquals(
                "<!-- acrolinxCheckData score=\"67\" status=\"yellow\" TimeStarted=\"=2017-08-09T10:39:39Z\" scorecardURL=\"https://test-ssl.acrolinx.com/output/en/something_report.html\" -->",
                embedCheckDataAsProcessingInstruction);
    }

    @Test
    public void testConvertToProcessingInstructionWhenCheckDataIsNull()
    {
        final CheckResult result = new CheckResult(new CheckedDocumentPart("id", new IntRange(1, 1)), null, null);
        final Optional<String> embedCheckDataAsProcessingInstruction = result.getEmbedCheckDataAsEmbeddableString();
        Assert.assertFalse(embedCheckDataAsProcessingInstruction.isPresent());
    }
}