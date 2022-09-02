/* Copyright (c) 2016-present Acrolinx GmbH */

package com.acrolinx.sidebar.jfx;

import java.util.ArrayList;
import java.util.List;

import com.acrolinx.sidebar.pojo.document.externalContent.ExternalContent;
import com.acrolinx.sidebar.swt.Cluster;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import com.acrolinx.sidebar.pojo.settings.*;
import com.acrolinx.sidebar.utils.LogMessages;
import com.acrolinx.sidebar.utils.SecurityUtils;
import com.acrolinx.sidebar.utils.SidebarUtils;
import com.acrolinx.sidebar.utils.StartPageInstaller;

import netscape.javascript.JSObject;

/**
 * JFX implementation of Acrolinx Sidebar.
 *
 * @see AcrolinxSidebar
 */
@SuppressWarnings("unused, WeakerAccess")
public class AcrolinxSidebarJFX implements AcrolinxSidebar
{
    protected WebView webView = new WebView();
    protected AcrolinxSidebarPlugin acrolinxSidebarPlugin;
    protected final AcrolinxIntegration integration;

    protected final Logger logger = LoggerFactory.getLogger(AcrolinxSidebarJFX.class);

    private List<Cluster> allClusters;

    public AcrolinxSidebarJFX(final AcrolinxIntegration integration)
    {
        this(integration, null);
    }

    /**
     * @param integration The implementation of the Acrolinx Integration.
     */
    public AcrolinxSidebarJFX(final AcrolinxIntegration integration, final AcrolinxStorage storage)
    {
        LogMessages.logJavaVersionAndUIFramework(logger, "Java FX");
        SecurityUtils.setUpEnvironment();
        this.integration = integration;
        final String sidebarUrl = StartPageInstaller.prepareSidebarUrl(integration.getInitParameters());

        logger.debug("Trying to load sidebar url: " + sidebarUrl);
        final WebView webView = getWebView();
        final WebEngine webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        webView.setContextMenuEnabled(false);
        webView.setCache(true);
        webView.setCacheHint(CacheHint.SPEED);

        webEngine.setOnError((final WebErrorEvent arg0) -> logger.error("Error: " + arg0.getMessage()));
        webEngine.setOnAlert((final WebEvent<String> arg0) -> logger.debug("Alert: " + arg0.getData()));

        webEngine.getLoadWorker().stateProperty().addListener((
                final ObservableValue<? extends Worker.State> observedValue, final Worker.State oldState,
                final Worker.State newState) -> this.getChangeListener(observedValue, oldState, newState, storage));
        webEngine.getLoadWorker().exceptionProperty().addListener(
                (ov, t, t1) -> logger.error("webEngine exception: " + t1.getMessage()));



        if (sidebarUrl != null) {
            logger.info("Loading: " + sidebarUrl);
            webEngine.load(sidebarUrl);
        } else {
            // if sidebar url is not available show log file location
            webEngine.loadContent(SidebarUtils.SIDEBAR_ERROR_HTML);
        }
    }

    public void callReuse() {
        JFXUtils.invokeInJFXThread(() -> {
            try {
                logger.info("about to call reuse");
                acrolinxSidebarPlugin.getWindowObject().eval("acrolinxSidebar.callReuse();");
                logger.info("called reuse");
            } catch (Exception e) {
                logger.error(e.toString());
            }
        });
    }

    private void getChangeListener(final ObservableValue<? extends Worker.State> observedValue,
            final Worker.State oldState, final Worker.State newState, final AcrolinxStorage storage)
    {

        logger.debug("state changed: " + observedValue.getValue() + ": " + oldState + " -> " + newState);
        if (newState == Worker.State.SUCCEEDED) {
            this.injectAcrolinxPlugin(storage);
        }
        if ("FAILED".equals("" + newState)) {
            final WebView webView = getWebView();
            final WebEngine webEngine = webView.getEngine();
            logger.debug("New state: " + newState);
            // noinspection ThrowableResultOfMethodCallIgnored
            if (webEngine.getLoadWorker().getException() != null) {
                // noinspection ThrowableResultOfMethodCallIgnored
                logger.error(webEngine.getLoadWorker().getException().getMessage());
            }
            webEngine.loadContent(SidebarUtils.SIDEBAR_ERROR_HTML);
        }
    }



