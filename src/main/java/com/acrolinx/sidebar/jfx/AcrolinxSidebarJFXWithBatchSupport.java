/**
 * Copyright (c) 2020-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.jfx;

import java.util.List;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.AcrolinxIntegrationWithBatchSupport;
import com.acrolinx.sidebar.AcrolinxSidebarWithBatchSupport;
import com.acrolinx.sidebar.AcrolinxStorage;
import com.acrolinx.sidebar.pojo.settings.BatchCheckRequestOptions;
import com.acrolinx.sidebar.pojo.settings.CheckOptions;
import com.acrolinx.sidebar.pojo.settings.PluginSupportedParameters;

import netscape.javascript.JSObject;

public class AcrolinxSidebarJFXWithBatchSupport extends AcrolinxSidebarJFX implements AcrolinxSidebarWithBatchSupport
{

    public AcrolinxSidebarJFXWithBatchSupport(final AcrolinxIntegrationWithBatchSupport integration)
    {
        super(integration);
    }

    public AcrolinxSidebarJFXWithBatchSupport(final AcrolinxIntegrationWithBatchSupport integration,
            final AcrolinxStorage storage)
    {
        super(integration, storage);
    }

    @Override
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
        if ((supported != null) && supported.isCheckSelection()) {
            acrolinxSidebarPlugin = new AcrolinxSidebarPluginWithBatchSupportAndCheckSelection(
                    (AcrolinxIntegrationWithBatchSupport) integration, webView);
        } else {
            acrolinxSidebarPlugin = new AcrolinxSidebarPluginWithBatchSupportAndNoCheckSelection(
                    (AcrolinxIntegrationWithBatchSupport) integration, webView);
        }
    }

    @Override
    public void initBatchCheck(List<BatchCheckRequestOptions> batchCheckRequestOptions)
    {
        ((AcrolinxSidebarPluginWithBatchSupport) acrolinxSidebarPlugin).initBatchCheck(batchCheckRequestOptions);
    }

    @Override
    public void checkReferenceInBackground(String reference, String documentContent, CheckOptions options)
    {
        ((AcrolinxSidebarPluginWithBatchSupport) acrolinxSidebarPlugin).checkReferenceInBackground(reference,
                documentContent, options);
    }

    @Override
    public void onReferenceLoadedInEditor(String reference)
    {
        ((AcrolinxSidebarPluginWithBatchSupport) acrolinxSidebarPlugin).onReferenceLoadedInEditor(reference);
    }

}
