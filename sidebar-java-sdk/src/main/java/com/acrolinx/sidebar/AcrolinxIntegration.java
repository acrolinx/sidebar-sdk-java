/*
 * Copyright (c) 2016-2017 Acrolinx GmbH
 */

package com.acrolinx.sidebar;

import java.util.Optional;

import com.acrolinx.sidebar.pojo.SidebarError;
import com.acrolinx.sidebar.pojo.document.CheckResult;
import com.acrolinx.sidebar.pojo.settings.AcrolinxSidebarInitParameter;

/**
 * This interface needs be implemented to integrate Acrolinx with an editor or editing environment.
 * These methods are called by the Acrolinx Sidebar to interact with the editor.
 *
 * Remember to activate logging by using LoggingUtils.setupDefaultLogging() on application/plugin start.
 *
 */
public interface AcrolinxIntegration
{

    /**
     * Adapter to extract the text to be checked. It needs to be an implementation of InputAdapterInterface.
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

}
