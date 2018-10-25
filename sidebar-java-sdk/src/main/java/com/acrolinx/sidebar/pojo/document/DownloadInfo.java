/* Copyright (c) 2016-present Acrolinx GmbH */

package com.acrolinx.sidebar.pojo.document;

@SuppressWarnings({"WeakerAccess", "unused"})
public class DownloadInfo
{
    private String url;
    private String filename;

    public DownloadInfo()
    {
        // for GSON
    }

    public String getUrl()
    {
        return url;
    }

    public String getFilename()
    {
        return filename;
    }
}
