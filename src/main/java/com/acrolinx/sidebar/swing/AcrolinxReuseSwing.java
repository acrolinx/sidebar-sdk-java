/**
 *  Copyright (c) 2020-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.swing;

import com.acrolinx.sidebar.AcrolinxReuseComponentInterface;
import com.acrolinx.sidebar.jfx.JFXUtils;
import com.acrolinx.sidebar.reuse.ReusePanelState;
import com.acrolinx.sidebar.utils.ReusePanelInstaller;
import javafx.concurrent.Worker;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


public class AcrolinxReuseSwing extends JFXPanel  implements AcrolinxReuseComponentInterface {

    private WebEngine webEngine;

    private WebView webView;
    private final Logger logger = LoggerFactory.getLogger(AcrolinxReuseSwing.class);

    private PhraseSelectionHandler phraseSelectionHandler;

    public AcrolinxReuseSwing(PhraseSelectionHandler phraseSelectionHandler) {
        super();
        try {
            ReusePanelInstaller.exportReusePanelResources();
        } catch (final Exception e) {
            logger.error("Error while exporting reuse panel resources: ", e.getMessage());
        }
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

            webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    // new page has loaded, process:
                    JFXUtils.invokeInJFXThread(() -> {
                        JSObject window = getWindowObject();
                        window.setMember("reuseAdapter", this);
                    });
                }
            });

            webEngine.load(ReusePanelInstaller.getReusePanelURL());

        });
    }

    public void handlePhraseSelection(String phrase) {
       if( phraseSelectionHandler != null) {
           phraseSelectionHandler.onPhraseSelected(phrase);
       }
    }

    public void closeReusePanel() {
        if( phraseSelectionHandler != null) {
            phraseSelectionHandler.closeReusePanel();
        }
    }

    @Override
    public void setReuseState(ReusePanelState reusePanelState) {
        JFXUtils.invokeInJFXThread(() -> {
            getWindowObject().eval("postMessage(" + reusePanelState.toJSON() + ",'*')");
        });
    }

    @Override
    public void queryCurrentSentence() {
        if(phraseSelectionHandler != null) {
            phraseSelectionHandler.queryCurrentSentence();
        }
    }

    public void log(String log) {
        logger.info(log);
    }
}