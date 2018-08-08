/* Copyright (c) 2016-2018 Acrolinx GmbH */

package com.acrolinx.sidebar.pojo.settings;

import com.google.gson.Gson;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class RequestDescription
{
    private final String documentReference;

    /**
     * The path or filename of the document to check. In a CMS, it can be the id that is used to
     * look up the document.
     *
     * @param documentReference
     */
    public RequestDescription(String documentReference)
    {
        this.documentReference = documentReference;
    }

    @Override
    public String toString()
    {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
