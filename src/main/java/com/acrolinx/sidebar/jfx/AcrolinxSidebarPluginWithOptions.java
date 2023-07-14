/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.jfx;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.Future;

import javafx.scene.web.WebView;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.adapter.NullEditorAdapter;
import com.acrolinx.sidebar.pojo.document.CheckContent;
import com.acrolinx.sidebar.pojo.settings.BatchCheckRequestOptions;
import com.acrolinx.sidebar.utils.LogMessages;

import netscape.javascript.JSObject;

public class AcrolinxSidebarPluginWithOptions extends AcrolinxSidebarPlugin
{
    public AcrolinxSidebarPluginWithOptions(final AcrolinxIntegration client, final WebView webView)
    {
        super(client, webView);
    }

    public synchronized void requestGlobalCheck(final JSObject o)
    {
        LogMessages.logCheckRequested(logger);
        this.checkStartedTime = Instant.now();
        boolean selection = false;
        boolean batchCheck = false;
        if (o != null) {
            if (o.getMember("selection") != null) {
                selection = Boolean.parseBoolean(o.getMember("selection").toString());
            }
            if (o.getMember("batchCheck") != null) {
                batchCheck = Boolean.parseBoolean(o.getMember("batchCheck").toString());
            }
        }
        if (batchCheck == true) {
            final Future<Boolean> initBatchFuture = executorService.submit(() -> {
                try {
                    final List<BatchCheckRequestOptions> batchCheckRequestOptions = client.extractReferences();
                    initBatchCheck(batchCheckRequestOptions);
                    return true;
                } catch (Exception e) {
                    logger.error("Extracting references in Future Task failed", e);
                    return false;
                }
            });
            logger.debug("Extracting references in Future task. Future task is running: {}", !initBatchFuture.isDone());
        } else {
            final CheckContent checkContent = getCheckContentFromClient();
            logger.debug("Fetched check content including external content");
            if ((client.getEditorAdapter() != null) && !(client.getEditorAdapter() instanceof NullEditorAdapter)
                    && (checkContent.getContent() != null)) {
                logger.debug("Editor is ready for running a check");
                runCheck(selection, checkContent);
            } else {
                logger.warn("Current File Editor not supported for checking or no file present.");
                onGlobalCheckRejected();
            }
        }
    }
}
