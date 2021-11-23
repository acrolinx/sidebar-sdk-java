/* Copyright (c) 2016-present Acrolinx GmbH */

package com.acrolinx.sidebar;

import java.util.List;

import com.acrolinx.sidebar.pojo.document.AbstractMatch;
import com.acrolinx.sidebar.pojo.document.CheckedDocumentPart;
import com.acrolinx.sidebar.pojo.settings.BatchCheckRequestOptions;
import com.acrolinx.sidebar.pojo.settings.CheckOptions;
import com.acrolinx.sidebar.pojo.settings.SidebarConfiguration;
import com.acrolinx.sidebar.pojo.settings.SidebarMessage;

/**
 * These commands are available to interact with the Acrolinx Sidebar.
 */
@SuppressWarnings("unused")
public interface AcrolinxSidebar
{
    /**
     * Pushed the configuration to the Acrolinx Sidebar.
     *
     * @param configuration
     * @see SidebarConfiguration
     */
    void configure(SidebarConfiguration configuration);

    /**
     * Triggers a check action in the Acrolinx Sidebar, same as pushing the checkButton.
     *
     */
    void checkGlobal();

    /**
     * Notifies the sidebar that the check was canceled.
     */
    void onGlobalCheckRejected();

    /**
     * Notifies the sidebar about invalid ranges of the current document. The sidebar will then
     * invalidate all cards containing issues within this text range.
     *
     * @param invalidCheckedDocumentRanges
     */
    void invalidateRanges(List<CheckedDocumentPart> invalidCheckedDocumentRanges);

    /**
     * Notifies the sidebar about invalid ranges of the current document. The sidebar will then
     * invalidate all cards containing issues within this text range for the current check.
     * 
     * @param matches
     */
    void invalidateRangesForMatches(List<? extends AbstractMatch> matches);

    /**
     * Load the sidebar from a different server address.
     *
     * @param serverAddress
     */
    void loadSidebarFromServerLocation(String serverAddress);

    /**
     * Reload the current sidebar.
     */
    void reload();

    /**
     * Gets the document reference of the lasted successfully checked document.
     * 
     * @return documentReference
     */
    String getLastCheckedDocumentReference();

    /**
     *
     * Gets the content of the lasted successfully checked document.
     * 
     * @return documentContent
     */
    String getLastCheckedDocument();

    /**
     * Shows a message in the sidebar.
     *
     * @param sidebarMessage
     */

    void showMessage(SidebarMessage sidebarMessage);

    /**
     *
     * @param batchCheckRequestOptions
     */
    void initBatchCheck(List<BatchCheckRequestOptions> batchCheckRequestOptions);

    /**
     *
     * @param documentIdentifier
     * @param documentContent
     * @param options
     */

    void checkDocumentInBackground(String documentIdentifier, String documentContent, CheckOptions options);

}
