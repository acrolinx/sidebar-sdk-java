/**
 * Copyright (c) 2020-present Acrolinx GmbH
 */

package com.acrolinx.sidebar.swt;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.acrolinx.sidebar.AcrolinxIntegrationWithBatchSupport;
import com.acrolinx.sidebar.AcrolinxSidebarWithBatchSupport;
import com.acrolinx.sidebar.AcrolinxStorage;
import com.acrolinx.sidebar.pojo.settings.BatchCheckRequestOptions;
import com.acrolinx.sidebar.pojo.settings.CheckModeType;
import com.acrolinx.sidebar.pojo.settings.CheckOptions;

public class AcrolinxSidebarSWTWithBatchSupport extends AcrolinxSidebarSWT implements AcrolinxSidebarWithBatchSupport
{

    public AcrolinxSidebarSWTWithBatchSupport(Composite parent, AcrolinxIntegrationWithBatchSupport client)
    {
        super(parent, client);
    }

    public AcrolinxSidebarSWTWithBatchSupport(Shell parent, AcrolinxIntegrationWithBatchSupport client)
    {
        super(parent, client);
    }

    public AcrolinxSidebarSWTWithBatchSupport(Composite parent, AcrolinxIntegrationWithBatchSupport client,
            AcrolinxStorage storage)
    {
        super(parent, client, storage);
    }

    public AcrolinxSidebarSWTWithBatchSupport(Shell parent, AcrolinxIntegrationWithBatchSupport client,
            AcrolinxStorage storage)
    {
        super(parent, client, storage);
    }

    @Override
    protected void initSidebar()
    {
        new BrowserFunction(browser, "overwriteJSLoggingInfoP") {
            @Override
            public Object function(final Object[] arguments)
            {
                logger.info("Java Script: " + arguments[0]);
                return null;
            }
        };

        new BrowserFunction(browser, "overwriteJSLoggingErrorP") {
            @Override
            public Object function(final Object[] arguments)
            {
                logger.debug("Java Script: " + arguments[0]);
                return null;
            }
        };

        new BrowserFunction(browser, "getInitParamsP") {
            @Override
            public Object function(final Object[] arguments)
            {
                return client.getInitParameters().toString();
            }
        };

        new BrowserFunction(browser, "getTextP") {
            @Override
            public Object function(final Object[] arguments)
            {
                return getTextObject();
            }

        };

        new BrowserFunction(browser, "onInitFinishedNotificationP") {
            @Override
            public Object function(final Object[] arguments)
            {
                return getOnInitFinishedNotificationObject(arguments[0]);
            }
        };

        new BrowserFunction(browser, "canCheck") {
            @Override
            public Object function(final Object[] arguments)
            {
                return getCanCheckObject();
            }
        };

        new BrowserFunction(browser, "canBatchCheck") {
            @Override
            public Object function(final Object[] arguments)
            {
                return getCanBatchCheck();
            }
        };

        new BrowserFunction(browser, "runBatchCheck") {
            @Override
            public Object function(final Object[] arguments)
            {
                return runBatchCheck();
            }
        };

        new BrowserFunction(browser, "getInputFormatP") {
            @Override
            public Object function(final Object[] arguments)
            {
                return client.getEditorAdapter().getInputFormat().toString();
            }
        };

        new BrowserFunction(browser, "getExternalContentP") {
            @Override
            public Object function(final Object[] arguments)
            {
                return getExternalContentObject();
            }
        };

        new BrowserFunction(browser, "onCheckResultP") {
            @Override
            public Object function(final Object[] arguments)
            {
                return getOnCheckResultObject(arguments[0]);
            }
        };

        new BrowserFunction(browser, "getCurrentSelectionRangesP") {
            @Override
            public Object function(final Object[] arguments)
            {
                return getCurrentSelectionRangesObject();
            }
        };

        new BrowserFunction(browser, "selectRangesP") {
            @Override
            public Object function(final Object[] arguments)
            {
                return getSelectRangesObject(arguments[1]);
            }

        };
        new BrowserFunction(browser, "replaceRangesP") {
            @Override
            public Object function(final Object[] arguments)
            {
                return getReplaceRangesObject(arguments[1]);
            }
        };
        // TODO arguments[0] vs [1] ?
        new BrowserFunction(browser, "requestBackgroundCheckForRefP") {
            @Override
            public Object function(final Object[] arguments)
            {
                return requestBackgroundCheckForRef(arguments[1]);
            }
        };
        // TODO arguments[0] vs [1] ?
        new BrowserFunction(browser, "openReferenceInEditorP") {
            @Override
            public Object function(final Object[] arguments)
            {
                return openReferenceInEditor(arguments[1]);
            }
        };
        new BrowserFunction(browser, "getDocUrlP") {
            @Override
            public Object function(final Object[] arguments)
            {
                final String documentRef = client.getEditorAdapter().getDocumentReference();
                currentDocumentReference.set(documentRef);
                return documentRef;
            }

        };

        new BrowserFunction(browser, "notifyAboutSidebarConfigurationP") {
            @Override
            public Object function(final Object[] arguments)
            {
                return null;
            }
        };

        new BrowserFunction(browser, "downloadP") {
            @Override
            public Object function(final Object[] arguments)
            {
                return null;
            }
        };

        new BrowserFunction(browser, "openWindowP") {
            @Override
            public Object function(final Object[] arguments)
            {
                return getOpenWindowObject(arguments[0]);
            }
        };

        new BrowserFunction(browser, "openLogFileP") {
            @Override
            public Object function(final Object[] arguments)
            {
                return getOpenLogFileObject();
            }
        };

        loadScriptJS("acrolinxPluginWithBatchSelectorScript.js");
    }

