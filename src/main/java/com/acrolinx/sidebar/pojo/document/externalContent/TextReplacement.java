/**
 * Copyright (c) 2019-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.pojo.document.externalContent;

public class TextReplacement
{
    private String id;
    private String content;

    TextReplacement(String id, String content)
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
