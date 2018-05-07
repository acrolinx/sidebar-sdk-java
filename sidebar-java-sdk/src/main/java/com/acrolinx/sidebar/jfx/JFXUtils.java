
package com.acrolinx.sidebar.jfx;

import javafx.application.Platform;

public class JFXUtils
{
    public static void invokeInJFXThread(Runnable runnable)
    {
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        } else {
            Platform.runLater(runnable);
        }
    }
}
