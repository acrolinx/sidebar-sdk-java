/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.settings;

public class PluginSupportedParameters
{
    private final boolean checkSelection;
    private final boolean supportsBatchChecks;

    /**
     * This is supported only for minimum sidebar version 14.5.0.
     * 
     * @param checkSelection set to true to enable check selection in the sidebar.
     */
    public PluginSupportedParameters(boolean checkSelection)
    {
        this(checkSelection, false);
    }

    public PluginSupportedParameters(boolean checkSelection, boolean supportsBatchChecks)
    {
        this.checkSelection = checkSelection;
        this.supportsBatchChecks = supportsBatchChecks;
    }

    public boolean isCheckSelection()
    {
        return checkSelection;
    }

    public boolean isBatchChecking()
    {
        return supportsBatchChecks;
    }
}
