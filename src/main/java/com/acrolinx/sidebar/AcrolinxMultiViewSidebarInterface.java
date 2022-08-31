/**
 * Copyright (c) 2021-present Acrolinx GmbH
 */

package com.acrolinx.sidebar;

import com.acrolinx.sidebar.utils.AcrolinxException;

/**
 * This interface needs be implemented to support multiple sidebars. Each open document will have
 * its own corresponding sidebar. These methods should be called by the integration whenever a new
 * editor is opened, activated or closed.
 *
 */
public interface AcrolinxMultiViewSidebarInterface
{

    /**
     * Launches a new sidebar for the opened document with id documentId.
     * 
     * @param client
     * @param documentId
     * @throws AcrolinxException
     */
    void addSidebar(AcrolinxIntegration client, String documentId) throws AcrolinxException;

    /**
     * Switches the active sidebar to the one corresponding to the document with id documentId.
     * 
     * @param documentId
     * @throws AcrolinxException
     */
    void switchSidebar(String documentId) throws AcrolinxException;

    /**
     * Removes the sidebar for the document with id documentId.
     * 
     * @param documentId
     * @throws AcrolinxException
     */
    void removeSidebar(String documentId) throws AcrolinxException;

    AcrolinxSidebar getActiveSidebar();
}
