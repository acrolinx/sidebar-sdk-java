/**
 *  Copyright (c) 2022-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.swt;


import com.acrolinx.sidebar.AcrolinxLiveComponentInterface;
import com.acrolinx.sidebar.live.LivePanelState;
import com.acrolinx.sidebar.swing.PhraseSelectionHandler;
import com.acrolinx.sidebar.utils.LivePanelInstaller;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AcrolinxLiveSWT implements AcrolinxLiveComponentInterface {

    final Browser browser;

    private final GridData gridData;

    private final PhraseSelectionHandler phraseSelectionHandler;

    protected final Logger logger = LoggerFactory.getLogger(AcrolinxLiveSWT.class);


    public void setLiveState(LivePanelState livePanelState) {
        livePanelState = livePanelState.changeSearchString(livePanelState.getSearchString().replace("\u0000",""));
        livePanelState = livePanelState.changeCurrentString(livePanelState.getCurrentString().replace("\u0000",""));
        browser.execute("window.postMessage("+livePanelState.toJSON()+",'*')");
    }


    public AcrolinxLiveSWT(Composite parent, PhraseSelectionHandler phraseSelectionHandler) {
        this.browser = new Browser(parent, SWT.DEFAULT);
        this.phraseSelectionHandler = phraseSelectionHandler;
        this.gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        browser.setLayoutData(this.gridData);
        try {
            LivePanelInstaller.exportLivePanelResources();
        } catch (final Exception e) {
            logger.error("Error while exporting live panel resources: ", e.getMessage());
        }
        showLiveWindow();
    }

    public void showLiveWindow() {
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
                new BrowserFunction(browser, "closeLivePanelP") {
                    @Override
                    public Object function(final Object[] arguments)
                    {
                        closeLivePanel();
                        return null;
                    }
                };
                SWTUtils.loadScriptJS(browser,"liveAdapter.js");
            }

            @Override
            public void changed(final ProgressEvent event)
            {
                // we only need completed event to be handled
            }
        });
        browser.setUrl(LivePanelInstaller.getLivePanelURL());
    }

    public void closeLivePanel() {
        if(phraseSelectionHandler != null) {
            phraseSelectionHandler.closeLivePanel();
        }
    }

}