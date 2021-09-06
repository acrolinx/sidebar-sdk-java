/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.jfx;

import com.acrolinx.sidebar.pojo.settings.CheckModeType;
import javafx.scene.web.WebView;

import com.acrolinx.sidebar.AcrolinxIntegration;

import netscape.javascript.JSObject;

public class AcrolinxSidebarPluginWithCheckSelectionSupport extends AcrolinxSidebarPlugin
{
    public AcrolinxSidebarPluginWithCheckSelectionSupport(final AcrolinxIntegration client, final WebView webView)
    {
        super(client, webView);
    }

    public synchronized void requestGlobalCheck(final JSObject o)
    {

        if(!this.client.getInitParameters().getSupported().isBatchChecking()) {
            runInteractiveCheckWithCheckSelection(o);
        } else {
            //getCheckModeOnCheckRequested to be implemented by client
            CheckModeType checkModeRequested = client.getCheckModeOnCheckRequested();
            if(CheckModeType.BATCHDITA.equals(checkModeRequested)) {
                runBatchCheck();
            } else {
                runInteractiveCheckWithCheckSelection(o);
            }
        }
//        LogMessages.logCheckRequested(logger);
//        this.checkStartedTime = Instant.now();
//        boolean selection = false;
//        if (o != null) {
//            if (o.getMember("selection") != null) {
//                selection = Boolean.parseBoolean(o.getMember("selection").toString());
//            }
//        }
//
//        final CheckContent checkContent = getCheckContentFromClient();
//        logger.debug("Fetched check content including external content");
//        if ((client.getEditorAdapter() != null) && !(client.getEditorAdapter() instanceof NullEditorAdapter)
//                && (checkContent.getContent() != null)) {
//            logger.debug("Editor is ready for running a check");
//            runCheck(selection, checkContent);
//        } else {
//            logger.warn("Current File Editor not supported for checking or no file present.");
//            onGlobalCheckRejected();
//        }



    }

}
