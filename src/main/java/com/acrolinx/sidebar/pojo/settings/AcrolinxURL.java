/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.pojo.settings;

@SuppressWarnings("unused")
public class AcrolinxURL
{
    private String url;

    public AcrolinxURL()
    {
        // Just for conversion with GSON
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }
}
