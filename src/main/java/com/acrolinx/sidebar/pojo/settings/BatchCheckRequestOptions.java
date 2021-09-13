/**
 * Copyright (c) 2020-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.pojo.settings;

import com.google.gson.Gson;

public class BatchCheckRequestOptions
{

    private String reference;
    private String displayName;

    public BatchCheckRequestOptions(String reference, String displayName)
    {
        this.displayName = displayName;
        this.reference = reference;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getReference() {
        return reference;
    }

    @Override
    public String toString()
    {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