    private Object requestBackgroundCheckForRef(Object argument)
    {
        // TODO: wait for the content ??
        String reference = argument.toString();
        String contentForReference = ((AcrolinxIntegrationWithBatchSupport) client).getContentForReference(reference);
        CheckOptions referenceCheckOptions = ((AcrolinxIntegrationWithBatchSupport) client).getCheckOptionsForReference(
                reference);
        this.checkReferenceInBackground(reference, contentForReference, referenceCheckOptions);
        return null;
    }

    private Object runBatchCheck()
    {
        List<BatchCheckRequestOptions> references = ((AcrolinxIntegrationWithBatchSupport) client).extractReferences();
        this.initBatchCheck(references);
        return null;
    }

    private Object openReferenceInEditor(Object argument)
    {
        // TODO: wait for the editor ??
        ((AcrolinxIntegrationWithBatchSupport) client).openReferenceInEditor(argument.toString());
        this.onReferenceLoadedInEditor(argument.toString());
        return null;
    }

    private boolean getCanBatchCheck()
    {
        boolean batchCheckSupported = client.getInitParameters().getSupported().isBatchChecking();
        CheckModeType checkModeRequested = ((AcrolinxIntegrationWithBatchSupport) client).getCheckModeOnCheckRequested();
        if (!batchCheckSupported || (batchCheckSupported && CheckModeType.INTERACTIVE.equals(checkModeRequested))) {
            return false;
        }
        return true;
    }

    @Override
    public void initBatchCheck(List<BatchCheckRequestOptions> batchCheckRequestOptions)
    {
        String jsArgs = buildStringOfBatchCheckRequestOptions(batchCheckRequestOptions);
        browser.execute("window.acrolinxSidebar.initBatchCheck([" + jsArgs + "]);");
    }

    @Override
    public void checkReferenceInBackground(String reference, String documentContent, CheckOptions options)
    {
        // TODO check passing multiple arguments!!
        browser.execute("window.acrolinxSidebar.checkReferenceInBackground(" + reference + ", " + documentContent + ", "
                + options.toString() + ");");
    }

    @Override
    public void onReferenceLoadedInEditor(String reference)
    {
        browser.execute("window.acrolinxSidebar.onReferenceLoadedInEditor(" + reference + ");");
    }

    // public for testing
    public static String buildStringOfBatchCheckRequestOptions(
            final java.util.List<BatchCheckRequestOptions> batchCheckRequestOptions)
    {
        // TODO verify if that returns
        return batchCheckRequestOptions.stream().map(BatchCheckRequestOptions::toString).collect(
                Collectors.joining(", "));
    }

}
