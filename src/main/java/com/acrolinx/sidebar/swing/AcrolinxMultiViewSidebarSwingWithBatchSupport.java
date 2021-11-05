/**
 * Copyright (c) 2020-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.swing;

import com.acrolinx.sidebar.AcrolinxIntegrationWithBatchSupport;
import com.acrolinx.sidebar.AcrolinxSidebarWithBatchSupport;
import com.acrolinx.sidebar.AcrolinxStorage;
import com.acrolinx.sidebar.jfx.AcrolinxSidebarJFXWithBatchSupport;
import com.acrolinx.sidebar.jfx.JFXUtils;
import com.acrolinx.sidebar.pojo.settings.BatchCheckRequestOptions;
import com.acrolinx.sidebar.pojo.settings.CheckOptions;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class AcrolinxMultiViewSidebarSwingWithBatchSupport extends AcrolinxSidebarSwing
        implements AcrolinxSidebarWithBatchSupport
{

    private Map<URL, AcrolinxSidebarJFXWithBatchSupport> sidebars = new ConcurrentHashMap<>();

    public AcrolinxMultiViewSidebarSwingWithBatchSupport(AcrolinxIntegrationWithBatchSupport integration)
    {
        super(integration);
    }

    public AcrolinxMultiViewSidebarSwingWithBatchSupport(AcrolinxIntegrationWithBatchSupport integration,
                                                         AcrolinxStorage storage)
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

                  for (AcrolinxSidebarJFXWithBatchSupport sidebarJFX : sidebars.values()) {
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


    //TODO: how to handle Storage
    public void addSidebar(AcrolinxIntegrationWithBatchSupport integration, URL documentURL) {
        JFXUtils.invokeInJFXThread(() -> {
        sidebarJFX = new AcrolinxSidebarJFXWithBatchSupport((AcrolinxIntegrationWithBatchSupport) integration, storage);
        final WebView webview = sidebarJFX.getWebView();
        GridPane.setHgrow(webview, Priority.ALWAYS);
        GridPane.setVgrow(webview, Priority.ALWAYS);
        webview.setPrefWidth(300);
        final float i = (float) getWidth() / 300;
        sidebarJFX.setZoom(i);
        Scene scene;
        if(getScene() != null) {
            scene = getScene();
            scene.setRoot(webview);
        } else {
            scene = new Scene(webview);
        }
        if(!sidebars.containsKey(documentURL)) {
            sidebars.put(documentURL, (AcrolinxSidebarJFXWithBatchSupport) sidebarJFX);
        }
        setScene(scene);
        setVisible(true);
        });
    }

    public void switchSidebar(URL documentURL) {
        JFXUtils.invokeInJFXThread(() -> {
            if(sidebars.containsKey(documentURL)) {
                Scene scene = getScene();
                scene.setRoot(sidebars.get(documentURL).getWebView());
                setScene(scene);
                setVisible(true);
                sidebarJFX = sidebars.get(documentURL);
            }
        });
    }

    public void removeSidebar(URL documentURL) {
        JFXUtils.invokeInJFXThread(() -> {
            if(sidebars.containsKey(documentURL)) {
                sidebars.remove(documentURL);
            }
        });
        //TODO: When removing sidebars, if there are no sidebars left, set sidebarJFX to null
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
