/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.jfx;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.InputAdapterInterface;
import com.acrolinx.sidebar.pojo.SidebarError;
import com.acrolinx.sidebar.pojo.document.AbstractMatch;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatch;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatchWithReplacement;
import com.acrolinx.sidebar.pojo.document.CheckContent;
import com.acrolinx.sidebar.pojo.document.CheckResult;
import com.acrolinx.sidebar.pojo.document.CheckedDocumentPart;
import com.acrolinx.sidebar.pojo.document.IntRange;
import com.acrolinx.sidebar.pojo.document.externalcontent.ExternalContent;
import com.acrolinx.sidebar.pojo.settings.AcrolinxPluginConfiguration;
import com.acrolinx.sidebar.pojo.settings.AcrolinxSidebarInitParameter;
import com.acrolinx.sidebar.pojo.settings.BatchCheckRequestOptions;
import com.acrolinx.sidebar.pojo.settings.CheckOptions;
import com.acrolinx.sidebar.pojo.settings.DocumentSelection;
import com.acrolinx.sidebar.pojo.settings.InputFormat;
import com.acrolinx.sidebar.pojo.settings.RequestDescription;
import com.acrolinx.sidebar.pojo.settings.SidebarConfiguration;
import com.acrolinx.sidebar.pojo.settings.SidebarMessage;
import com.acrolinx.sidebar.utils.LogMessages;
import com.acrolinx.sidebar.utils.SidebarUtils;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import javafx.scene.web.WebView;
import javax.annotation.Nullable;
import netscape.javascript.JSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** For internal use. */
abstract class AcrolinxSidebarPlugin {
  static final Logger logger = LoggerFactory.getLogger(AcrolinxSidebarPlugin.class);

  final AcrolinxIntegration acrolinxIntegration;
  private final AtomicReference<String> currentDocumentReference = new AtomicReference<>("");
  private final AtomicReference<String> lastCheckedDocument = new AtomicReference<>("");
  private final AtomicReference<ExternalContent> lastCheckedExternalContent =
      new AtomicReference<>();
  private final AtomicReference<String> currentlyCheckedDocument = new AtomicReference<>("");
  private final AtomicReference<ExternalContent> currentlyCheckedExternalContent =
      new AtomicReference<>();
  private final AtomicReference<String> currentCheckId = new AtomicReference<>("");
  private final AtomicReference<InputFormat> inputFormatRef = new AtomicReference<>();
  private final AtomicReference<String> lastCheckedDocumentReference = new AtomicReference<>("");
  private final AtomicReference<List<IntRange>> checkSelectionRange = new AtomicReference<>();
  private final AtomicReference<AcrolinxSidebarInitParameter> initParameters =
      new AtomicReference<>();
  Instant checkStartedTime;
  private WebView webView;
  final ExecutorService executorService = Executors.newFixedThreadPool(1);

  AcrolinxSidebarPlugin(final AcrolinxIntegration acrolinxIntegration, final WebView webView) {
    Objects.requireNonNull(acrolinxIntegration, "Workspace should not be null");
    Objects.requireNonNull(
        acrolinxIntegration.getEditorAdapter(), "EditorAdapter should not be null");

    this.acrolinxIntegration = acrolinxIntegration;
    this.webView = webView;
    logger.debug("Injecting Acrolinx Plugin.");
    JFXUtils.invokeInJFXThread(
        () -> {
          try {
            getWindowObject().setMember("acrolinxPlugin", this);
          } catch (final Exception e) {
            logger.error("", e);
          }
        });
  }

  private static String buildStringOfCheckedDocumentRanges(
      final java.util.List<CheckedDocumentPart> checkedDocumentParts) {
    return checkedDocumentParts.stream()
        .map(CheckedDocumentPart::getAsJS)
        .collect(Collectors.joining(", "));
  }

  JSObject getWindowObject() {
    logger.info("Get window object from webview...");
    JSObject jsobj = null;
    int count = 0;

    while ((count < 6) && (jsobj == null)) {
      try {
        logger.info("Fetching Window object. Attempt: {}", count);
        count++;
        TimeUnit.MILLISECONDS.sleep(500);
        jsobj = (JSObject) webView.getEngine().executeScript("window");
      } catch (final InterruptedException e) {
        logger.error("", e);
        Thread.currentThread().interrupt();
      } catch (final Exception e) {
        // Window object might not be available in the first attempt and
        // throws netscape.javascript.JSException: JavaScript execution terminated.
        // The object becomes available on the next try
        logger.warn("Window object not available. Trying again. Attempt: {}", count);
      }
    }

    return jsobj;
  }

  public synchronized void requestInit() {
    logger.debug("Requesting init sidebar: {}", acrolinxIntegration.getInitParameters());
    this.initParameters.set(acrolinxIntegration.getInitParameters());
    JFXUtils.invokeInJFXThread(
        () -> {
          try {
            getWindowObject()
                .eval("acrolinxSidebar.init(" + this.initParameters.get().toString() + ")");
          } catch (final Exception e) {
            logger.error("", e);
          }
        });
  }

