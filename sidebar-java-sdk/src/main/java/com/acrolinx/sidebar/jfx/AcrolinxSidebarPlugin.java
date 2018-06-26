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

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import javafx.scene.web.WebView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.pojo.SidebarError;
import com.acrolinx.sidebar.pojo.document.AbstractMatch;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatch;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatchWithReplacement;
import com.acrolinx.sidebar.pojo.document.CheckResult;
import com.acrolinx.sidebar.pojo.document.CheckedDocumentPart;
import com.acrolinx.sidebar.pojo.document.IntRange;
import com.acrolinx.sidebar.pojo.settings.AcrolinxPluginConfiguration;
import com.acrolinx.sidebar.pojo.settings.AcrolinxSidebarInitParameter;
import com.acrolinx.sidebar.pojo.settings.CheckOptions;
import com.acrolinx.sidebar.pojo.settings.DocumentSelection;
import com.acrolinx.sidebar.pojo.settings.InputFormat;
import com.acrolinx.sidebar.pojo.settings.RequestDescription;
import com.acrolinx.sidebar.pojo.settings.SidebarConfiguration;
import com.acrolinx.sidebar.utils.LogMessages;
import com.acrolinx.sidebar.utils.SidebarUtils;
import com.google.common.base.Preconditions;

import netscape.javascript.JSObject;

