/**
 *  Copyright (c) 2020-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.swing;

import com.acrolinx.sidebar.jfx.JFXUtils;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.awt.*;
import java.awt.event.KeyEvent;

public class AcrolinxReuseSwing extends JFXPanel {

    public AcrolinxReuseSwing() {
        super();
        showReuseWindow();
    }

    public void showReuseWindow() {
        JFXUtils.invokeInJFXThread(() -> {
            final WebView webview = new WebView();;
            GridPane.setHgrow(webview, Priority.ALWAYS);
            GridPane.setVgrow(webview, Priority.ALWAYS);
            webview.setPrefWidth(300);
            final WebEngine webEngine = webview.getEngine();

            Scene scene = new Scene(webview);
            setScene(scene);
            setVisible(true);
            webEngine.executeScript(""
                    + "let elemDiv = document.createElement('div');"
                    + "let elemDiv2 = document.createElement('div');"
                    + "elemDiv.innerText = 'This is an example suggested sentence.';"
                    + "elemDiv2.innerText = 'This is another example suggested sentence.';"
                    + "elemDiv.style.cssText='color:blue;text-decoration: underline;cursor: pointer;';"
                    + "elemDiv2.style.cssText='color:blue;text-decoration: underline;cursor: pointer;';"
                    + "elemDiv.tabIndex = 1;"
                    + "elemDiv2.tabIndex = 2;"
                    + "document.body.appendChild(elemDiv);"
                    + "document.body.appendChild(elemDiv2);");
        });
    }
}