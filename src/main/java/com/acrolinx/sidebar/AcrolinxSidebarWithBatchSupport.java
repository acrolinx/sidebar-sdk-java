/**
 * Copyright (c) 2020-present Acrolinx GmbH
 */

package com.acrolinx.sidebar;

import java.util.List;

import com.acrolinx.sidebar.pojo.settings.BatchCheckRequestOptions;
import com.acrolinx.sidebar.pojo.settings.CheckOptions;

// TODO: Document from which sidebar version this can be used
public interface AcrolinxSidebarWithBatchSupport extends AcrolinxSidebar
{

    // TODO add sidebar dita functions

    /**
     *
     * @param batchCheckRequestOptions
     */
    void initBatchCheck(List<BatchCheckRequestOptions> batchCheckRequestOptions);

    /**
     *
     * @param reference
     * @param documentContent
     * @param options
     */

    void checkReferenceInBackground(String reference, String documentContent, CheckOptions options);

    /**
     *
     * @param reference
     */
    void onReferenceLoadedInEditor(String reference);
}
