/* Copyright (c) 2018-present Acrolinx GmbH */
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
public interface AcrolinxSidebar
{
    /**
     * Pushed the configuration to the Acrolinx Sidebar.
     *
     * @see SidebarConfiguration
     */
    void configure(SidebarConfiguration sidebarConfiguration);

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
     */
    void invalidateRanges(List<CheckedDocumentPart> invalidCheckedDocumentRanges);

    /**
     * Notifies the sidebar about invalid ranges of the current document. The sidebar will then
     * invalidate all cards containing issues within this text range for the current check.
     */
    void invalidateRangesForMatches(List<? extends AbstractMatch> matches);

    /**
     * Load the sidebar from a different server address.
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
     */
    void showMessage(SidebarMessage sidebarMessage);

    void initBatchCheck(List<BatchCheckRequestOptions> batchCheckRequestOptions);

    void checkDocumentInBatch(String documentIdentifier, String documentContent, CheckOptions checkOptions);
}
