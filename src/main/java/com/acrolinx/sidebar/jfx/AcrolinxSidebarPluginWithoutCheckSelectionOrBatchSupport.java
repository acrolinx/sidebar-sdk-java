/**
 * Copyright (c) 2020-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.jfx;

import com.acrolinx.sidebar.adapter.NullEditorAdapter;
import com.acrolinx.sidebar.pojo.document.CheckContent;
import com.acrolinx.sidebar.utils.LogMessages;
import javafx.scene.web.WebView;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.pojo.settings.CheckModeType;

import java.time.Instant;

public class AcrolinxSidebarPluginWithoutCheckSelectionOrBatchSupport extends AcrolinxSidebarPlugin
{
    public AcrolinxSidebarPluginWithoutCheckSelectionOrBatchSupport(final AcrolinxIntegration client, final WebView webView)
    {
        super(client, webView);
    }

    public synchronized void requestGlobalCheck()
    {
        LogMessages.logCheckRequested(logger);
        this.checkStartedTime = Instant.now();
        final CheckContent checkContent = getCheckContentFromClient();
        logger.debug("Fetched check content including external content");
        if ((client.getEditorAdapter() != null) && !(client.getEditorAdapter() instanceof NullEditorAdapter)
                && (checkContent.getContent() != null)) {
            runCheck(false, checkContent);
        } else {
            logger.warn("Current File Editor not supported for checking or no file present.");
            onGlobalCheckRejected();
        }
    }
}
