
package com.acrolinx.sidebar.pojo.settings;

@SuppressWarnings("unused")
public class PluginSupportedParameters
{
    private final boolean checkSelection;

    public PluginSupportedParameters(boolean checkSelection)
    {
        this.checkSelection = checkSelection;
    }

    public boolean isCheckSelection()
    {
        return checkSelection;
    }
}
