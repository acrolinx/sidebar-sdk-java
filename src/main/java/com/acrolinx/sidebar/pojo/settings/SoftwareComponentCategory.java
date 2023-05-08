/* Copyright (c) 2016-present Acrolinx GmbH */

package com.acrolinx.sidebar.pojo.settings;

public enum SoftwareComponentCategory
{
    /**
     * There should be exactly one MAIN component. This information is used to identify your client on
     * the server. Version information about this components might be displayed more prominently.
     */
    MAIN,
    /**
     * Version information about such components are displayed in the about dialog.
     */
    DEFAULT,
    /**
     * Version information about such components are displayed in the detail section of the about dialog
     * or not at all.
     */
    DETAIL
}
