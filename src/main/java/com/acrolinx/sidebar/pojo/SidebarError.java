/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo;

/** Error Object returned if an error occurred within the Acrolinx Sidebar. */
public class SidebarError {
  private final String code;
  private final String message;

  public SidebarError(String message, String code) {
    this.message = message;
    this.code = code;
  }

  public String getErrorCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }
}
