/**
 * Copyright (c) 2020-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.jfx;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import javafx.scene.web.WebView;

import com.acrolinx.sidebar.AcrolinxIntegrationWithBatchSupport;
import com.acrolinx.sidebar.adapter.NullEditorAdapter;
import com.acrolinx.sidebar.pojo.document.CheckContent;
import com.acrolinx.sidebar.pojo.settings.BatchCheckRequestOptions;
import com.acrolinx.sidebar.pojo.settings.CheckOptions;
import com.acrolinx.sidebar.utils.LogMessages;

import netscape.javascript.JSObject;

public class AcrolinxSidebarPluginWithBatchSupport extends AcrolinxSidebarPlugin
{

    public AcrolinxSidebarPluginWithBatchSupport(AcrolinxIntegrationWithBatchSupport client, WebView webView)
    {
        super(client, webView);
    }

    public synchronized void requestBackgroundCheckForRef(String ditaTopicReference)
    {
        // TODO: Wait for the content ?
        logger.info("requestBackgroundCheckForRef is called...");
        final String contentToCheck = ((AcrolinxIntegrationWithBatchSupport) client).getContentForReference(
                ditaTopicReference);
        CheckOptions referenceCheckOptions = ((AcrolinxIntegrationWithBatchSupport) client).getCheckOptionsForReference(
                ditaTopicReference);
        this.checkReferenceInBackground(ditaTopicReference, contentToCheck, referenceCheckOptions);
    }

    public synchronized void openReferenceInEditor(String reference)
    {
        // TODO: Wait for the editor to open the reference ?
        logger.info("openReferenceInEditor is called...");
        ((AcrolinxIntegrationWithBatchSupport) client).openReferenceInEditor(reference);
        this.onReferenceLoadedInEditor(reference);
    }

    public synchronized void initBatchCheck(final List<BatchCheckRequestOptions> batchCheckRequestOptions)
    {
        final String js = buildStringOfCheckedRequestOptions(batchCheckRequestOptions);
        logger.info("initBatchCheck...");
        JFXUtils.invokeInJFXThread(() -> {
            try {
                getWindowObject().eval("acrolinxSidebar.initBatchCheck([" + js + "])");
            } catch (final Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
        logger.info("end of initBatchCheck...");
    }

    // TODO: Multiple parameter passing ?
    public synchronized void checkReferenceInBackground(final String reference, final String documentContent,
            final CheckOptions options)
    {
        logger.info("checkReferenceInBackground is called...");
        JFXUtils.invokeInJFXThread(() -> {
            try {
                final String nameVariableReference = "reference";
                final String nameVariableContent = "documentContent";
                final JSObject jsObject = getWindowObject();
                jsObject.setMember(nameVariableReference, reference);
                jsObject.setMember(nameVariableContent, documentContent);
                jsObject.eval("acrolinxSidebar.checkReferenceInBackground(reference, documentContent, "
                        + options.toString() + ");");
                logger.info("end of checkReferenceInBackground...");
            } catch (final Exception e) {
                logger.info("This is from checkRefernceInBackground");
                logger.error(e.getMessage(), e);
            }
        });

    }

    public synchronized void onReferenceLoadedInEditor(final String reference)
    {
        logger.info("Sidebar onReferenceLoadedInEditor is called...");
        JFXUtils.invokeInJFXThread(() -> {
            try {
                final String nameVariableReference = "reference";
                final JSObject jsObject = getWindowObject();
                jsObject.setMember(nameVariableReference, reference);
                jsObject.eval("acrolinxSidebar.onReferenceLoadedInEditor(reference);");
            } catch (final Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    public synchronized void runBatchCheck()
    {
        List<BatchCheckRequestOptions> batchCheckRequestOptions = ((AcrolinxIntegrationWithBatchSupport) client).extractReferences();
        initBatchCheck(batchCheckRequestOptions);
    }

    public synchronized void runInteractiveCheckWithCheckSelection(final JSObject o)
    {
        LogMessages.logCheckRequested(logger);
        this.checkStartedTime = Instant.now();
        boolean selection = false;
        if (o != null) {
            if (o.getMember("selection") != null) {
                selection = Boolean.parseBoolean(o.getMember("selection").toString());
            }
        }

        final CheckContent checkContent = getCheckContentFromClient();
        logger.debug("Fetched check content including external content");
        if ((client.getEditorAdapter() != null) && !(client.getEditorAdapter() instanceof NullEditorAdapter)
                && (checkContent.getContent() != null)) {
            logger.debug("Editor is ready for running a check");
            runCheck(selection, checkContent);
        } else {
            logger.warn("Current File Editor not supported for checking or no file present.");
            onGlobalCheckRejected();
        }
    }

    public synchronized void runInteractiveCheckWithoutCheckSelection()
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

    private static String buildStringOfCheckedRequestOptions(
            final List<BatchCheckRequestOptions> batchCheckRequestOptions)
    {
        return batchCheckRequestOptions.stream().map(BatchCheckRequestOptions::toString).collect(
                Collectors.joining(", "));
    }
}
