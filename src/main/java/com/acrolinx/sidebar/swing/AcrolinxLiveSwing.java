/**
 *  Copyright (c) 2020-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.swing;

import com.acrolinx.sidebar.AcrolinxLiveComponentInterface;
import com.acrolinx.sidebar.jfx.JFXUtils;
import com.acrolinx.sidebar.live.LivePanelState;
import com.acrolinx.sidebar.utils.LivePanelInstaller;
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


public class AcrolinxLiveSwing extends JFXPanel  implements AcrolinxLiveComponentInterface {

    private WebEngine webEngine;

    private WebView webView;
    private final Logger logger = LoggerFactory.getLogger(AcrolinxLiveSwing.class);

    private PhraseSelectionHandler phraseSelectionHandler;

    public AcrolinxLiveSwing(PhraseSelectionHandler phraseSelectionHandler) {
        super();
        try {
            LivePanelInstaller.exportLivePanelResources();
        } catch (final Exception e) {
            logger.error("Error while exporting live panel resources: ", e.getMessage());
        }
        showLiveWindow();
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

    public void showLiveWindow() {
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
                        window.setMember("liveAdapter", this);
                    });
                }
            });

            webEngine.load(LivePanelInstaller.getLivePanelURL());

        });
    }

    public void handlePhraseSelection(String phrase) {
       if( phraseSelectionHandler != null) {
           phraseSelectionHandler.onPhraseSelected(phrase);
       }
    }

    public void closeLivePanel() {
        if( phraseSelectionHandler != null) {
            phraseSelectionHandler.closeLivePanel();
        }
    }

    @Override
    public void setLiveState(LivePanelState livePanelState) {
        JFXUtils.invokeInJFXThread(() -> {
            getWindowObject().eval("postMessage(" + livePanelState.toJSON() + ",'*')");
        });
    }

    public void log(String log) {
        logger.info(log);
    }
}