/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.pojo.document.externalContent;

import com.acrolinx.sidebar.utils.GsonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Entity
{

    private String id;
    private String content;

    Entity(String id, String content)
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
        return GsonUtils.getJsonFormObject(this);
    }
}
