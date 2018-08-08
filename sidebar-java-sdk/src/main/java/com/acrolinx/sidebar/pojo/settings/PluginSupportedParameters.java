/* Copyright (c) 2017-present Acrolinx GmbH */


package com.acrolinx.sidebar.pojo.settings;

@SuppressWarnings("unused")
public class PluginSupportedParameters
{
    private final boolean checkSelection;

    /**
     * This is supported only for minimum sidebar version 14.5.0.
     * 
     * @param checkSelection set to true to enable check selection in the sidebar.
     */
    public PluginSupportedParameters(boolean checkSelection)
    {
        this.checkSelection = checkSelection;
    }

    public boolean isCheckSelection()
    {
        return checkSelection;
    }
}
