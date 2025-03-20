/* Copyright (c) 2023 Acrolinx GmbH */
package com.acrolinx.sidebar.utils;

/**
 * Similar to {@link Runnable} and {@link java.util.concurrent.Callable} except that it allows for
 * throwing arbitrary exceptions and errors and does not return anything.
 */
@FunctionalInterface
public interface Action {
  void perform() throws Throwable;
}