  public synchronized void onInitFinished(final JSObject jsObject) {
    final Optional<SidebarError> initResult =
        JSToJavaConverter.getAcrolinxInitResultFromJSObject(jsObject);
    initResult.ifPresent(sidebarError -> logger.error("{}", sidebarError.getErrorCode()));
    acrolinxIntegration.onInitFinished(initResult);
  }

  public void configureSidebar(final SidebarConfiguration sidebarConfiguration) {
    logger.debug("Configuring Sidebar: {}", sidebarConfiguration);
    JFXUtils.invokeInJFXThread(
        () -> {
          try {
            getWindowObject()
                .eval("acrolinxSidebar.configure(" + sidebarConfiguration.toString() + ")");
          } catch (final Exception e) {
            logger.error("", e);
          }
        });
  }

  public synchronized void runCheck(
      final boolean selectionEnabled, final CheckContent checkContent) {
    final CheckOptions checkOptions =
        getCheckSettingsFromClient(selectionEnabled, checkContent.getExternalContent());

    currentlyCheckedDocument.set(checkContent.getContent());
    currentlyCheckedExternalContent.set(checkContent.getExternalContent());

    JFXUtils.invokeInJFXThread(
        () -> {
          try {
            logger.debug(checkOptions.toString());
            final String nameVariableCheckText = "checkText";
            final JSObject jsObject = getWindowObject();
            jsObject.setMember(nameVariableCheckText, checkContent.getContent());
            jsObject.eval(
                "acrolinxSidebar.checkGlobal(checkText," + checkOptions.toString() + ");");
          } catch (final Exception e) {
            logger.error("", e);
            onGlobalCheckRejected();
          }
        });
  }

  public void showSidebarMessage(final SidebarMessage sidebarMessage) {
    logger.debug("Message to Sidebar: {}", sidebarMessage);
    JFXUtils.invokeInJFXThread(
        () -> {
          try {
            getWindowObject()
                .eval("acrolinxSidebar.showMessage(" + sidebarMessage.toString() + ")");
          } catch (final Exception e) {
            logger.error("", e);
          }
        });
  }

  public synchronized void onCheckResult(final JSObject jsObject) {
    final Instant checkEndedTime = Instant.now();
    LogMessages.logCheckFinishedWithDurationTime(
        logger, Duration.between(checkStartedTime, checkEndedTime));
    final CheckResult checkResult = JSToJavaConverter.getCheckResultFromJSObject(jsObject);

    if (checkResult == null) {
      logger.info("Check finished with errors.");
    } else {
      currentCheckId.set(checkResult.getCheckedDocumentPart().getCheckId());
      lastCheckedDocumentReference.set(currentDocumentReference.get());
      lastCheckedDocument.set(currentlyCheckedDocument.get());
      lastCheckedExternalContent.set(currentlyCheckedExternalContent.get());
      acrolinxIntegration.onCheckResult(checkResult);
    }
  }

  public synchronized void selectRanges(final String checkId, final JSObject jsObject) {
    LogMessages.logSelectingRange(logger);
    final List<AcrolinxMatch> acrolinxMatches =
        JSToJavaConverter.getAcrolinxMatchFromJSObject(jsObject);

    acrolinxIntegration.getEditorAdapter().selectRanges(checkId, acrolinxMatches);
  }

  public synchronized void replaceRanges(final String checkId, final JSObject jsObject) {
    LogMessages.logReplacingRange(logger);
    final List<AcrolinxMatchWithReplacement> matches =
        JSToJavaConverter.getAcrolinxMatchWithReplacementFromJSObject(jsObject);
    acrolinxIntegration.getEditorAdapter().replaceRanges(checkId, matches);
  }

  public void invalidateRangesForMatches(final List<? extends AbstractMatch> matches) {
    final List<CheckedDocumentPart> invalidDocumentParts =
        matches.stream()
            .map(
                match -> {
                  if (((AcrolinxMatch) match).getExternalContentMatches() != null) {
                    return new CheckedDocumentPart(
                        currentCheckId.get(),
                        new IntRange(
                            match.getRange().getMinimumInteger(),
                            match.getRange().getMaximumInteger()),
                        ((AcrolinxMatch) match).getExternalContentMatches());
                  }

                  return new CheckedDocumentPart(
                      currentCheckId.get(),
                      new IntRange(
                          match.getRange().getMinimumInteger(),
                          match.getRange().getMaximumInteger()));
                })
            .collect(Collectors.toList());
    invalidateRanges(invalidDocumentParts);
  }

  public synchronized void openWindow(final JSObject jsObject) {
    final String url = jsObject.getMember("url").toString();
    SidebarUtils.openWebPageInDefaultBrowser(url);
  }

  public void openLogFile() {
    SidebarUtils.openLogFile();
  }

