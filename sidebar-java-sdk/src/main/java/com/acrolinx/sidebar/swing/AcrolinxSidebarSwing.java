/*
 * Copyright (c) 2016-2017 Acrolinx GmbH
 */

package com.acrolinx.sidebar.swing;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.AcrolinxSidebar;
import com.acrolinx.sidebar.AcrolinxStorage;
import com.acrolinx.sidebar.jfx.AcrolinxSidebarJFX;
import com.acrolinx.sidebar.pojo.document.AbstractMatch;
import com.acrolinx.sidebar.pojo.document.CheckedDocumentPart;
import com.acrolinx.sidebar.pojo.settings.CheckOptions;
import com.acrolinx.sidebar.pojo.settings.SidebarConfiguration;
import com.acrolinx.sidebar.utils.LogMessages;

/**
 * Swing implementation of Acrolinx Sidebar.
 * @see AcrolinxSidebar
 */
@SuppressWarnings({"SameParameterValue", "WeakerAccess", "unused"})
public class AcrolinxSidebarSwing extends JFXPanel implements AcrolinxSidebar
{
    private AcrolinxSidebarJFX sidebarJFX;
    private final AcrolinxStorage storage;

    private final AcrolinxIntegration integration;

    private final Logger logger = LoggerFactory.getLogger(AcrolinxSidebarSwing.class);

    public AcrolinxSidebarSwing(AcrolinxIntegration integration)
    {
        this(integration, null);
    }

    public AcrolinxSidebarSwing(AcrolinxIntegration integration, AcrolinxStorage storage)
    {
        super();
        LogMessages.logJavaVersionAndUIFramework(logger, "Java Swing with Java FX Sidebar component");
        this.storage = storage;
        this.integration = integration;
        Platform.setImplicitExit(false);
        Platform.runLater(this::createScene);
    }

    protected void processKeyEvent(KeyEvent e)
    {
        // Hack to prevent pasting event for editor (e. g. .
        if (e.getModifiers() == Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()
                && e.getKeyCode() == KeyEvent.VK_V) {
            //Consume it.
            e.consume();
        }
        super.processKeyEvent(e);
    }

    private void createScene()
    {
        sidebarJFX = new AcrolinxSidebarJFX(integration, storage);
        WebView webview = sidebarJFX.getWebView();
        GridPane.setHgrow(webview, Priority.ALWAYS);
        GridPane.setVgrow(webview, Priority.ALWAYS);
        webview.setPrefWidth(300);
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e)
            {
                logger.debug("Component resized");
                logger.debug(getWidth() + " width");

                final float i = (float) getWidth() / 300;
                logger.debug(String.valueOf(i) + " Zoom");
                Platform.runLater(() -> getSidebarJFX().getWebView().setZoom(i));

            }

            @Override
            public void componentMoved(ComponentEvent e)
            {

            }

            @Override
            public void componentShown(ComponentEvent e)
            {

            }

            @Override
            public void componentHidden(ComponentEvent e)
            {

            }
        });
        Scene scene = new Scene(webview);
        setScene(scene);
        setVisible(true);
    }

    @Override
    public void configure(SidebarConfiguration configuration)
    {
        sidebarJFX.configure(configuration);
    }

    @Override
    public void checkGlobal()
    {
        sidebarJFX.checkGlobal();
    }

    @Override
    public void onGlobalCheckRejected()
    {
        sidebarJFX.onGlobalCheckRejected();
    }

    @Override
    public void invalidateRanges(List<CheckedDocumentPart> invalidCheckedDocumentRanges)
    {
        sidebarJFX.invalidateRanges(invalidCheckedDocumentRanges);
    }

    @Override
    public void invalidateRangesForMatches(List<? extends AbstractMatch> matches)
    {
        sidebarJFX.invalidateRangesForMatches(matches);
    }

    @Override
    public void loadSidebarFromServerLocation(String serverAddress)
    {
        sidebarJFX.loadSidebarFromServerLocation(serverAddress);
    }

    @Override
    public void reload()
    {
        sidebarJFX.reload();
    }

    @Override
    public String getLastCheckedDocumentReference()
    {
        return sidebarJFX.getLastCheckedDocumentReference();
    }

    @Override
    public String getLastCheckedDocument()
    {
        return sidebarJFX.getLastCheckedDocument();
    }

    public AcrolinxSidebarJFX getSidebarJFX()
    {
        return this.sidebarJFX;
    }
}
