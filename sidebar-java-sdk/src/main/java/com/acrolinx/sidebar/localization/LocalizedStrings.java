/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.localization;

@SuppressWarnings("unused")
public enum LocalizedStrings
{
    SHOW_SIDEBAR_LABEL
    {
        @Override
        public String toString()
        {
            return "MENU.JAVASDK.OPENSIDEBAR";
        }
    },
    SHOW_SIDEBAR_TOOLTIP
    {
        @Override
        public String toString()
        {
            return "TOOLBAR.JAVASDK.ACROLINX_BUTTON.TOOLTIP";
        }
    },
    SIDEBAR_CONTAINER_LABEL
    {
        @Override
        public String toString()
        {
            return "WINDOW.JAVASDK.SIDEBAR_CONTAINER.LABEL";
        }
    },
    CURRENT_EDITOR_CAN_NOT_BE_CHECKED_MESSAGE
    {
        @Override
        public String toString()
        {
            return "MESSAGE.JAVASDK.EDITOR_CONTENT_CAN_NOT_BE_CHECKED";
        }
    },
    NO_CHECK_CONTENT_AVAILABLE_MESSAGE
    {
        @Override
        public String toString()
        {
            return "MESSAGE.JAVASDK.NOTHING_AVAILABLE_FOR_CHECKING";
        }
    }
}
