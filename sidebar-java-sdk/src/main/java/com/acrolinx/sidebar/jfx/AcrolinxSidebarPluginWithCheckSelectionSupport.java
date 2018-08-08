/* Copyright (c) 2017-2018 Acrolinx GmbH */

package com.acrolinx.sidebar.jfx;

import java.time.Instant;

import javafx.scene.web.WebView;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.adapter.NullEditorAdapter;
import com.acrolinx.sidebar.utils.LogMessages;

import netscape.javascript.JSObject;

public class AcrolinxSidebarPluginWithCheckSelectionSupport extends AcrolinxSidebarPlugin
{
    public AcrolinxSidebarPluginWithCheckSelectionSupport(final AcrolinxIntegration client, final WebView webView)
    {
        super(client, webView);
    }

    public synchronized void requestGlobalCheck(final JSObject o)
    {
        LogMessages.logCheckRequested(logger);
        this.checkStartedTime = Instant.now();
        boolean selection = false;
        if (o != null) {
            if (o.getMember("selection") != null) {
                selection = Boolean.parseBoolean(o.getMember("selection").toString());
            }
        }
        if ((client.getEditorAdapter() != null) && !(client.getEditorAdapter() instanceof NullEditorAdapter)
                && (client.getEditorAdapter().getContent() != null)) {
            logger.debug("Editor is ready for running a check");
            runCheck(selection);
        } else {
            logger.warn("Current File Editor not supported for checking or no file present.");
            onGlobalCheckRejected();
        }

    }

}
