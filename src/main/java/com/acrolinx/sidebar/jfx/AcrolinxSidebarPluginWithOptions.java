/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar.jfx;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.adapter.NullEditorAdapter;
import com.acrolinx.sidebar.pojo.document.CheckContent;
import com.acrolinx.sidebar.pojo.settings.BatchCheckRequestOptions;
import com.acrolinx.sidebar.utils.LogMessages;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Future;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class AcrolinxSidebarPluginWithOptions extends AcrolinxSidebarPlugin {
  public AcrolinxSidebarPluginWithOptions(
      final AcrolinxIntegration acrolinxIntegration, final WebView webView) {
    super(acrolinxIntegration, webView);
  }

  public synchronized void requestGlobalCheck(final JSObject jsObject) {
    LogMessages.logCheckRequested(logger);
    this.checkStartedTime = Instant.now();
    boolean selection = false;
    boolean batchCheck = false;

    if (jsObject != null) {
      if (jsObject.getMember("selection") != null) {
        selection = Boolean.parseBoolean(jsObject.getMember("selection").toString());
      }

      if (jsObject.getMember("batchCheck") != null) {
        batchCheck = Boolean.parseBoolean(jsObject.getMember("batchCheck").toString());
      }
    }

    if (batchCheck) {
      final Future<Boolean> initBatchFuture =
          executorService.submit(
              () -> {
                try {
                  final List<BatchCheckRequestOptions> batchCheckRequestOptions =
                      acrolinxIntegration.extractReferences();
                  initBatchCheck(batchCheckRequestOptions);
                  return true;
                } catch (Exception e) {
                  logger.error("Extracting references in Future Task failed", e);
                  return false;
                }
              });

      logger.debug(
          "Extracting references in Future task. Future task is running: {}",
          !initBatchFuture.isDone());
    } else {
      final CheckContent checkContent = getCheckContentFromClient();
      logger.debug("Fetched check content including external content");

      if ((acrolinxIntegration.getEditorAdapter() != null)
          && !(acrolinxIntegration.getEditorAdapter() instanceof NullEditorAdapter)
          && (checkContent.getContent() != null)) {
        logger.debug("Editor is ready for running a check");
        runCheck(selection, checkContent);
      } else {
        logger.warn("Current File Editor not supported for checking or no file present.");
        onGlobalCheckRejected();
      }
    }
  }
}
