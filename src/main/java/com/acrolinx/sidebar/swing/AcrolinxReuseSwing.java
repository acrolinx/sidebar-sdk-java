/**
 *  Copyright (c) 2020-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.swing;

import com.acrolinx.sidebar.AcrolinxReuseComponentInterface;
import com.acrolinx.sidebar.jfx.JFXUtils;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AcrolinxReuseSwing extends JFXPanel  implements AcrolinxReuseComponentInterface {

    private WebEngine webEngine;

    private WebView webView;

    private PhraseSelectionHandler phraseSelectionHandler;



    public final static String file = "C:\\Users\\jhorn\\code\\java\\intellij\\reuse-environment\\website\\index.html";

    public AcrolinxReuseSwing(PhraseSelectionHandler phraseSelectionHandler) {
        super();
        showReuseWindow();
        this.phraseSelectionHandler = phraseSelectionHandler;
    }

    protected JSObject getWindowObject()
    {
        JSObject jsobj = null;
        int count = 0;
        while ((count < 6) && (jsobj == null)) {
            try {
                count++;
                TimeUnit.MILLISECONDS.sleep(500);
                jsobj = (JSObject) webView.getEngine().executeScript("window");
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (final Exception e) {
                // Window object might not be available in the first attempt and
                // throws netscape.javascript.JSException: JavaScript execution terminated.
                // The object becomes available on the next try
            }
        }
        return jsobj;
    }



    public void showReuseWindow() {
        JFXUtils.invokeInJFXThread(() -> {
            webView = new WebView();
            GridPane.setHgrow(webView, Priority.ALWAYS);
            GridPane.setVgrow(webView, Priority.ALWAYS);
            webView.setPrefWidth(300);
            webEngine = webView.getEngine();

            Scene scene = new Scene(webView);
            setScene(scene);
            setVisible(true);
            File file = new File(AcrolinxReuseSwing.file);

            webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    // new page has loaded, process:
                    JFXUtils.invokeInJFXThread(() -> {
                        getWindowObject().setMember("reuseAdapter", this);
                    });
                }
            });

            webEngine.load(file.toURI().toString());

        });
    }

    public void handlePhraseSelection(String phrase) {
       if( phraseSelectionHandler != null) {
           phraseSelectionHandler.onPhraseSelected(phrase);
       }
    }

    public void logMessage(String message) {
        if (phraseSelectionHandler != null) {
            phraseSelectionHandler.logMessage(message);
        }
    }

    @Override
    public void showPreferredPhrases(List<String> preferredPhrases) {
        String phrasesList = "[" + preferredPhrases.stream().map(p -> "'"+ p +"'" ).collect(Collectors.joining(",")) +"]";
        JFXUtils.invokeInJFXThread(() -> {
            getWindowObject().eval("postMessage({'phrases':"+ phrasesList + "},'*')");
        });
    }

    @Override
    public void showPreferredPhrasesAndOriginal(List<String> preferredPhrases, String original) {
        String phrasesList = "[" + preferredPhrases.stream().map(p -> "'"+ p +"'" ).collect(Collectors.joining(",")) +"]";
        JFXUtils.invokeInJFXThread(() -> {
            getWindowObject().eval("postMessage({'phrases':"+ phrasesList + ",'original':'"+original+"'},'*')");
        });
    }

    @Override
    public void setLoading(boolean loading, String queriedPhrase) {
        JFXUtils.invokeInJFXThread(() -> {
            getWindowObject().eval("postMessage({'loading':"+(loading? "true":"false" ) +",'queriedPhrase':'"+queriedPhrase+"'},'*')");
            repaint();
        });
    }
}