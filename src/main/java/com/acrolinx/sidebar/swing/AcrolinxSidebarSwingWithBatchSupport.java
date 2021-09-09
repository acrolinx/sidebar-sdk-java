/**
 * Copyright (c) 2020-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.swing;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebView;

import com.acrolinx.sidebar.AcrolinxIntegrationWithBatchSupport;
import com.acrolinx.sidebar.AcrolinxSidebarWithBatchSupport;
import com.acrolinx.sidebar.AcrolinxStorage;
import com.acrolinx.sidebar.jfx.AcrolinxSidebarJFXWithBatchSupport;
import com.acrolinx.sidebar.pojo.settings.BatchCheckRequestOptions;
import com.acrolinx.sidebar.pojo.settings.CheckOptions;

public class AcrolinxSidebarSwingWithBatchSupport extends AcrolinxSidebarSwing
        implements AcrolinxSidebarWithBatchSupport
{

    public AcrolinxSidebarSwingWithBatchSupport(AcrolinxIntegrationWithBatchSupport integration)
    {
        super(integration);
    }

    public AcrolinxSidebarSwingWithBatchSupport(AcrolinxIntegrationWithBatchSupport integration,
            AcrolinxStorage storage)
    {
        super(integration, storage);
    }

    @Override
    protected void createScene()
    {
        sidebarJFX = new AcrolinxSidebarJFXWithBatchSupport((AcrolinxIntegrationWithBatchSupport) integration, storage);
        final WebView webview = sidebarJFX.getWebView();
        GridPane.setHgrow(webview, Priority.ALWAYS);
        GridPane.setVgrow(webview, Priority.ALWAYS);
        webview.setPrefWidth(300);
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(final ComponentEvent e)
            {
                logger.debug("Component resized");
                logger.debug(getWidth() + " width");
                final float i = (float) getWidth() / 300;
                logger.debug(i + " Zoom");
                sidebarJFX.setZoom(i);
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
        final Scene scene = new Scene(webview);
        setScene(scene);
        setVisible(true);
    }

    @Override
    public void initBatchCheck(List<BatchCheckRequestOptions> batchCheckRequestOptions)
    {
        ((AcrolinxSidebarJFXWithBatchSupport) sidebarJFX).initBatchCheck(batchCheckRequestOptions);
    }

    @Override
    public void checkReferenceInBackground(String reference, String documentContent, CheckOptions options)
    {
        ((AcrolinxSidebarJFXWithBatchSupport) sidebarJFX).checkReferenceInBackground(reference, documentContent,
                options);
    }

    @Override
    public void onReferenceLoadedInEditor(String reference)
    {
        ((AcrolinxSidebarJFXWithBatchSupport) sidebarJFX).onReferenceLoadedInEditor(reference);
    }

}