  private CheckOptions getCheckSettingsFromClient(
      final boolean includeCheckSelectionRanges, @Nullable ExternalContent externalContent) {
    inputFormatRef.set(acrolinxIntegration.getEditorAdapter().getInputFormat());
    currentDocumentReference.set(acrolinxIntegration.getEditorAdapter().getDocumentReference());
    DocumentSelection documentSelection = null;

    if (includeCheckSelectionRanges) {
      logger.debug("Including check selection ranges.");
      final List<IntRange> currentSelection =
          acrolinxIntegration.getEditorAdapter().getCurrentSelection();
      checkSelectionRange.set(currentSelection);

      if (currentSelection != null) {
        documentSelection = new DocumentSelection(checkSelectionRange.get());
      }
    }

    return new CheckOptions(
        new RequestDescription(currentDocumentReference.get()),
        inputFormatRef.get(),
        documentSelection,
        externalContent);
  }

  CheckContent getCheckContentFromClient() {
    final InputAdapterInterface editorAdapter = acrolinxIntegration.getEditorAdapter();
    return new CheckContent(editorAdapter.getContent(), editorAdapter.getExternalContent());
  }

  public void onGlobalCheckRejected() {
    LogMessages.logCheckRejected(logger);
    JFXUtils.invokeInJFXThread(
        () -> {
          try {
            getWindowObject().eval("acrolinxSidebar.onGlobalCheckRejected();");
          } catch (final Exception e) {
            logger.error("", e);
          }
        });
  }

  public void invalidateRanges(final List<CheckedDocumentPart> invalidCheckedDocumentRanges) {
    final String js = buildStringOfCheckedDocumentRanges(invalidCheckedDocumentRanges);
    JFXUtils.invokeInJFXThread(
        () -> {
          try {
            getWindowObject().eval("acrolinxSidebar.invalidateRanges([" + js + "])");
          } catch (final Exception e) {
            logger.error("", e);
          }
        });
  }

  public String getLastCheckedDocumentReference() {
    if ((this.lastCheckedDocumentReference.get() != null)
        && !"".equals(this.lastCheckedDocumentReference.get())) {
      return this.lastCheckedDocumentReference.get();
    }

    return null;
  }

  public String getLastCheckedDocument() {
    if ((this.lastCheckedDocument.get() != null) && !"".equals(this.lastCheckedDocument.get())) {
      return this.lastCheckedDocument.get();
    }

    return null;
  }

  public ExternalContent getLastCheckedExternalContent() {
    if (this.lastCheckedExternalContent.get() != null) {
      return this.lastCheckedExternalContent.get();
    }

    return null;
  }

  public void configure(final AcrolinxPluginConfiguration configuration) {
    // Not used ...
  }

  public synchronized void requestCheckForDocumentInBatch(String documentIdentifier) {
    logger.debug("requestCheckForDocumentInBatch is called.");
    final String contentToCheck = acrolinxIntegration.getContentForDocument(documentIdentifier);
    CheckOptions referenceCheckOptions =
        acrolinxIntegration.getCheckOptionsForDocument(documentIdentifier);
    this.checkDocumentInBatch(documentIdentifier, contentToCheck, referenceCheckOptions);
  }

  public synchronized void openDocumentInEditor(String documentIdentifier) {
    logger.debug("openDocumentInEditor is called...");
    Future<Boolean> openDocumentFuture =
        executorService.submit(
            () -> {
              boolean documentIsOpen = acrolinxIntegration.openDocumentInEditor(documentIdentifier);

              if (!documentIsOpen) {
                // ToDo: Send a message to the sidebar
              }

              return documentIsOpen;
            });

    logger.debug(
        "Opening document in Future task. Future task is running: {}",
        !openDocumentFuture.isDone());
  }

  public synchronized void initBatchCheck(
      final List<BatchCheckRequestOptions> batchCheckRequestOptions) {
    logger.info("initBatchCheck...");
    JFXUtils.invokeInJFXThread(
        () -> {
          try {
            final JSObject windowObject = getWindowObject();
            windowObject.eval(
                "acrolinxSidebar.initBatchCheck(" + batchCheckRequestOptions.toString() + ")");
          } catch (final Exception e) {
            logger.error("", e);
          }
        });
  }

  public synchronized void checkDocumentInBatch(
      final String documentIdentifier,
      final String documentContent,
      final CheckOptions checkOptions) {
    logger.debug("checkDocumentInBatch is called.");
    JFXUtils.invokeInJFXThread(
        () -> {
          try {
            final String nameVariableReference = "documentIdentifier";
            final String nameVariableContent = "documentContent";
            final JSObject jsObject = getWindowObject();
            jsObject.setMember(nameVariableReference, documentIdentifier);
            jsObject.setMember(nameVariableContent, documentContent);
            jsObject.eval(
                "acrolinxSidebar.checkDocumentInBatch(documentIdentifier, documentContent, "
                    + checkOptions.toString()
                    + ");");
          } catch (final Exception e) {
            logger.error("", e);
          }
        });
  }
}
