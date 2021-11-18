/**
 * Copyright (c) 2021-present Acrolinx GmbH
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
import com.acrolinx.sidebar.swt.AcrolinxSidebarSWT;
import com.acrolinx.sidebar.utils.AcrolinxException;

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

    public void addSidebar(AcrolinxIntegration integration, String documentURL) throws AcrolinxException
    {
        final AcrolinxSidebarJFX existingSidebar = sidebars.get(documentURL);
        if (existingSidebar != null) {
            throw new AcrolinxException("Sidebar already exists for document id: " + documentURL);
        }
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

    public void switchSidebar(String documentURL) throws AcrolinxException
    {
        final AcrolinxSidebarJFX acrolinxSidebarJFX = sidebars.get(documentURL);
        if (acrolinxSidebarJFX == null) {
            throw new AcrolinxException("Existing sidebar not found for document Id. " + documentURL);
        }
        JFXUtils.invokeInJFXThread(() -> {
            if (sidebars.containsKey(documentURL)) {
                Scene scene = getScene();
                // scene.setRoot(sidebars.get(documentURL).getWebView());
                scene.setRoot(acrolinxSidebarJFX.getWebView());
                setScene(scene);
                setVisible(true);
                // sidebarJFX = sidebars.get(documentURL);
                sidebarJFX = acrolinxSidebarJFX;
            }
        });
    }

    public void removeSidebar(String documentURL) throws AcrolinxException
    {
        final AcrolinxSidebarJFX removedJFXSidebar = sidebars.remove(documentURL);

        if (removedJFXSidebar == null) {
            throw new AcrolinxException("Sidebar doesn't exist for the given document Id");
        }
        if (sidebars.isEmpty()) {
            JFXUtils.invokeInJFXThread(() -> {
                setVisible(false);
                sidebarJFX = null;
            });

        }
    }

}
