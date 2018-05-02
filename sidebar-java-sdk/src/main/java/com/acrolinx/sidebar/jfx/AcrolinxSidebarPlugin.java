/*
 * Copyright (c) 2016-2017 Acrolinx GmbH
 */

/**
 * (c) 2015 Acrolinx GmbH. All rights reserved.
 *
 * Created 19.05.2015
 *
 * @author ralf
 */

package com.acrolinx.sidebar.jfx;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.pojo.SidebarError;
import com.acrolinx.sidebar.pojo.document.*;
import com.acrolinx.sidebar.pojo.settings.*;
import com.acrolinx.sidebar.utils.LogMessages;
import com.acrolinx.sidebar.utils.SidebarUtils;
import com.google.common.base.Preconditions;
import javafx.application.Platform;
import netscape.javascript.JSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * For internal use.
 */
@SuppressWarnings({"unused", "WeakerAccess"}) abstract class AcrolinxSidebarPlugin
{
    final AcrolinxIntegration client;
    private final JSObject jsobj;
    private final AtomicReference<String> currentDocumentReference = new AtomicReference<>("");
    private final AtomicReference<String> lastCheckedDocument = new AtomicReference<>("");
    private final AtomicReference<String> currentCheckId = new AtomicReference<>("");
    private final AtomicReference<InputFormat> inputFormatRef = new AtomicReference<>();
    private final AtomicReference<String> documentReference = new AtomicReference<>("");
    private final AtomicReference<List<IntRange>> checkSelectionRange = new AtomicReference<>();
    private final AtomicReference<AcrolinxSidebarInitParameter> initParameters = new AtomicReference<>();

    final Logger logger = LoggerFactory.getLogger(AcrolinxSidebarPlugin.class);

    protected Instant checkStartedTime;

    public AcrolinxSidebarPlugin(final AcrolinxIntegration client, final JSObject jsobj)
    {
        Preconditions.checkNotNull(jsobj, "JsObject should not be null");
        Preconditions.checkNotNull(client, "Workspace should not be null");
        Preconditions.checkNotNull(client.getEditorAdapter(), "EditorAdapter should not be null");

        this.client = client;
        this.jsobj = jsobj;
    }

    private AcrolinxIntegration getClient()
    {
        return client;
    }

    public synchronized void requestInit()
    {
        logger.debug("Requesting init sidebar: " + client.getInitParameters().toString());
        this.initParameters.set(client.getInitParameters());
        Platform.runLater(() -> jsobj.eval("acrolinxSidebar.init(" + this.initParameters.get().toString() + ")"));
    }

    public synchronized void onInitFinished(final JSObject o)
    {
        final Optional<SidebarError> initResult = JSToJavaConverter.getAcrolinxInitResultFromJSObject(o);
        if (initResult.isPresent()) {
            logger.error(initResult.get().getMessage());
        }
        client.onInitFinished(initResult);
    }

    public synchronized void configureSidebar(SidebarConfiguration sidebarConfiguration)
    {
        logger.debug("Configuring Sidebar: " + sidebarConfiguration.toString());
        Platform.runLater(() -> jsobj.eval("acrolinxSidebar.configure(" + sidebarConfiguration.toString() + ")"));
    }

    public synchronized void runCheck(boolean selectionEnabled)
    {
        final CheckOptions checkOptions = getCheckSettingsFromClient(selectionEnabled);
        lastCheckedDocument.set(client.getEditorAdapter().getContent());
        logger.debug("Got check content.");
        logger.debug(lastCheckedDocument.get());
        Platform.runLater(() -> {
            logger.debug(checkOptions.toString());
            logger.debug("jsObject is present:" + (jsobj != null));
            jsobj.eval("var checkText=" + lastCheckedDocument.get() + ";");
            jsobj.eval("var checkOptions=" + checkOptions.toString() + ";");
            jsobj.eval("acrolinxSidebar.checkGlobal(checkText, checkOptions);");
            logger.debug("Run check was sent...");
        });
    }

