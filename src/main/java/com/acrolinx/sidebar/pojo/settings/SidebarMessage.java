/* Copyright (c) 2021-present Acrolinx GmbH */

package com.acrolinx.sidebar.pojo.settings;

import com.google.gson.Gson;

public class SidebarMessage
{
    private String type;
    private String title;
    private String text;

    public SidebarMessage(String title, String text, SidebarMessageType type)
    {
        this.type = type.getValue();
        this.title = title;
        this.text = text;
    }

    public String getTitle()
    {
        return title;
    }

    public String getText()
    {
        return text;
    }

    public String getType()
    {
        return type;
    }

    @Override
    public String toString()
    {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
