package com.acrolinx.sidebar.swt;


import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import java.util.List;
import java.util.stream.Collectors;

public class AcrolinxReuseSWT {

    final Browser browser;

    private final GridData gridData;

    public interface EventHandler {
        public void handleReuseEvent();
    }

    private final EventHandler eventHandler;

    public AcrolinxReuseSWT(Composite parent, EventHandler eventHandler) {
        this.browser = new Browser(parent, SWT.DEFAULT);
        this.gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        this.eventHandler = eventHandler;
        browser.setLayoutData(this.gridData);
        showReuseWindow();
    }

    public void showPrefferedPhrases(List<String> phrases) {
        String finalString = phrases.stream().map( p ->"<p>" +p+"</p>").collect(Collectors.joining());
        browser.execute("document.fillDiv3('"+finalString+"');");
    }

    public void buildReuseWindow() {
        browser.evaluate(""
                + "let elemDiv = document.createElement('div');"
                + "let elemDiv2 = document.createElement('div');"
                + "elemDiv.addEventListener(\"keydown\",function(event) {"
                + "console.log(event.keyCode);"
                + "window.logMessage(\"Key has been pressed\");"
                + "if (event.keyCode === 13) {"
                + "elemDiv.style.backgroundColor =  (elemDiv.style.backgroundColor === 'red' ? 'white':'red');"
                + "}"
                + "if (event.key === 'ArrowDown') {"
                + "event.currentTarget.nextElementSibling && event.currentTarget.nextElementSibling.focus();"
                + "}"
                + "if (event.key === 'ArrowUp') {"
                + "event.currentTarget.previousElementSibling && event.currentTarget.previousElementSibling.focus();"
                + "}"
                + "});"
                + "elemDiv2.addEventListener(\"keydown\",function(event) {"
                + "logMessage(\"Key has been pressed\");"
                + "if (event.keyCode === 13) {"
                + "elemDiv2.style.backgroundColor =  (elemDiv2.style.backgroundColor === 'red' ? 'white':'red');"
                + "}"
                + "if (event.key === 'ArrowDown') {"
                + "event.currentTarget.nextElementSibling && event.currentTarget.nextElementSibling.focus();"
                + "}"
                + "if (event.key === 'ArrowUp') {"
                + "event.currentTarget.previousElementSibling && event.currentTarget.previousElementSibling.focus();"
                + "}"
                + "});"
                + "elemDiv.innerText = 'This is an example suggested sentence. Bro';"
                + "elemDiv2.innerText = 'This is another example suggested sentence.';"
                + "elemDiv.style.cssText='color:blue;text-decoration: underline;cursor: pointer;';"
                + "elemDiv2.style.cssText='color:blue;text-decoration: underline;cursor: pointer;';"
                + "elemDiv.tabIndex = 1;"
                + "elemDiv2.tabIndex = 2;"
                + "document.body.appendChild(elemDiv);"
                + "let elemDiv3 = document.createElement('div');"
                + "document.body.appendChild(elemDiv2);"
                + "document.fillDiv3 = function(innerText) {"
                + "elemDiv3.innerHTML = innerText;"
                + "};"
                + "document.body.appendChild(elemDiv3);");
    }

    public void showReuseWindow() {
        browser.setJavascriptEnabled(true);

        new BrowserFunction(browser, "logMessage") {
            @Override
            public Object function(final Object[] arguments)
            {
                eventHandler.handleReuseEvent();
                System.out.println("Browser message:" + arguments[0]);
                return null;
            }
        };
        buildReuseWindow();
    }
}