/**
 *  Copyright (c) 2020-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.swt;


import com.acrolinx.sidebar.AcrolinxReuseComponentInterface;
import com.acrolinx.sidebar.reuse.ReusePanelState;
import com.acrolinx.sidebar.swing.PhraseSelectionHandler;
import com.acrolinx.sidebar.utils.ReusePanelInstaller;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AcrolinxReuseSWT implements AcrolinxReuseComponentInterface {

    final Browser browser;

    private final GridData gridData;

    private final PhraseSelectionHandler phraseSelectionHandler;

    protected final Logger logger = LoggerFactory.getLogger(AcrolinxReuseSWT.class);


    public void setReuseState(ReusePanelState reusePanelState) {
        ReusePanelState reusePanelState1 = new ReusePanelState(reusePanelState.getMessage(), reusePanelState.getSuggestions(),reusePanelState.getSearchString().replace("\u0000",""),reusePanelState.getPotentialNextSearchString().replace("\u0000",""),reusePanelState.isLoading());
        browser.execute("window.postMessage("+reusePanelState1.toJSON()+",'*')");
    }


    public AcrolinxReuseSWT(Composite parent, PhraseSelectionHandler phraseSelectionHandler) {
        this.browser = new Browser(parent, SWT.DEFAULT);
        this.phraseSelectionHandler = phraseSelectionHandler;
        this.gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        browser.setLayoutData(this.gridData);
        try {
            ReusePanelInstaller.exportReusePanelResources();
        } catch (final Exception e) {
            logger.error("Error while exporting reuse panel resources: ", e.getMessage());
        }
        showReuseWindow();
    }

    public void showReuseWindow() {
        browser.setJavascriptEnabled(true);

        browser.addProgressListener(new ProgressListener() {
            @Override
            public void completed(final ProgressEvent event)
            {
                new BrowserFunction(browser, "handlePhraseSelectionP") {
                    @Override
                    public Object function(final Object[] arguments)
                    {
                        phraseSelectionHandler.onPhraseSelected((String) arguments[0]);
                        return null;
                    }
                };
                new BrowserFunction(browser, "logP") {
                    @Override
                    public Object function(final Object[] arguments)
                    {
                        logger.info((String) arguments[0]);
                        return null;
                    }
                };
                SWTUtils.loadScriptJS(browser,"reuseAdapter.js");
            }

            @Override
            public void changed(final ProgressEvent event)
            {
                // we only need completed event to be handled
            }
        });
        browser.setUrl(ReusePanelInstaller.getReusePanelURL());
    }

    @Override
    public void queryCurrentSentence() {
        if(phraseSelectionHandler != null) {
            phraseSelectionHandler.queryCurrentSentence();
        }
    }
}