/* Copyright (c) 2016-present Acrolinx GmbH */

package com.acrolinx.sidebar;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.acrolinx.sidebar.pojo.SidebarError;
import com.acrolinx.sidebar.pojo.document.CheckResult;
import com.acrolinx.sidebar.pojo.settings.AcrolinxSidebarInitParameter;
import com.acrolinx.sidebar.pojo.settings.BatchCheckRequestOptions;
import com.acrolinx.sidebar.pojo.settings.CheckOptions;
import com.acrolinx.sidebar.live.LiveResponse;

/**
 * This interface needs be implemented to integrate Acrolinx with an editor or editing environment.
 * These methods are called by the Acrolinx Sidebar to interact with the editor.
 *
 * Remember to activate logging by using LoggingUtils.setupDefaultLogging() on application/plugin
 * start.
 *
 */
public interface AcrolinxIntegration
{

    /**
     * Adapter to extract the text to be checked. It needs to be an implementation of
     * InputAdapterInterface.
     *
     * @return Implementation of InputAdapterInterface
     * @see InputAdapterInterface
     */
    InputAdapterInterface getEditorAdapter();

    /**
     * Gets the parameters used to initialize the Acrolinx Sidebar.
     *
     * @return AcrolinxInitParameter
     * @see AcrolinxSidebarInitParameter
     */
    AcrolinxSidebarInitParameter getInitParameters();

    /**
     * Notifies the Acrolinx Integration about the checks result.
     *
     * @param checkResult
     */
    void onCheckResult(CheckResult checkResult);

    /**
     * Notifies the Acrolinx Integration about the result of the initializing process.
     *
     * @param initResult
     */
    void onInitFinished(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<SidebarError> initResult);

    /**
     * Opens the given document in editor and notifies the sidebar that the document has been
     * opened.
     *
     * @param documentIdentifier
     */
    boolean openDocumentInEditor(String documentIdentifier);

    /**
     * Extracts all the references that should be listed for background check
     *
     * @return List<BatchCheckRequestOptions>
     */
    List<BatchCheckRequestOptions> extractReferences();

    /**
     * Called together with getContentForDocument before running the background check on the given
     * document.
     *
     * @param documentIdentifier
     * @return CheckOptions
     */
    CheckOptions getCheckOptionsForDocument(String documentIdentifier);

    /**
     * Gets the content for a requested background check
     *
     * @param documentIdentifier
     * @return String
     */
    String getContentForDocument(String documentIdentifier);


    void onLiveSearchSuggestions(LiveResponse liveResponse);

    void onLiveSearchError(Exception e);

    void onLivePrefixSearchFailed();

    void openLivePanel();

    void onUILanguageChanged(Locale locale);

    void onTargetChanged(boolean supportsLive);
}
