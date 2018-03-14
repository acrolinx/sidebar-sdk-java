/*
 * Copyright (c) 2016-2017 Acrolinx GmbH
 */

package com.acrolinx.sidebar.jfx;

import static jdk.nashorn.internal.objects.Global.undefined;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.scene.CacheHint;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.AcrolinxSidebar;
import com.acrolinx.sidebar.AcrolinxStorage;
import com.acrolinx.sidebar.pojo.document.AbstractMatch;
import com.acrolinx.sidebar.pojo.document.CheckedDocumentPart;
import com.acrolinx.sidebar.pojo.settings.CheckOptions;
import com.acrolinx.sidebar.pojo.settings.PluginSupportedParameters;
import com.acrolinx.sidebar.pojo.settings.SidebarConfiguration;
import com.acrolinx.sidebar.utils.LogMessages;
import com.acrolinx.sidebar.utils.SecurityUtils;
import com.acrolinx.sidebar.utils.SidebarUtils;
import com.acrolinx.sidebar.utils.StartPageInstaller;

import netscape.javascript.JSObject;

/**
 * JFX implementation of Acrolinx Sidebar.
 * @see AcrolinxSidebar
 */
@SuppressWarnings("unused")
public class AcrolinxSidebarJFX implements AcrolinxSidebar
{
    private final WebView browser = new WebView();
    private final WebEngine webEngine;
    private AcrolinxSidebarPlugin acrolinxSidebarPlugin;
    private final AcrolinxIntegration integration;

    private final Logger logger = LoggerFactory.getLogger(AcrolinxSidebarJFX.class);

    @SuppressWarnings("WeakerAccess")
    public AcrolinxSidebarJFX(AcrolinxIntegration integration)
    {
        this(integration, null);
    }

    /**
     * @param integration The implementation of the Acrolinx Integration.
     */
    @SuppressWarnings("ConstantConditions")
    public AcrolinxSidebarJFX(AcrolinxIntegration integration, AcrolinxStorage storage)
    {
        LogMessages.logJavaVersionAndUIFramework(logger, "Java FX");
        SecurityUtils.setUpEnvironment();
        this.integration = integration;
        String sidebarUrl = null;
        try {
            sidebarUrl = integration.getInitParameters().getSidebarUrl();
        } catch (Exception e) {
            logger.error("Error getting sidebarURL", e);
        }

        logger.debug("Trying to load sidebar url: " + sidebarUrl);

        this.webEngine = this.browser.getEngine();
        webEngine.setJavaScriptEnabled(true);
        browser.setContextMenuEnabled(false);
        browser.setCache(true);
        browser.setCacheHint(CacheHint.SPEED);

        webEngine.setOnError((final WebErrorEvent arg0) -> logger.error("Error: " + arg0.getMessage()));
        webEngine.setOnAlert((final WebEvent<String> arg0) -> logger.debug("Alert: " + arg0.getData()));

        webEngine.getLoadWorker().stateProperty().addListener(
                (final ObservableValue<? extends Worker.State> observedValue, final Worker.State oldState,
                        final Worker.State newState) -> {
                    logger.debug("state changed: " + observedValue.getValue() + ": " + oldState + " -> " + newState);
                    if (newState == Worker.State.SUCCEEDED) {
                        logger.debug("Sidebar loaded from " + webEngine.getLocation());
                        final JSObject jsobj = (JSObject) webEngine.executeScript("window");
                        if (jsobj == null || jsobj == undefined) {
                            logger.error("Window Object undefined or null!");
                        }
                        logger.debug("Setting local storage");
                        jsobj.setMember("acrolinxStorage", storage);
                        PluginSupportedParameters supported = integration.getInitParameters().getSupported();
                        if (supported != null && supported.isCheckSelection()) {
                            acrolinxSidebarPlugin = new AcrolinxSidebarPluginWithCheckSelectionSupport(integration,
                                    jsobj);
                        } else {
                            acrolinxSidebarPlugin = new AcrolinxSidebarPluginWithoutCheckSelectionSupport(integration,
                                    jsobj);
                        }
                        logger.debug("Injecting Acrolinx Plugin.");
                        jsobj.setMember("acrolinxPlugin", acrolinxSidebarPlugin);
                    }
                    if ("FAILED".equals("" + newState)) {
                        logger.debug("New state: " + newState);
                        //noinspection ThrowableResultOfMethodCallIgnored
                        if (webEngine.getLoadWorker().getException() != null) {
                            //noinspection ThrowableResultOfMethodCallIgnored
                            logger.error(webEngine.getLoadWorker().getException().getMessage());
                        }
                        webEngine.loadContent(SidebarUtils.sidebarErrorHTML);
                    }
                });
        webEngine.getLoadWorker().exceptionProperty().addListener((ov, t, t1) -> {
            logger.error("webEngine exception: " + t1.getMessage());
        });

        com.sun.javafx.webkit.WebConsoleListener.setDefaultListener(
                (webView, message, lineNumber, sourceId) -> logger.info("JavaScript: " + message));
        if (sidebarUrl != null) {
            logger.info("Loading: " + sidebarUrl);
            webEngine.load(sidebarUrl);
        } else {
            // if sidebar url is not available show log file location
            webEngine.loadContent(SidebarUtils.sidebarErrorHTML);
        }
    }

    public WebView getWebView()
    {
        return this.browser;
    }

    @Override
    public void configure(SidebarConfiguration configuration)
    {
        acrolinxSidebarPlugin.configureSidebar(configuration);

    }

    @Override
    public CompletableFuture<String> checkGlobal(String documentContent, CheckOptions options)
    {
        return acrolinxSidebarPlugin.checkGlobal(documentContent, options);
    }

    @Override
    public void onGlobalCheckRejected()
    {

        acrolinxSidebarPlugin.onGlobalCheckRejected();
    }

    @Override
    public void invalidateRanges(List<CheckedDocumentPart> invalidCheckedDocumentRanges)
    {
        acrolinxSidebarPlugin.invalidateRanges(invalidCheckedDocumentRanges);

    }

    @Override
    public void invalidateRangesForMatches(List<? extends AbstractMatch> matches)
    {
        acrolinxSidebarPlugin.invalidateRangesForMatches(matches);
    }

    @Override
    public void loadSidebarFromServerLocation(String serverAddress)
    {
        integration.getInitParameters().setServerAddress(serverAddress);
        integration.getInitParameters().setShowServerSelector(true);
        Platform.runLater(() -> webEngine.load(SidebarUtils.getSidebarUrl(serverAddress)));
    }

    @Override
    public void reload()
    {
        if (integration.getInitParameters().getShowServerSelector()) {
            try {
                StartPageInstaller.exportStartPageResources();
            } catch (Exception e) {
                logger.error("Error while exporting start page resources!");
                logger.error(e.getMessage());
                webEngine.loadContent(SidebarUtils.sidebarErrorHTML);
            }
        }
        Platform.runLater(webEngine::reload);
    }

    @Override
    public String getLastCheckedDocumentReference()
    {
        return acrolinxSidebarPlugin.getLastCheckedDocumentReference();
    }

    @Override
    public String getLastCheckedDocument()
    {
        return acrolinxSidebarPlugin.getLastCheckedDocument();
    }
}
