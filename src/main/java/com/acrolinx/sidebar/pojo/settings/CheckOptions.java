/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.settings;

import javax.annotation.Nullable;

import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContent;
import com.google.gson.Gson;

/**
 * Check options define how the Acrolinx Server handles document to check.
 */
public class CheckOptions
{
    private final InputFormat inputFormat;
    private final RequestDescription requestDescription;
    private final DocumentSelection selection;
    private final ExternalContent externalContent;

    /**
     * @param inputFormat Check InputFormat for valid formats.
     * @param requestDescription Contains the document reference. This can be an id or path to identify
     *        the document.
     */
    public CheckOptions(RequestDescription requestDescription, InputFormat inputFormat,
            DocumentSelection documentSelection, @Nullable ExternalContent externalContent)
    {
        this.requestDescription = requestDescription;
        this.inputFormat = inputFormat;
        this.selection = documentSelection;
        this.externalContent = externalContent;
    }

    public InputFormat getInputFormat()
    {
        return inputFormat;
    }

    public RequestDescription getRequestDescription()
    {
        return requestDescription;
    }

    @Override
    public String toString()
    {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