/**
 * For internal use.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
abstract class AcrolinxSidebarPlugin
{
    final AcrolinxIntegration client;
    private final AtomicReference<String> currentDocumentReference = new AtomicReference<>("");
    private final AtomicReference<String> lastCheckedDocument = new AtomicReference<>("");
    private final AtomicReference<String> currentlyCheckedDocument = new AtomicReference<>("");
    private final AtomicReference<String> currentCheckId = new AtomicReference<>("");
    private final AtomicReference<InputFormat> inputFormatRef = new AtomicReference<>();
    private final AtomicReference<String> lastCheckedDocumentReference = new AtomicReference<>("");
    private final AtomicReference<List<IntRange>> checkSelectionRange = new AtomicReference<>();
    private final AtomicReference<AcrolinxSidebarInitParameter> initParameters = new AtomicReference<>();
    private volatile WebView webView;

    final Logger logger = LoggerFactory.getLogger(AcrolinxSidebarPlugin.class);

    protected Instant checkStartedTime;

    public AcrolinxSidebarPlugin(final AcrolinxIntegration client, final WebView webView)
    {
        Preconditions.checkNotNull(client, "Workspace should not be null");
        Preconditions.checkNotNull(client.getEditorAdapter(), "EditorAdapter should not be null");
        this.client = client;
        this.webView = webView;
        logger.debug("Injecting Acrolinx Plugin.");
        JFXUtils.invokeInJFXThread(() -> {
            try {
                getWindowObject().setMember("acrolinxPlugin", this);
            } catch (final Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    private JSObject getWindowObject()
    {
        logger.info("Get window object from webview...");
        JSObject jsobj = (JSObject) webView.getEngine().executeScript("window");
        int count = 0;
        while ((count < 6) && (jsobj == null)) {
            try {
                logger.info("Window Object not present, retrying ...");
                TimeUnit.MILLISECONDS.sleep(500);
                jsobj = (JSObject) webView.getEngine().executeScript("window");
                count++;
            } catch (final InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return jsobj;
    }

    private AcrolinxIntegration getClient()
    {
        return client;
    }

    public synchronized void requestInit()
    {
        logger.debug("Requesting init sidebar: " + client.getInitParameters().toString());
        this.initParameters.set(client.getInitParameters());
        JFXUtils.invokeInJFXThread(() -> {
            try {
                getWindowObject().eval("acrolinxSidebar.init(" + this.initParameters.get().toString() + ")");
            } catch (final Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    public synchronized void onInitFinished(final JSObject o)
    {
        final Optional<SidebarError> initResult = JSToJavaConverter.getAcrolinxInitResultFromJSObject(o);
        if (initResult.isPresent()) {
            logger.error(initResult.get().getMessage());
        }
        client.onInitFinished(initResult);
    }

    public void configureSidebar(final SidebarConfiguration sidebarConfiguration)
    {
        logger.debug("Configuring Sidebar: " + sidebarConfiguration.toString());
        JFXUtils.invokeInJFXThread(() -> {
            try {
                getWindowObject().eval("acrolinxSidebar.configure(" + sidebarConfiguration.toString() + ")");
            } catch (final Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    public synchronized void runCheck(final boolean selectionEnabled)
    {
        final CheckOptions checkOptions = getCheckSettingsFromClient(selectionEnabled);
        currentlyCheckedDocument.set(client.getEditorAdapter().getContent());
        JFXUtils.invokeInJFXThread(() -> {
            try {
                logger.debug(checkOptions.toString());
                final String nameVariableCheckText = "checkText";
                final JSObject jsObject = getWindowObject();
                jsObject.setMember(nameVariableCheckText, currentlyCheckedDocument.get());
                jsObject.eval("acrolinxSidebar.checkGlobal(checkText," + checkOptions.toString() + ");");
            } catch (final Exception e) {
                logger.error(e.getMessage(), e);
                onGlobalCheckRejected();
            }
        });
    }

    public synchronized void onCheckResult(final JSObject o)
    {
        final Instant checkEndedTime = Instant.now();
        LogMessages.logCheckFinishedWithDurationTime(logger,
                Duration.between(checkStartedTime, checkEndedTime).toMillis());
        final CheckResult checkResult = JSToJavaConverter.getCheckResultFromJSObject(o);
        if (checkResult == null) {
            logger.info("Check finished with errors.");
        } else {
            currentCheckId.set(checkResult.getCheckedDocumentPart().getCheckId());
            lastCheckedDocumentReference.set(currentDocumentReference.get());
            lastCheckedDocument.set(currentlyCheckedDocument.get());
            client.onCheckResult(checkResult);
        }
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

    public void invalidateRangesForMatches(final List<? extends AbstractMatch> matches)
    {
        final List<CheckedDocumentPart> invalidDocumentParts = matches.stream().map((match) -> new CheckedDocumentPart(
                currentCheckId.get(),
                new IntRange(match.getRange().getMinimumInteger(), match.getRange().getMaximumInteger()))).collect(
                        Collectors.toList());
        invalidateRanges(invalidDocumentParts);
    }

    public synchronized void openWindow(final JSObject o)
    {
        final String url = o.getMember("url").toString();
        SidebarUtils.openWebPageInDefaultBrowser(url);
    }

    public void openLogFile()
    {
        SidebarUtils.openLogFile();
    }

    private CheckOptions getCheckSettingsFromClient(final boolean includeCheckSelectionRanges)
    {
        inputFormatRef.set(client.getEditorAdapter().getInputFormat());
        currentDocumentReference.set(client.getEditorAdapter().getDocumentReference());
        DocumentSelection selection = null;
        if (includeCheckSelectionRanges) {
            logger.debug("Including check selection ranges.");
            final List<IntRange> currentSelection = client.getEditorAdapter().getCurrentSelection();
            checkSelectionRange.set(currentSelection);
            if (currentSelection != null) {
                selection = new DocumentSelection(checkSelectionRange.get());
            }
        }
        return new CheckOptions(new RequestDescription(currentDocumentReference.get()), inputFormatRef.get(),
                selection);
    }

    public void onGlobalCheckRejected()
    {
        LogMessages.logCheckRejected(logger);
        JFXUtils.invokeInJFXThread(() -> {
            try {
                getWindowObject().eval("acrolinxSidebar.onGlobalCheckRejected();");
            } catch (final Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    private static String buildStringOfCheckedDocumentRanges(
            final java.util.List<CheckedDocumentPart> checkedDocumentParts)
    {
        return checkedDocumentParts.stream().map(CheckedDocumentPart::getAsJS).collect(Collectors.joining(", "));
    }

    public void invalidateRanges(final List<CheckedDocumentPart> invalidCheckedDocumentRanges)
    {
        final String js = buildStringOfCheckedDocumentRanges(invalidCheckedDocumentRanges);
        JFXUtils.invokeInJFXThread(() -> {
            try {
                getWindowObject().eval("acrolinxSidebar.invalidateRanges([" + js + "])");
            } catch (final Exception e) {
                logger.error(e.getMessage(), e);
            }
        });
    }

    public String getLastCheckedDocumentReference()
    {
        if ((this.lastCheckedDocumentReference.get() != null) && !"".equals(this.lastCheckedDocumentReference.get())) {
            return this.lastCheckedDocumentReference.get();
        }
        return null;
    }

    public String getLastCheckedDocument()
    {
        if ((this.lastCheckedDocument.get() != null) && !"".equals(this.lastCheckedDocument.get())) {
            return this.lastCheckedDocument.get();
        }
        return null;
    }

    public void configure(final AcrolinxPluginConfiguration configuration)
    {
        // Not used ...
    }

}
