/**
 * Copyright (c) 2020-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.jfx;

import javafx.scene.web.WebView;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.AcrolinxIntegrationWithBatchSupport;
import com.acrolinx.sidebar.pojo.settings.CheckModeType;

public class AcrolinxSidebarPluginWithBatchSupportAndNoCheckSelection extends AcrolinxSidebarPluginWithBatchSupport
{
    public AcrolinxSidebarPluginWithBatchSupportAndNoCheckSelection(final AcrolinxIntegrationWithBatchSupport client,
            final WebView webView)
    {
        super(client, webView);
    }

    public synchronized void requestGlobalCheck()
    {
        if (!this.client.getInitParameters().getSupported().isBatchChecking()) {
            runInteractiveCheckWithoutCheckSelection();
        } else {
            CheckModeType checkModeRequested = ((AcrolinxIntegrationWithBatchSupport) client).getCheckModeOnCheckRequested();
            if (CheckModeType.BACKGROUNDCHECK.equals(checkModeRequested)) {
                runBatchCheck();
            } else {
                runInteractiveCheckWithoutCheckSelection();
            }
        }
    }
}
