/**
 *  Copyright (c) 2020-present Acrolinx GmbH
 */
package com.acrolinx.sidebar.swt;


import com.acrolinx.sidebar.AcrolinxReuseComponentInterface;
import com.acrolinx.sidebar.swing.PhraseSelectionHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class AcrolinxReuseSWT implements AcrolinxReuseComponentInterface {

    final Browser browser;

    private final GridData gridData;

    private final PhraseSelectionHandler phraseSelectionHandler;


    public AcrolinxReuseSWT(Composite parent, PhraseSelectionHandler phraseSelectionHandler) {
        this.browser = new Browser(parent, SWT.DEFAULT);
        this.phraseSelectionHandler = phraseSelectionHandler;
        this.gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        browser.setLayoutData(this.gridData);
        showReuseWindow();
    }

    public void showReuseWindow() {
        browser.setJavascriptEnabled(true);

        browser.addProgressListener(new ProgressListener() {
            @Override
            public void completed(final ProgressEvent event)
            {
                System.out.println("complete!");
                new BrowserFunction(browser, "handlePhraseSelection") {
                    @Override
                    public Object function(final Object[] arguments)
                    {
                        phraseSelectionHandler.onPhraseSelected((String) arguments[0]);
                        System.out.println("Browser message:" + arguments[0]);
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
        File file = new File( "C:\\Users\\jhorn\\code\\java\\intellij\\reuse-environment\\website\\index.html");
        browser.setUrl(file.toURI().toString());
    }

    @Override
    public void showPreferredPhrases(List<String> preferredPhrases) {
        String phrasesList = "[" + preferredPhrases.stream().map(p -> "'"+ p +"'" ).collect(Collectors.joining(",")) +"]";
        browser.execute("window.postMessage({'phrases':"+ phrasesList + "},'*')");
    }

    @Override
    public void showPreferredPhrasesAndOriginal(List<String> preferredPhrases, String original) {
        String phrasesList = "[" + preferredPhrases.stream().map(p -> "'"+ p +"'" ).collect(Collectors.joining(",")) +"]";
        browser.execute("window.postMessage({'phrases':"+ phrasesList + ",'original':'"+original+"'},'*')");

    }

    @Override
    public void setLoading(boolean loading) {
        browser.execute("window.postMessage({'loading':"+ (loading? "true":"false" )+"},'*')");
    }
}