/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.pojo.document.externalContent;

import com.google.gson.Gson;

public class ExternalContentField
{
    private final String id;
    private final String content;

    ExternalContentField(String id, String content)
    {
        this.id = id;
        this.content = content;
    }

    public String getId()
    {
        return id;
    }

    public String getContent()
    {
        return content;
    }

    @Override
    public String toString()
    {
        return new Gson().toJson(this);
    }
}
