/**
 * Copyright (c) 2021-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.swing;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.acrolinx.sidebar.AcrolinxMultiViewSidebarInterface;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebView;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.AcrolinxStorage;
import com.acrolinx.sidebar.jfx.AcrolinxSidebarJFX;
import com.acrolinx.sidebar.jfx.JFXUtils;
import com.acrolinx.sidebar.utils.AcrolinxException;

public class AcrolinxMultiViewSidebarSwing extends AcrolinxSidebarSwing implements AcrolinxMultiViewSidebarInterface
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

    @Override
    public void addSidebar(AcrolinxIntegration client, String documentId) throws AcrolinxException
    {
        final AcrolinxSidebarJFX existingSidebar = sidebars.get(documentId);
        if (existingSidebar != null) {
            throw new AcrolinxException("Sidebar already exists for document id: " + documentId);
        }
        JFXUtils.invokeInJFXThread(() -> {
            sidebarJFX = new AcrolinxSidebarJFX(client, storage);
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
            if (!sidebars.containsKey(documentId)) {
                sidebars.put(documentId, (AcrolinxSidebarJFX) sidebarJFX);
            }
            setScene(scene);
            setVisible(true);
        });
    }

    @Override
    public void switchSidebar(String documentId) throws AcrolinxException
    {
        final AcrolinxSidebarJFX acrolinxSidebarJFX = sidebars.get(documentId);
        if (acrolinxSidebarJFX == null) {
            throw new AcrolinxException("Existing sidebar not found for document Id. " + documentId);
        }
        JFXUtils.invokeInJFXThread(() -> {
            if (sidebars.containsKey(documentId)) {
                Scene scene = getScene();
                scene.setRoot(acrolinxSidebarJFX.getWebView());
                setScene(scene);
                setVisible(true);
                sidebarJFX = acrolinxSidebarJFX;
            }
        });
    }

    @Override
    public void removeSidebar(String documentId) throws AcrolinxException
    {
        final AcrolinxSidebarJFX removedJFXSidebar = sidebars.remove(documentId);

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
