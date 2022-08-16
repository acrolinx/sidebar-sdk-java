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
import java.util.List;
import java.util.stream.Collectors;

public class AcrolinxReuseSwing extends JFXPanel {

    private WebEngine webEngine;

    public AcrolinxReuseSwing() {
        super();
        showReuseWindow();
    }

    public void showPrefferedPhrases(List<String> phrases) {
        String finalString = "'" + phrases.stream().map(p -> "<p>" + p + "</p>").collect(Collectors.joining()) +"'";
        JFXUtils.invokeInJFXThread(() -> {
            this.webEngine.executeScript("document.fillDiv3(" + finalString + ");");
        });
    }

    public void showReuseWindow() {
        JFXUtils.invokeInJFXThread(() -> {
            final WebView webview = new WebView();;
            GridPane.setHgrow(webview, Priority.ALWAYS);
            GridPane.setVgrow(webview, Priority.ALWAYS);
            webview.setPrefWidth(300);
            this.webEngine = webview.getEngine();

            Scene scene = new Scene(webview);
            setScene(scene);
            setVisible(true);
            webEngine.executeScript(""
                    + "let elemDiv = document.createElement('div');"
                    + "let elemDiv2 = document.createElement('div');"
                    + "elemDiv.addEventListener(\"keydown\",function(event) {"
                    + "console.log(event.keyCode);"
                    + "window.logMessage(\"Key has been pressed\");"
                    + "if (event.keyCode === 13) {"
                    + "elemDiv.style.backgroundColor =  (elemDiv.style.backgroundColor === 'red' ? 'white':'red');"
                    + "}"
                    + "if (event.key === 'ArrowDown') {"
                    + "event.currentTarget.nextElementSibling && event.currentTarget.nextElementSibling.focus();"
                    + "}"
                    + "if (event.key === 'ArrowUp') {"
                    + "event.currentTarget.previousElementSibling && event.currentTarget.previousElementSibling.focus();"
                    + "}"
                    + "});"
                    + "elemDiv2.addEventListener(\"keydown\",function(event) {"
                    + "logMessage(\"Key has been pressed\");"
                    + "if (event.keyCode === 13) {"
                    + "elemDiv2.style.backgroundColor =  (elemDiv2.style.backgroundColor === 'red' ? 'white':'red');"
                    + "}"
                    + "if (event.key === 'ArrowDown') {"
                    + "event.currentTarget.nextElementSibling && event.currentTarget.nextElementSibling.focus();"
                    + "}"
                    + "if (event.key === 'ArrowUp') {"
                    + "event.currentTarget.previousElementSibling && event.currentTarget.previousElementSibling.focus();"
                    + "}"
                    + "});"
                    + "elemDiv.innerText = 'This is an example suggested sentence. Bro';"
                    + "elemDiv2.innerText = 'This is another example suggested sentence.';"
                    + "elemDiv.style.cssText='color:blue;text-decoration: underline;cursor: pointer;';"
                    + "elemDiv2.style.cssText='color:blue;text-decoration: underline;cursor: pointer;';"
                    + "elemDiv.tabIndex = 1;"
                    + "elemDiv2.tabIndex = 2;"
                    + "document.body.appendChild(elemDiv);"
                    + "let elemDiv3 = document.createElement('div');"
                    + "document.body.appendChild(elemDiv2);"
                    + "document.fillDiv3 = function(innerText) {"
                    + "elemDiv3.innerHTML = innerText;"
                    + "};"
                    + "document.body.appendChild(elemDiv3);");
        });
    }
}