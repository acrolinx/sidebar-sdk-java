/**
 * Copyright (c) 2020-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.jfx;

import javafx.scene.web.WebView;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.pojo.settings.CheckModeType;

public class AcrolinxSidebarPluginWithoutCheckSelectionSupport extends AcrolinxSidebarPlugin
{
    public AcrolinxSidebarPluginWithoutCheckSelectionSupport(final AcrolinxIntegration client, final WebView webView)
    {
        super(client, webView);
    }

    public synchronized void requestGlobalCheck()
    {
        logger.info("requestGlobalCheck is called.");
        if (!this.client.getInitParameters().getSupported().isBatchChecking()) {
            runInteractiveCheckWithoutCheckSelection();
        } else {
            CheckModeType checkModeRequested = client.getCheckModeOnCheckRequested();
            if (CheckModeType.BACKGROUNDCHECK.equals(checkModeRequested)) {
                logger.info("runBatchCheck is called.");
                runBatchCheck();
            } else {
                logger.info("runInteractiveCheckWithoutCheckSelection is called.");
                runInteractiveCheckWithoutCheckSelection();
            }
        }
    }
}
