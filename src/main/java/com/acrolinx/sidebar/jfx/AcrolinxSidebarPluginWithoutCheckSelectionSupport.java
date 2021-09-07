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
            if(CheckModeType.BACKGROUNDCHECK.equals(checkModeRequested)) {
                runBatchCheck();
            } else {
                runInteractiveCheckWithoutCheckSelection();
            }
        }
    }
}
