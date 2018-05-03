/*
 * Copyright (c) 2017 Acrolinx GmbH
 */

package com.acrolinx.sidebar.jfx;

import java.time.Instant;
import javafx.scene.web.WebEngine;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.adapter.NullEditorAdapter;
import com.acrolinx.sidebar.utils.LogMessages;

import netscape.javascript.JSObject;

public class AcrolinxSidebarPluginWithCheckSelectionSupport extends AcrolinxSidebarPlugin
{
    public AcrolinxSidebarPluginWithCheckSelectionSupport(AcrolinxIntegration client)
    {
        super(client);
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
        if (client.getEditorAdapter() != null && !(client.getEditorAdapter() instanceof NullEditorAdapter)
                && client.getEditorAdapter().getContent() != null) {
            logger.debug("Editor is ready for running a check");
            runCheck(selection);
        } else {
            logger.warn("Current File Editor not supported for checking or no file present.");
            onGlobalCheckRejected();
        }

    }

}
