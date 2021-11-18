/**
 * Copyright (c) 2020-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.pojo.settings;

import com.google.gson.Gson;

public class BatchCheckRequestOptions
{

    private String documentIdentifier;
    private String displayName;

    public BatchCheckRequestOptions(String reference, String displayName)
    {
        this.displayName = displayName;
        this.documentIdentifier = reference;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getReference()
    {
        return documentIdentifier;
    }

    @Override
    public String toString()
    {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
