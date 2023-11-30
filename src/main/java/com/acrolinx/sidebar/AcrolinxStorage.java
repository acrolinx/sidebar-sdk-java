/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar;

/**
 * Functionality only available with sidebar version 14.4.3
 *
 * <p>Use to overwrite the browser's local storage.
 */
public interface AcrolinxStorage {
  String getItem(String key);

  void removeItem(String key);

  void setItem(String key, String data);
}
