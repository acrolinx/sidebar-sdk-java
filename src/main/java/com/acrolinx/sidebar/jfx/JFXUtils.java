/* Copyright (c) 2018-present Acrolinx GmbH */

package com.acrolinx.sidebar.jfx;

import javafx.application.Platform;

public final class JFXUtils
{
    public static void invokeInJFXThread(Runnable runnable)
    {
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        } else {
            Platform.runLater(runnable);
        }
    }

    private JFXUtils()
    {
        throw new IllegalStateException();
    }
}
