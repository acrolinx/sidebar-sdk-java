/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.jfx;

import java.time.Instant;

import javafx.scene.web.WebView;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.adapter.NullEditorAdapter;
import com.acrolinx.sidebar.pojo.document.CheckContent;
import com.acrolinx.sidebar.utils.LogMessages;

public class AcrolinxSidebarPluginWithoutOptions extends AcrolinxSidebarPlugin
{
    public AcrolinxSidebarPluginWithoutOptions(final AcrolinxIntegration acrolinxIntegration, final WebView webView)
    {
        super(acrolinxIntegration, webView);
    }

    public synchronized void requestGlobalCheck()
    {
        LogMessages.logCheckRequested(logger);
        this.checkStartedTime = Instant.now();
        final CheckContent checkContent = getCheckContentFromClient();
        logger.debug("Fetched check content including external content");

        if ((acrolinxIntegration.getEditorAdapter() != null)
                && !(acrolinxIntegration.getEditorAdapter() instanceof NullEditorAdapter)
                && (checkContent.getContent() != null)) {
            runCheck(false, checkContent);
        } else {
            logger.warn("Current File Editor not supported for checking or no file present.");
            onGlobalCheckRejected();
        }
    }
}
