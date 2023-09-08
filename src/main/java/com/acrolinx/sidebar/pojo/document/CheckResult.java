/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.document;

import com.acrolinx.sidebar.pojo.settings.InputFormat;
import java.util.Map;
import java.util.Optional;

public class CheckResult
{
    public static final String ACROLINX_PROCESSING_INSTRUCTION_TAG_NAME = "acrolinxCheckData";

    private final CheckedDocumentPart checkedDocumentPart;
    private Map<String, String> embedCheckInformation;
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
            final StringBuilder stringBuilder = new StringBuilder();
            getEmbedCheckInformation().ifPresent(map -> {
                stringBuilder.append("<?" + ACROLINX_PROCESSING_INSTRUCTION_TAG_NAME + " ");
                map.forEach(
                        (key, value) -> stringBuilder.append(key).append('=').append('"').append(value).append("\" "));
                stringBuilder.append("?>");
            });

            if (stringBuilder.length() > 0) {
                return Optional.of(stringBuilder.toString());
            }
        } else if (InputFormat.MARKDOWN.toString().equalsIgnoreCase(this.inputFormat)) {
            final StringBuilder stringBuilder = new StringBuilder();
            getEmbedCheckInformation().ifPresent(map -> {
                stringBuilder.append("<!-- " + ACROLINX_PROCESSING_INSTRUCTION_TAG_NAME + " ");
                map.forEach(
                        (key, value) -> stringBuilder.append(key).append('=').append('"').append(value).append("\" "));
                stringBuilder.append("-->");
            });

            if (stringBuilder.length() > 0) {
                return Optional.of(stringBuilder.toString());
            }
        } else if (InputFormat.HTML.toString().equalsIgnoreCase(this.inputFormat)) {
            final StringBuilder stringBuilder = new StringBuilder();
            getEmbedCheckInformation().ifPresent(map -> {
                stringBuilder.append("<meta " + "name=\"" + ACROLINX_PROCESSING_INSTRUCTION_TAG_NAME + "\" ");
                map.forEach(
                        (key, value) -> stringBuilder.append(key).append('=').append('"').append(value).append("\" "));
                stringBuilder.append("/>");
            });

            if (stringBuilder.length() > 0) {
                return Optional.of(stringBuilder.toString());
            }
        }

        return Optional.empty();
    }

    public Optional<String> getInputFormat()
    {
        return Optional.ofNullable(this.inputFormat);
    }
}
