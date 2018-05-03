/*
 * Copyright (c) 2017 Acrolinx GmbH
 */

package com.acrolinx.sidebar.jfx;

import java.time.Instant;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.adapter.NullEditorAdapter;
import com.acrolinx.sidebar.utils.LogMessages;

import jdk.nashorn.api.scripting.JSObject;

@SuppressWarnings("WeakerAccess")
public class AcrolinxSidebarPluginWithoutCheckSelectionSupport extends AcrolinxSidebarPlugin
{

    public AcrolinxSidebarPluginWithoutCheckSelectionSupport(AcrolinxIntegration client, JSObject jsobj)
    {
        super(client, jsobj);
    }

    public synchronized void requestGlobalCheck()
    {
        LogMessages.logCheckRequested(logger);
        this.checkStartedTime = Instant.now();
        if (client.getEditorAdapter() != null && !(client.getEditorAdapter() instanceof NullEditorAdapter)
                && client.getEditorAdapter().getContent() != null) {
            runCheck(false);
        } else {
            logger.warn("Current File Editor not supported for checking or no file present.");
            onGlobalCheckRejected();
        }

    }
}
