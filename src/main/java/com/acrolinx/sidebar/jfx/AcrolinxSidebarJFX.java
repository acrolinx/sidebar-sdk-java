/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.jfx;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.AcrolinxSidebar;
import com.acrolinx.sidebar.AcrolinxStorage;
import com.acrolinx.sidebar.pojo.document.AbstractMatch;
import com.acrolinx.sidebar.pojo.document.CheckedDocumentPart;
import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContent;
import com.acrolinx.sidebar.pojo.settings.BatchCheckRequestOptions;
import com.acrolinx.sidebar.pojo.settings.CheckOptions;
import com.acrolinx.sidebar.pojo.settings.PluginSupportedParameters;
import com.acrolinx.sidebar.pojo.settings.SidebarConfiguration;
import com.acrolinx.sidebar.pojo.settings.SidebarMessage;
import com.acrolinx.sidebar.utils.LogMessages;
import com.acrolinx.sidebar.utils.SecurityUtils;
import com.acrolinx.sidebar.utils.SidebarUtils;
import com.acrolinx.sidebar.utils.StartPageInstaller;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.scene.CacheHint;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JFX implementation of Acrolinx Sidebar.
 *
 * @see AcrolinxSidebar
 */
public class AcrolinxSidebarJFX implements AcrolinxSidebar
{
    private static final Logger logger = LoggerFactory.getLogger(AcrolinxSidebarJFX.class);

    private WebView webView = new WebView();
    private AcrolinxSidebarPlugin acrolinxSidebarPlugin;
    private final AcrolinxIntegration acrolinxIntegration;

    public AcrolinxSidebarJFX(final AcrolinxIntegration acrolinxIntegration)
    {
        this(acrolinxIntegration, null);
    }

    /**
     * @param acrolinxIntegration The implementation of the Acrolinx Integration.
     */
    public AcrolinxSidebarJFX(final AcrolinxIntegration acrolinxIntegration, final AcrolinxStorage acrolinxStorage)
    {
        LogMessages.logJavaVersionAndUiFramework(logger, "Java FX");
        SecurityUtils.setUpEnvironment();
        this.acrolinxIntegration = acrolinxIntegration;
        final String sidebarUrl = StartPageInstaller.prepareSidebarUrl(acrolinxIntegration.getInitParameters());

        logger.debug("Trying to load sidebar url: {}", sidebarUrl);
        final WebView webView = getWebView();
        final WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webView.setContextMenuEnabled(false);
        webView.setCache(true);
        webView.setCacheHint(CacheHint.SPEED);

        webEngine.setOnError((final WebErrorEvent webErrorEvent) -> logger.error("Error: {}", webErrorEvent));
        webEngine.setOnAlert((final WebEvent<String> webEvent) -> logger.debug("Alert: {}", webEvent.getData()));

        webEngine.getLoadWorker().stateProperty().addListener(
                (final ObservableValue<? extends Worker.State> observedValue, final Worker.State oldState,
                        final Worker.State newState) -> this.getChangeListener(observedValue, oldState, newState,
                                acrolinxStorage));
        webEngine.getLoadWorker().exceptionProperty().addListener(
                (observableValue, oldThrowable, newThrowable) -> logger.error("webEngine exception", newThrowable));

        if (sidebarUrl != null) {
            logger.info("Loading: {}", sidebarUrl);
            webEngine.load(sidebarUrl);
        } else {
            // if sidebar url is not available show log file location
            webEngine.loadContent(SidebarUtils.SIDEBAR_ERROR_HTML);
        }
    }

    private void getChangeListener(final ObservableValue<? extends Worker.State> observedValue,
            final Worker.State oldState, final Worker.State newState, final AcrolinxStorage acrolinxStorage)
    {
        logger.debug("state changed: {} : {} -> {}", observedValue.getValue(), oldState, newState);

        if (newState == Worker.State.SUCCEEDED) {
            this.injectAcrolinxPlugin(acrolinxStorage);
        }

        if (newState == State.FAILED) {
            final WebView webView = getWebView();
            final WebEngine webEngine = webView.getEngine();
            logger.debug("New state: {}", newState);

            if (webEngine.getLoadWorker().getException() != null) {
                logger.error("", webEngine.getLoadWorker().getException());
            }

            webEngine.loadContent(SidebarUtils.SIDEBAR_ERROR_HTML);
        }
    }

