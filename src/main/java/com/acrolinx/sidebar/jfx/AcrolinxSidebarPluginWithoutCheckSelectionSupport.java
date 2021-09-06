/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.jfx;

import com.acrolinx.sidebar.pojo.settings.CheckModeType;
import javafx.scene.web.WebView;

import com.acrolinx.sidebar.AcrolinxIntegration;

@SuppressWarnings("WeakerAccess")
public class AcrolinxSidebarPluginWithoutCheckSelectionSupport extends AcrolinxSidebarPlugin
{

    public AcrolinxSidebarPluginWithoutCheckSelectionSupport(final AcrolinxIntegration client, final WebView webView)
    {
        super(client, webView);
    }

    public synchronized void requestGlobalCheck()
    {


        if(!this.client.getInitParameters().getSupported().isBatchChecking()) {
            runInteractiveCheckWithoutCheckSelection();
        } else {
            CheckModeType checkModeRequested = client.getCheckModeOnCheckRequested();
            if(CheckModeType.BATCHDITA.equals(checkModeRequested)) {
                runBatchCheck();
            } else {
                runInteractiveCheckWithoutCheckSelection();
            }
        }

//        LogMessages.logCheckRequested(logger);
//        this.checkStartedTime = Instant.now();
//        final CheckContent checkContent = getCheckContentFromClient();
//        logger.debug("Fetched check content including external content");
//        if ((client.getEditorAdapter() != null) && !(client.getEditorAdapter() instanceof NullEditorAdapter)
//                && (checkContent.getContent() != null)) {
//            runCheck(false, checkContent);
//        } else {
//            logger.warn("Current File Editor not supported for checking or no file present.");
//            onGlobalCheckRejected();
//        }

    }
}
