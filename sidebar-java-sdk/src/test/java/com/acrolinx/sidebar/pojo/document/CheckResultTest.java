/* Copyright (c) 2017-2018 Acrolinx GmbH */


package com.acrolinx.sidebar.pojo.document;

import java.util.HashMap;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("OptionalGetWithoutIsPresent")
public class CheckResultTest
{
    @Test
    public void testConvertToProcessingInstructionXML()
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("TimeStarted", "=2017-08-09T10:39:39Z");
        map.put("score", "67");
        map.put("status", "yellow");
        map.put("scorecardURL", "https://test-ssl.acrolinx.com/output/en/something_report.html");
        String inputFormat = "xml";
        CheckResult result = new CheckResult(new CheckedDocumentPart("id", new IntRange(1, 1)), map, inputFormat);
        String embedCheckDataAsProcessingInstruction = result.getEmbedCheckDataAsEmbeddableString().get();
        Assert.assertTrue(embedCheckDataAsProcessingInstruction.equalsIgnoreCase(
                "<?acrolinxCheckData score=\"67\" TimeStarted=\"=2017-08-09T10:39:39Z\" scorecardURL=\"https://test-ssl.acrolinx.com/output/en/something_report.html\" status=\"yellow\" ?>"));
    }

    @Test
    public void testConvertToProcessingInstructionHTML()
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("TimeStarted", "=2017-08-09T10:39:39Z");
        map.put("score", "67");
        map.put("status", "yellow");
        map.put("scorecardURL", "https://test-ssl.acrolinx.com/output/en/something_report.html");
        String inputFormat = "html";
        CheckResult result = new CheckResult(new CheckedDocumentPart("id", new IntRange(1, 1)), map, inputFormat);
        String embedCheckDataAsProcessingInstruction = result.getEmbedCheckDataAsEmbeddableString().get();
        Assert.assertTrue(embedCheckDataAsProcessingInstruction.equalsIgnoreCase(
                "<meta name=\"acrolinxCheckData\" score=\"67\" TimeStarted=\"=2017-08-09T10:39:39Z\" scorecardURL=\"https://test-ssl.acrolinx.com/output/en/something_report.html\" status=\"yellow\" />"));
    }

    @Test
    public void testConvertToProcessingInstructionMarkdown()
    {
        HashMap<String, String> map = new HashMap<>();
        map.put("TimeStarted", "=2017-08-09T10:39:39Z");
        map.put("score", "67");
        map.put("status", "yellow");
        map.put("scorecardURL", "https://test-ssl.acrolinx.com/output/en/something_report.html");
        String inputFormat = "markdown";
        CheckResult result = new CheckResult(new CheckedDocumentPart("id", new IntRange(1, 1)), map, inputFormat);
        String embedCheckDataAsProcessingInstruction = result.getEmbedCheckDataAsEmbeddableString().get();
        System.out.println(embedCheckDataAsProcessingInstruction);
        Assert.assertTrue(embedCheckDataAsProcessingInstruction.equalsIgnoreCase(
                "<!-- acrolinxCheckData score=\"67\" TimeStarted=\"=2017-08-09T10:39:39Z\" scorecardURL=\"https://test-ssl.acrolinx.com/output/en/something_report.html\" status=\"yellow\" -->"));
    }

    @Test
    public void testConvertToProcessingInstructionWhenCheckDataIsNull()
    {
        CheckResult result = new CheckResult(new CheckedDocumentPart("id", new IntRange(1, 1)), null, null);
        Optional<String> embedCheckDataAsProcessingInstruction = result.getEmbedCheckDataAsEmbeddableString();
        Assert.assertFalse(embedCheckDataAsProcessingInstruction.isPresent());
    }
}