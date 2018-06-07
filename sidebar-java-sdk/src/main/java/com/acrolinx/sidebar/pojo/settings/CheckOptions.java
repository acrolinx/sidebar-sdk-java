/*
 * Copyright (c) 2016-2017 Acrolinx GmbH
 */

package com.acrolinx.sidebar.pojo.settings;

import com.google.gson.Gson;

/**
 * Check options define how the Acrolinx Server handles document to check.
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class CheckOptions
{
    private final InputFormat inputFormat;
    private final RequestDescription requestDescription;
    private final DocumentSelection selection;

    /**
     * @param inputFormat Check InputFormat for valid formats.
     * @param requestDescription Contains the document reference. This can be an id or path to
     *        identify the document.
     */
    public CheckOptions(RequestDescription requestDescription, InputFormat inputFormat,
            DocumentSelection documentSelection)
    {
        this.requestDescription = requestDescription;
        this.inputFormat = inputFormat;
        this.selection = documentSelection;
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
