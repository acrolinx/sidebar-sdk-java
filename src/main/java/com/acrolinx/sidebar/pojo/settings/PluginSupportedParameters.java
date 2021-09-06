/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.pojo.settings;

@SuppressWarnings("unused")
public class PluginSupportedParameters
{
    private final boolean checkSelection;

    private boolean supportsBatchChecking;

    /**
     * This is supported only for minimum sidebar version 14.5.0.
     * 
     * @param checkSelection set to true to enable check selection in the sidebar.
     */
    public PluginSupportedParameters(boolean checkSelection)
    {
        this(checkSelection, false);
        //this.checkSelection = checkSelection;
    }

    public PluginSupportedParameters(boolean checkSelection, boolean supportsBatchChecking)
    {
        this.checkSelection = checkSelection;
        this.supportsBatchChecking = supportsBatchChecking;
    }

    public boolean isCheckSelection()
    {
        return checkSelection;
    }

    public boolean isBatchChecking() { return supportsBatchChecking; }

}