    protected void injectAcrolinxPlugin(AcrolinxStorage acrolinxStorage)
    {
        final WebView webView = getWebView();
        final WebEngine webEngine = webView.getEngine();
        logger.debug("Sidebar loaded from: {}", webEngine.getLocation());
        final JSObject jsObject = (JSObject) webEngine.executeScript("window");

        if (jsObject == null) {
            logger.error("Window Object null!");
        } else {
            logger.debug("Injecting JSLogger.");
            jsObject.setMember("java", new JSLogger());
            webEngine.executeScript("console.log = function()\n" + "{\n" + "    java.log('JavaScript: ' + "
                    + "JSON.stringify(Array.prototype.slice.call(arguments)));\n" + "};");
            webEngine.executeScript("console.error = function()\n" + "{\n" + "    java.error('JavaScript: ' + "
                    + "JSON.stringify(Array.prototype.slice.call(arguments)));\n" + "};");
            logger.debug("Setting local storage");
            jsObject.setMember("acrolinxStorage", acrolinxStorage);
        }

        final PluginSupportedParameters supported = this.acrolinxIntegration.getInitParameters().getSupported();

        if ((supported != null) && (supported.isCheckSelection() || supported.isBatchChecking())) {
            acrolinxSidebarPlugin = new AcrolinxSidebarPluginWithOptions(acrolinxIntegration, webView);
        } else {
            acrolinxSidebarPlugin = new AcrolinxSidebarPluginWithoutOptions(acrolinxIntegration, webView);
        }
    }

    public void setZoom(final float i)
    {
        JFXUtils.invokeInJFXThread(() -> {
            try {
                webView.setZoom(i);
            } catch (final Exception e) {
                logger.error("", e);
            }
        });
    }

    public WebView getWebView()
    {
        return webView;
    }

    @Override
    public void configure(final SidebarConfiguration sidebarConfiguration)
    {
        acrolinxSidebarPlugin.configureSidebar(sidebarConfiguration);
    }

    @Override
    public void checkGlobal()
    {
        if (acrolinxSidebarPlugin instanceof AcrolinxSidebarPluginWithOptions) {
            ((AcrolinxSidebarPluginWithOptions) acrolinxSidebarPlugin).requestGlobalCheck(null);
        } else if (acrolinxSidebarPlugin instanceof AcrolinxSidebarPluginWithoutOptions) {
            ((AcrolinxSidebarPluginWithoutOptions) acrolinxSidebarPlugin).requestGlobalCheck();
        }
    }

    @Override
    public void onGlobalCheckRejected()
    {
        acrolinxSidebarPlugin.onGlobalCheckRejected();
    }

    @Override
    public void invalidateRanges(final List<CheckedDocumentPart> invalidCheckedDocumentRanges)
    {
        acrolinxSidebarPlugin.invalidateRanges(invalidCheckedDocumentRanges);
    }

    @Override
    public void invalidateRangesForMatches(final List<? extends AbstractMatch> matches)
    {
        acrolinxSidebarPlugin.invalidateRangesForMatches(matches);
    }

    @Override
    public void loadSidebarFromServerLocation(final String serverAddress)
    {
        acrolinxIntegration.getInitParameters().setServerAddress(serverAddress);
        acrolinxIntegration.getInitParameters().setShowServerSelector(true);
        JFXUtils.invokeInJFXThread(() -> {
            try {
                webView.getEngine().load(SidebarUtils.getSidebarUrl(serverAddress));
            } catch (final Exception e) {
                logger.error("", e);
            }
        });
    }

    @Override
    public void reload()
    {
        if (StartPageInstaller.isExportRequired(acrolinxIntegration.getInitParameters())) {
            try {
                StartPageInstaller.exportStartPageResources();
            } catch (final Exception e) {
                logger.error("Error while exporting start page resources!", e);
                webView.getEngine().loadContent(SidebarUtils.SIDEBAR_ERROR_HTML);
            }
        }

        JFXUtils.invokeInJFXThread(webView.getEngine()::reload);
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

    public ExternalContent getLastCheckedExternalContent()
    {
        return acrolinxSidebarPlugin.getLastCheckedExternalContent();
    }

    @Override
    public void showMessage(SidebarMessage sidebarMessage)
    {
        acrolinxSidebarPlugin.showSidebarMessage(sidebarMessage);
    }

    @Override
    public void initBatchCheck(List<BatchCheckRequestOptions> batchCheckRequestOptions)
    {
        acrolinxSidebarPlugin.initBatchCheck(batchCheckRequestOptions);
    }

    @Override
    public void checkDocumentInBatch(String documentIdentifier, String documentContent, CheckOptions checkOptions)
    {
        acrolinxSidebarPlugin.checkDocumentInBatch(documentIdentifier, documentContent, checkOptions);
    }
}
