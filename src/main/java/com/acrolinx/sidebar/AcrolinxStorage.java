/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar;

/**
 * Functionality only available with sidebar version 14.4.3
 *
 * Use to overwrite the browser's local storage.
 */
public interface AcrolinxStorage
{
    String getItem(String key);

    void removeItem(String key);

    void setItem(String key, String data);
}
