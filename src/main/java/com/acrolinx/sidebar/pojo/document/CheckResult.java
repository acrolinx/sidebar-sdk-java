/* Copyright (c) 2016-present Acrolinx GmbH */

package com.acrolinx.sidebar.pojo.document;

import java.util.Map;
import java.util.Optional;

import com.acrolinx.sidebar.pojo.settings.InputFormat;

public class CheckResult
{
    private final CheckedDocumentPart checkedDocumentPart;
    private Map<String, String> embedCheckInformation;
    public final static String ACROLINX_PROCESSING_INSTRUCTION_TAG_NAME = "acrolinxCheckData";
    private String inputFormat;

    public CheckResult(final CheckedDocumentPart checkedDocumentPart)
    {
        this.checkedDocumentPart = checkedDocumentPart;
    }

    public CheckResult(final CheckedDocumentPart checkedDocumentPart, final Map<String, String> embedCheckInformation,
            final String inputFormat)
    {
        this.checkedDocumentPart = checkedDocumentPart;
        this.embedCheckInformation = embedCheckInformation;
        this.inputFormat = inputFormat;
    }

    public CheckedDocumentPart getCheckedDocumentPart()
    {
        return checkedDocumentPart;
    }

    public Optional<Map<String, String>> getEmbedCheckInformation()
    {
        return Optional.ofNullable(embedCheckInformation);
    }

    public Optional<String> getEmbedCheckDataAsEmbeddableString()
    {
        if (InputFormat.XML.toString().equalsIgnoreCase(this.inputFormat)) {
            final StringBuilder sb = new StringBuilder();
            getEmbedCheckInformation().ifPresent((map) -> {
                sb.append("<?" + ACROLINX_PROCESSING_INSTRUCTION_TAG_NAME + " ");
                map.forEach((key, value) -> sb.append(key).append("=").append("\"").append(value).append("\" "));
                sb.append("?>");
            });
            if (sb.length() > 0) {
                return Optional.of(sb.toString());
            }
        } else if (InputFormat.MARKDOWN.toString().equalsIgnoreCase(this.inputFormat)) {
            final StringBuilder sb = new StringBuilder();
            getEmbedCheckInformation().ifPresent((map) -> {
                sb.append("<!-- " + ACROLINX_PROCESSING_INSTRUCTION_TAG_NAME + " ");
                map.forEach((key, value) -> sb.append(key).append("=").append("\"").append(value).append("\" "));
                sb.append("-->");
            });
            if (sb.length() > 0) {
                return Optional.of(sb.toString());
            }
        } else if (InputFormat.HTML.toString().equalsIgnoreCase(this.inputFormat)) {
            final StringBuilder sb = new StringBuilder();
            getEmbedCheckInformation().ifPresent((map) -> {
                sb.append("<meta " + "name=\"" + ACROLINX_PROCESSING_INSTRUCTION_TAG_NAME + "\" ");
                map.forEach((key, value) -> sb.append(key).append("=").append("\"").append(value).append("\" "));
                sb.append("/>");
            });
            if (sb.length() > 0) {
                return Optional.of(sb.toString());
            }
        }
        return Optional.empty();
    }

    public Optional<String> getInputFormat()
    {
        return Optional.ofNullable(this.inputFormat);
    }
}