    public synchronized void onCheckResult(final JSObject o)
    {
        Instant checkEndedTime = Instant.now();
        LogMessages.logCheckFinishedWithDurationTime(logger,
                Duration.between(checkStartedTime, checkEndedTime).toMillis());
        final CheckResult checkResult = JSToJavaConverter.getCheckResultFromJSObject(o);
        currentCheckId.set(checkResult.getCheckedDocumentPart().getCheckId());
        currentDocumentReference.set(documentReference.get());
        client.onCheckResult(checkResult);
    }

    public synchronized void selectRanges(final String checkID, final JSObject o)
    {
        LogMessages.logSelectingRange(logger);
        final List<AcrolinxMatch> matches = JSToJavaConverter.getAcrolinxMatchFromJSObject(o);

        client.getEditorAdapter().selectRanges(checkID, matches);

    }

    public synchronized void replaceRanges(final String checkID, final JSObject o)
    {
        LogMessages.logReplacingRange(logger);
        final List<AcrolinxMatchWithReplacement> matches = JSToJavaConverter.getAcrolinxMatchWithReplacementFromJSObject(
                o);
        client.getEditorAdapter().replaceRanges(checkID, matches);

    }

    public void invalidateRangesForMatches(List<? extends AbstractMatch> matches)
    {
        List<CheckedDocumentPart> invalidDocumentParts = matches.stream().map(
                (match) -> new CheckedDocumentPart(currentCheckId.get(),
                        new IntRange(match.getRange().getMinimumInteger(),
                                match.getRange().getMaximumInteger()))).collect(Collectors.toList());
        invalidateRanges(invalidDocumentParts);
    }

    public synchronized void openWindow(final JSObject o)
    {
        String url = o.getMember("url").toString();
        SidebarUtils.openWebPageInDefaultBrowser(url);
    }

    public void openLogFile()
    {
        SidebarUtils.openLogFile();
    }

    private CheckOptions getCheckSettingsFromClient(boolean includeCheckSelectionRanges)
    {
        inputFormatRef.set(client.getEditorAdapter().getInputFormat());
        documentReference.set(client.getEditorAdapter().getDocumentReference());
        DocumentSelection selection = null;
        if (includeCheckSelectionRanges) {
            logger.debug("Including check selection ranges.");
            List<IntRange> currentSelection = client.getEditorAdapter().getCurrentSelection();
            checkSelectionRange.set(currentSelection);
            if (currentSelection != null) {
                selection = new DocumentSelection(checkSelectionRange.get());
            }
        }
        return new CheckOptions(new RequestDescription(documentReference.get()), inputFormatRef.get(), selection);
    }

    public void onGlobalCheckRejected()
    {
        LogMessages.logCheckRejected(logger);
        Platform.runLater(() -> jsobj.eval("acrolinxSidebar.onGlobalCheckRejected()"));
    }

    private static String buildStringOfCheckedDocumentRanges(java.util.List<CheckedDocumentPart> checkedDocumentParts)
    {
        return checkedDocumentParts.stream().map(CheckedDocumentPart::getAsJS).collect(Collectors.joining(", "));
    }

    public void invalidateRanges(List<CheckedDocumentPart> invalidCheckedDocumentRanges)
    {
        String js = buildStringOfCheckedDocumentRanges(invalidCheckedDocumentRanges);
        Platform.runLater(() -> jsobj.eval("acrolinxSidebar.invalidateRanges([" + js + "])"));
    }

    public String getLastCheckedDocumentReference()
    {
        if (this.documentReference.get() != null && !"".equals(this.documentReference.get())) {
            return this.documentReference.get();
        }
        return null;
    }

    public String getLastCheckedDocument()
    {
        if (this.lastCheckedDocument.get() != null && !"".equals(this.lastCheckedDocument.get())) {
            return this.lastCheckedDocument.get();
        }
        return null;
    }

    public void configure(AcrolinxPluginConfiguration configuration)
    {
        // Not used ...
    }
}
