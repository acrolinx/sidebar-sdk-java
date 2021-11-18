/* Copyright (c) 2016-present Acrolinx GmbH */

/**
 * (c) 2015 Acrolinx GmbH. All rights reserved.
 * <p>
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

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.InputAdapterInterface;
import com.acrolinx.sidebar.adapter.NullEditorAdapter;
import com.acrolinx.sidebar.pojo.SidebarError;
import com.acrolinx.sidebar.pojo.document.*;
import com.acrolinx.sidebar.pojo.document.externalContent.ExternalContent;
import com.acrolinx.sidebar.pojo.settings.*;
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
    final Logger logger = LoggerFactory.getLogger(AcrolinxSidebarPlugin.class);
    private final AtomicReference<String> currentDocumentReference = new AtomicReference<>("");
    private final AtomicReference<String> lastCheckedDocument = new AtomicReference<>("");
    private final AtomicReference<String> currentlyCheckedDocument = new AtomicReference<>("");
    private final AtomicReference<String> currentCheckId = new AtomicReference<>("");
    private final AtomicReference<InputFormat> inputFormatRef = new AtomicReference<>();
    private final AtomicReference<String> lastCheckedDocumentReference = new AtomicReference<>("");
    private final AtomicReference<List<IntRange>> checkSelectionRange = new AtomicReference<>();
    private final AtomicReference<AcrolinxSidebarInitParameter> initParameters = new AtomicReference<>();
    protected Instant checkStartedTime;
    private WebView webView;

    protected AcrolinxSidebarPlugin(final AcrolinxIntegration client, final WebView webView)
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

    private static String buildStringOfCheckedDocumentRanges(
            final java.util.List<CheckedDocumentPart> checkedDocumentParts)
    {
        return checkedDocumentParts.stream().map(CheckedDocumentPart::getAsJS).collect(Collectors.joining(", "));
    }

    protected JSObject getWindowObject()
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
                Thread.currentThread().interrupt();
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

    public synchronized void runCheck(final boolean selectionEnabled, final CheckContent checkContent)
    {
        final CheckOptions checkOptions = getCheckSettingsFromClient(selectionEnabled,
                checkContent.getExternalContent());

        currentlyCheckedDocument.set(checkContent.getContent());
        JFXUtils.invokeInJFXThread(() -> {
            try {
                logger.info(checkOptions.toString());
                final String nameVariableCheckText = "checkText";
                final JSObject jsObject = getWindowObject();
                jsObject.setMember(nameVariableCheckText, checkContent.getContent());
                jsObject.eval("acrolinxSidebar.checkGlobal(checkText," + checkOptions.toString() + ");");
            } catch (final Exception e) {
                logger.error(e.getMessage(), e);
                onGlobalCheckRejected();
            }
        });
    }

    public void showSidebarMessage(final SidebarMessage sidebarMessage)
    {
        logger.debug("Message to Sidebar: " + sidebarMessage.toString());
        JFXUtils.invokeInJFXThread(() -> {
            try {
                getWindowObject().eval("acrolinxSidebar.showMessage(" + sidebarMessage.toString() + ")");
            } catch (final Exception e) {
                logger.error(e.getMessage(), e);
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

    private CheckOptions getCheckSettingsFromClient(final boolean includeCheckSelectionRanges,
            @Nullable ExternalContent externalContent)
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
        return new CheckOptions(new RequestDescription(currentDocumentReference.get()), inputFormatRef.get(), selection,
                externalContent);
    }

    protected CheckContent getCheckContentFromClient()
    {
        final InputAdapterInterface editorAdapter = client.getEditorAdapter();
        return new CheckContent(editorAdapter.getContent(), editorAdapter.getExternalContent());
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

    public synchronized void requestBackgroundCheckForDocument(String documentIdentifier)
    {
        logger.debug("requestBackgroundCheckForRef is called.");
        final String contentToCheck = ((AcrolinxIntegration) client).getContentForDocument(documentIdentifier);
        CheckOptions referenceCheckOptions = ((AcrolinxIntegration) client).getCheckOptionsForDocument(
                documentIdentifier);
        this.checkDocumentInBackground(documentIdentifier, contentToCheck, referenceCheckOptions);
    }

    public synchronized void openDocumentInEditor(String documentIdentifier)
    {
        logger.debug("openDocumentInEditor is called...");
        Boolean referenceIsOpen = ((AcrolinxIntegration) client).openDocumentInEditor(documentIdentifier);
        if (!referenceIsOpen) {
            logger.debug("openDocumentInEditor failed.");
        }
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
    }

    public synchronized void checkDocumentInBackground(final String documentIdentifier, final String documentContent,
            final CheckOptions options)
    {
        logger.debug("checkDocumentInBackground is called.");
        JFXUtils.invokeInJFXThread(() -> {
            try {
                final String nameVariableReference = "documentIdentifier";
                final String nameVariableContent = "documentContent";
                final JSObject jsObject = getWindowObject();
                jsObject.setMember(nameVariableReference, documentIdentifier);
                jsObject.setMember(nameVariableContent, documentContent);
                jsObject.eval("acrolinxSidebar.checkDocumentInBackground(documentIdentifier, documentContent, "
                        + options.toString() + ");");
            } catch (final Exception e) {
                logger.error(e.getMessage(), e);
            }
        });

    }

    private static String buildStringOfCheckedRequestOptions(
            final List<BatchCheckRequestOptions> batchCheckRequestOptions)
    {
        return batchCheckRequestOptions.stream().map(BatchCheckRequestOptions::toString).collect(
                Collectors.joining(", "));
    }

}
