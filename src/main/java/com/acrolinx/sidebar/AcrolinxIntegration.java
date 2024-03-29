/* Copyright (c) 2018 Acrolinx GmbH */
package com.acrolinx.sidebar;

import com.acrolinx.sidebar.pojo.SidebarError;
import com.acrolinx.sidebar.pojo.document.CheckResult;
import com.acrolinx.sidebar.pojo.settings.AcrolinxSidebarInitParameter;
import com.acrolinx.sidebar.pojo.settings.BatchCheckRequestOptions;
import com.acrolinx.sidebar.pojo.settings.CheckOptions;
import java.util.List;
import java.util.Optional;

/**
 * This interface needs be implemented to integrate Acrolinx with an editor or editing environment.
 * These methods are called by the Acrolinx Sidebar to interact with the editor.
 *
 * <p>Remember to activate logging by using LoggingUtils.setupDefaultLogging() on application/plugin
 * start.
 */
public interface AcrolinxIntegration {
  /**
   * Adapter to extract the text to be checked. It needs to be an implementation of
   * InputAdapterInterface.
   *
   * @see InputAdapterInterface
   */
  InputAdapterInterface getEditorAdapter();

  /**
   * Gets the parameters used to initialize the Acrolinx Sidebar.
   *
   * @see AcrolinxSidebarInitParameter
   */
  AcrolinxSidebarInitParameter getInitParameters();

  /** Notifies the Acrolinx Integration about the checks result. */
  void onCheckResult(CheckResult checkResult);

  /** Notifies the Acrolinx Integration about the result of the initializing process. */
  void onInitFinished(Optional<SidebarError> initResult);

  /**
   * Opens the given document in editor and notifies the sidebar that the document has been opened.
   */
  boolean openDocumentInEditor(String documentIdentifier);

  /** Extracts all the references that should be listed for background check */
  List<BatchCheckRequestOptions> extractReferences();

  /**
   * Called together with getContentForDocument before running the background check on the given
   * document.
   */
  CheckOptions getCheckOptionsForDocument(String documentIdentifier);

  /** Gets the content for a requested background check */
  String getContentForDocument(String documentIdentifier);
}
