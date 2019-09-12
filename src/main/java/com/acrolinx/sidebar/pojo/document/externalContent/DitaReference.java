/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.pojo.document.externalContent;

public class DitaReference
{
    private String id;
    private String content;

    DitaReference(String id, String content)
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
        return "{\"id\": \"" + id + "\" , \"content\" : \"" + content + "\"}";
    }
}
