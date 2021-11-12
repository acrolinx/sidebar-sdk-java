/**
 * Copyright (c) 2020-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.swing;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebView;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.AcrolinxStorage;
import com.acrolinx.sidebar.jfx.AcrolinxSidebarJFX;
import com.acrolinx.sidebar.jfx.JFXUtils;

public class AcrolinxMultiViewSidebarSwing extends AcrolinxSidebarSwing
{

    private Map<String, AcrolinxSidebarJFX> sidebars = new ConcurrentHashMap<>();

    public AcrolinxMultiViewSidebarSwing(AcrolinxIntegration integration)
    {
        super(integration);
    }

    public AcrolinxMultiViewSidebarSwing(AcrolinxIntegration integration, AcrolinxStorage storage)
    {
        super(integration, storage);
    }

    @Override
    protected void createScene()
    {
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(final ComponentEvent e)
            {

                for (AcrolinxSidebarJFX sidebarJFX : sidebars.values()) {
                    logger.debug("Component resized");
                    logger.debug(getWidth() + " width");
                    final float i = (float) getWidth() / 300;
                    logger.debug(i + " Zoom");
                    sidebarJFX.setZoom(i);
                }
            }

            @Override
            public void componentMoved(final ComponentEvent e)
            {
                // we only need resize event to be handled
            }

            @Override
            public void componentShown(final ComponentEvent e)
            {
                // we only need resize event to be handled
            }

            @Override
            public void componentHidden(final ComponentEvent e)
            {
                // we only need resize event to be handled
            }
        });
    }

    // TODO: how to handle Storage
    public void addSidebar(AcrolinxIntegration integration, String documentURL)
    {
        JFXUtils.invokeInJFXThread(() -> {
            sidebarJFX = new AcrolinxSidebarJFX(integration, storage);
            final WebView webview = sidebarJFX.getWebView();
            GridPane.setHgrow(webview, Priority.ALWAYS);
            GridPane.setVgrow(webview, Priority.ALWAYS);
            webview.setPrefWidth(300);
            final float i = (float) getWidth() / 300;
            sidebarJFX.setZoom(i);
            Scene scene;
            if (getScene() != null) {
                scene = getScene();
                scene.setRoot(webview);
            } else {
                scene = new Scene(webview);
            }
            if (!sidebars.containsKey(documentURL)) {
                sidebars.put(documentURL, (AcrolinxSidebarJFX) sidebarJFX);
            }
            setScene(scene);
            setVisible(true);
        });
    }

    public void switchSidebar(String documentURL)
    {
        JFXUtils.invokeInJFXThread(() -> {
            if (sidebars.containsKey(documentURL)) {
                Scene scene = getScene();
                scene.setRoot(sidebars.get(documentURL).getWebView());
                setScene(scene);
                setVisible(true);
                sidebarJFX = sidebars.get(documentURL);
            }
        });
    }

    public void removeSidebar(String documentURL)
    {
        JFXUtils.invokeInJFXThread(() -> {
            if (sidebars.containsKey(documentURL)) {
                sidebars.remove(documentURL);
            }
        });
        // TODO: When removing sidebars, if there are no sidebars left, set sidebarJFX to null
    }

}
