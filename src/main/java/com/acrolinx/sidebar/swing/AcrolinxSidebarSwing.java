/* Copyright (c) 2016-present Acrolinx GmbH */

package com.acrolinx.sidebar.swing;

import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.AcrolinxSidebar;
import com.acrolinx.sidebar.AcrolinxStorage;
import com.acrolinx.sidebar.jfx.AcrolinxSidebarJFX;
import com.acrolinx.sidebar.jfx.JFXUtils;
import com.acrolinx.sidebar.pojo.document.AbstractMatch;
import com.acrolinx.sidebar.pojo.document.CheckedDocumentPart;
import com.acrolinx.sidebar.pojo.document.externalContent.ExternalContent;
import com.acrolinx.sidebar.pojo.settings.BatchCheckRequestOptions;
import com.acrolinx.sidebar.pojo.settings.CheckOptions;
import com.acrolinx.sidebar.pojo.settings.SidebarConfiguration;
import com.acrolinx.sidebar.pojo.settings.SidebarMessage;
import com.acrolinx.sidebar.utils.LogMessages;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.web.WebView;

/**
 * Swing implementation of Acrolinx Sidebar.
 *
 * @see AcrolinxSidebar
 */
public class AcrolinxSidebarSwing extends JFXPanel implements AcrolinxSidebar
{
    private static final long serialVersionUID = 3813416489627785478L;

    protected transient AcrolinxSidebarJFX sidebarJFX;
    protected final transient AcrolinxStorage storage;
    protected final transient AcrolinxIntegration integration;
    protected final transient Logger logger = LoggerFactory.getLogger(AcrolinxSidebarSwing.class);

    public AcrolinxSidebarSwing(final AcrolinxIntegration integration)
    {
        this(integration, null);
    }

    public AcrolinxSidebarSwing(final AcrolinxIntegration integration, final AcrolinxStorage storage)
    {
        super();
        LogMessages.logJavaVersionAndUIFramework(logger, "Java Swing with Java FX Sidebar component");
        this.storage = storage;
        this.integration = integration;
        Platform.setImplicitExit(false);
        JFXUtils.invokeInJFXThread(this::createScene);
    }

    @Override
    protected void processKeyEvent(final KeyEvent e)
    {
        // Hack to prevent pasting event for editor (e. g. .
        if ((e.getModifiersEx() == Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())
                && (e.getKeyCode() == KeyEvent.VK_V)) {
            // Consume it.
            e.consume();
        } else {
            super.processKeyEvent(e);
        }

    }

    protected void createScene()
    {
        sidebarJFX = new AcrolinxSidebarJFX(integration, storage);
        final WebView webview = sidebarJFX.getWebView();
        GridPane.setHgrow(webview, Priority.ALWAYS);
        GridPane.setVgrow(webview, Priority.ALWAYS);
        webview.setPrefWidth(300);
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(final ComponentEvent e)
            {
                logger.debug("Component resized");
                logger.debug("{} width", getWidth());
                final float i = (float) getWidth() / 300;
                logger.debug("{} Zoom", i);
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
    public void configure(final SidebarConfiguration configuration)
    {
        if (sidebarJFX != null) {
            sidebarJFX.configure(configuration);
        }
    }

    @Override
    public void checkGlobal()
    {
        if (sidebarJFX != null) {
            sidebarJFX.checkGlobal();
        }
    }

    @Override
    public void onGlobalCheckRejected()
    {
        if (sidebarJFX != null) {
            sidebarJFX.onGlobalCheckRejected();
        }
    }

    @Override
    public void invalidateRanges(final List<CheckedDocumentPart> invalidCheckedDocumentRanges)
    {
        if (sidebarJFX != null) {
            sidebarJFX.invalidateRanges(invalidCheckedDocumentRanges);
        }
    }

    @Override
    public void invalidateRangesForMatches(final List<? extends AbstractMatch> matches)
    {
        if (sidebarJFX != null) {
            sidebarJFX.invalidateRangesForMatches(matches);
        }
    }

    @Override
    public void loadSidebarFromServerLocation(final String serverAddress)
    {
        if (sidebarJFX != null) {
            sidebarJFX.loadSidebarFromServerLocation(serverAddress);
        }
    }

    @Override
    public void reload()
    {
        if (sidebarJFX != null) {
            sidebarJFX.reload();
        }
    }

    @Override
    public String getLastCheckedDocumentReference()
    {
        if (sidebarJFX != null) {
            return sidebarJFX.getLastCheckedDocumentReference();
        }
        return "";
    }

    @Override
    public String getLastCheckedDocument()
    {
        if (sidebarJFX != null) {
            return sidebarJFX.getLastCheckedDocument();
        }
        return "";
    }

    public ExternalContent getLastCheckedExternalContent()
    {
        if (sidebarJFX != null) {
            return sidebarJFX.getLastCheckedExternalContent();
        }
        return null;
    }

    @Override
    public void showMessage(SidebarMessage sidebarMessage)
    {
        if (sidebarJFX != null) {
            sidebarJFX.showMessage(sidebarMessage);
        }
    }

    @Override
    public void initBatchCheck(List<BatchCheckRequestOptions> batchCheckRequestOptions)
    {
        if (sidebarJFX != null) {
            sidebarJFX.initBatchCheck(batchCheckRequestOptions);
        }
    }

    @Override
    public void checkDocumentInBatch(String documentIdentifier, String documentContent, CheckOptions options)
    {
        if (sidebarJFX != null) {
            sidebarJFX.checkDocumentInBatch(documentIdentifier, documentContent, options);
        }
    }

    public AcrolinxSidebarJFX getSidebarJFX()
    {
        return this.sidebarJFX;
    }
}