    protected void injectAcrolinxPlugin(AcrolinxStorage storage)
    {
        final WebView webView = getWebView();
        final WebEngine webEngine = webView.getEngine();
        logger.debug("Sidebar loaded from " + webEngine.getLocation());
        final JSObject jsobj = (JSObject) webEngine.executeScript("window");
        if (jsobj == null) {
            logger.error("Window Object null!");
        } else {
            logger.debug("Injecting JSLogger.");
            jsobj.setMember("java", new JSLogger());
            webEngine.executeScript("console.log = function()\n" + "{\n" + "    java.log('JavaScript: ' + "
                    + "JSON.stringify(Array.prototype.slice.call(arguments)));\n" + "};");
            webEngine.executeScript("console.error = function()\n" + "{\n" + "    java.error('JavaScript: ' + "
                    + "JSON.stringify(Array.prototype.slice.call(arguments)));\n" + "};");
            logger.debug("Setting local storage");
            jsobj.setMember("acrolinxStorage", storage);
        }
        final PluginSupportedParameters supported = this.integration.getInitParameters().getSupported();
        if ((supported != null) && (supported.isCheckSelection() || supported.isBatchChecking())) {
            acrolinxSidebarPlugin = new AcrolinxSidebarPluginWithOptions(integration, webView);
        } else {
            acrolinxSidebarPlugin = new AcrolinxSidebarPluginWithoutOptions(integration, webView);
        }
    }

    public void setZoom(final float i)
    {
        JFXUtils.invokeInJFXThread(() -> {
            try {
                webView.setZoom(i);
            } catch (final Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    public WebView getWebView()
    {
        return webView;
    }

    @Override
    public void configure(final SidebarConfiguration configuration)
    {
        acrolinxSidebarPlugin.configureSidebar(configuration);

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
        integration.getInitParameters().setServerAddress(serverAddress);
        integration.getInitParameters().setShowServerSelector(true);
        JFXUtils.invokeInJFXThread(() -> {
            try {
                webView.getEngine().load(SidebarUtils.getSidebarUrl(serverAddress));
            } catch (final Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    @Override
    public void reload()
    {
        if (StartPageInstaller.isExportRequired(integration.getInitParameters())) {
            try {
                StartPageInstaller.exportStartPageResources();
            } catch (final Exception e) {
                logger.error("Error while exporting start page resources!");
                logger.error(e.getMessage());
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

    public ExternalContent getLastCheckedExternalContent() { return acrolinxSidebarPlugin.getLastCheckedExternalContent();}

    @Override
    public void showMessage(SidebarMessage sidebarMessage)
    {
        acrolinxSidebarPlugin.showSidebarMessage(sidebarMessage);
    }

    @Override
    public void initBatchCheck(List<BatchCheckRequestOptions> batchCheckRequestOptions)
    {
        ((AcrolinxSidebarPlugin) acrolinxSidebarPlugin).initBatchCheck(batchCheckRequestOptions);
    }

    @Override
    public void checkDocumentInBatch(String documentIdentifier, String documentContent, CheckOptions options)
    {
        ((AcrolinxSidebarPlugin) acrolinxSidebarPlugin).checkDocumentInBatch(documentIdentifier, documentContent,
                options);
    }

    @Override
    public void reusePrefixSearch(String prefix)
    {
        ((AcrolinxSidebarPlugin) acrolinxSidebarPlugin).reusePrefixSearch(prefix);
    }

    public List<String> getPhrases(String original) {
        List<String> phrases = new ArrayList<>();
        phrases.add("Hallo");
        phrases.add("Wie geht es Ihnen?");
        phrases.add("Wie geht es Ihnen heute?");
        phrases.add("Wie geht es Ihnen jetzt?");
        return phrases;
    }


}
