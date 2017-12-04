/*
 * Copyright (c) 2016-2017 Acrolinx GmbH
 */

package com.acrolinx.sidebar.pojo.document;

import com.acrolinx.sidebar.pojo.SidebarError;

@SuppressWarnings("unused")
public class CheckError extends SidebarError
{
    private final String checkId;

    public CheckError(String message, String code, String checkId)
    {
        super(message, code);
        this.checkId = checkId;
    }

    public String getCheckId()
    {
        return checkId;
    }

}
