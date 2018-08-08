/* Copyright (c) 2016-2018 Acrolinx GmbH */

package com.acrolinx.sidebar.pojo.settings;

import com.google.gson.Gson;

/**
 * Available configuration for the Acrolinx Sidebar
 */
@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class SidebarConfiguration
{
    private final Boolean readOnlySuggestions;

    /**
     * The Acrolinx Sidebar can be configured to only show suggestion but to not be clickable. This
     * can be useful if the checked context shouldn't be modified by the Acrolinx Sidebar.
     *
     * @param readOnlySuggestions If set to `true` the suggestions won't be clickable and not modify
     *        the content.
     */
    public SidebarConfiguration(Boolean readOnlySuggestions)
    {

        this.readOnlySuggestions = readOnlySuggestions;
    }

    @Override
    public String toString()
    {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
