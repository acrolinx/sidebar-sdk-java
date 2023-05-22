/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.settings;

public enum SidebarMessageType
{
    SUCCESS("success"), INFO("info"), WARNING("warning"), ERROR("error");

    private String value;

    public String getValue()
    {
        return this.value;
    }

    private SidebarMessageType(String value)
    {
        this.value = value;
    }
}
