/**
 * Copyright (c) 2020-present Acrolinx GmbH
 */

package com.acrolinx.sidebar;

import java.util.List;

import com.acrolinx.sidebar.pojo.settings.BatchCheckRequestOptions;
import com.acrolinx.sidebar.pojo.settings.CheckModeType;
import com.acrolinx.sidebar.pojo.settings.CheckOptions;

// TODO: document from which sidebar version the batch support is available
public interface AcrolinxIntegrationWithBatchSupport extends AcrolinxIntegration
{
    /**
     * Opens the given reference in editor and notifies the sidebar that the document has been
     * opened.
     *
     * @param reference
     */
    boolean openReferenceInEditor(String reference);

    /**
     * Opens the last checked map in editor on pressing the back to topics list button
     *
     */
    void openMapInEditor();

    /**
     * Extracts all the references that should be listed for background check
     *
     * @return List<BatchCheckRequestOptions>
     */
    List<BatchCheckRequestOptions> extractReferences();

    /**
     * Called together with getContentForReference before running the background check on the given
     * reference.
     *
     * @param reference
     * @return CheckOptions
     */
    CheckOptions getCheckOptionsForReference(String reference);

    /**
     * Gets the content for a requested background check
     *
     * @param reference
     * @return String
     */
    String getContentForReference(String reference);

    /**
     * Gets the check mode for the requested check, returns CheckModeType.INTERACTIVE for normal
     * checks and CheckModeType.BACKGROUNDCHECK to initialize a background check.
     *
     * @return CheckModeType
     */
    CheckModeType getCheckModeOnCheckRequested();
}
