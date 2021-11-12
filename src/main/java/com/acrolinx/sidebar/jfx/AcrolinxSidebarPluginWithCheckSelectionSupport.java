/**
 * Copyright (c) 2020-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.jfx;

import javafx.scene.web.WebView;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.pojo.settings.CheckModeType;

import netscape.javascript.JSObject;

public class AcrolinxSidebarPluginWithCheckSelectionSupport extends AcrolinxSidebarPlugin
{
    public AcrolinxSidebarPluginWithCheckSelectionSupport(final AcrolinxIntegration client, final WebView webView)
    {
        super(client, webView);
    }

    public synchronized void requestGlobalCheck(final JSObject o)
    {
        if (!this.client.getInitParameters().getSupported().isBatchChecking()) {
            runInteractiveCheckWithCheckSelection(o);
        } else {
            CheckModeType checkModeRequested = ((AcrolinxIntegration) client).getCheckModeOnCheckRequested();
            if (CheckModeType.BACKGROUNDCHECK.equals(checkModeRequested)) {
                runBatchCheck();
            } else {
                runInteractiveCheckWithCheckSelection(o);
            }
        }
    }
}
