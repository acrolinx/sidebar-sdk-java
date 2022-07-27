package com.acrolinx.sidebar.swt;


import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

public class AcrolinxReuseSWT {

    final Browser browser;

    private final GridData gridData;


    public AcrolinxReuseSWT(Composite parent) {
        this.browser = new Browser(parent, SWT.DEFAULT);
        this.gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        browser.setLayoutData(this.gridData);
        showReuseWindow();
    }

    public void showReuseWindow() {
        browser.setJavascriptEnabled(true);
        browser.evaluate(""
                + "let elemDiv = document.createElement('div');"
                + "let elemDiv2 = document.createElement('div');"
                + "elemDiv.addEventListener(\"keydown\",function(event) {"
                + "console.log(event.keyCode);"
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
                + "elemDiv.innerText = 'This is an example suggested sentence.';"
                + "elemDiv2.innerText = 'This is another example suggested sentence.';"
                + "elemDiv.style.cssText='color:blue;text-decoration: underline;cursor: pointer;';"
                + "elemDiv2.style.cssText='color:blue;text-decoration: underline;cursor: pointer;';"
                + "elemDiv.tabIndex = 1;"
                + "elemDiv2.tabIndex = 2;"
                + "document.body.appendChild(elemDiv);"
                + "document.body.appendChild(elemDiv2);");

    }
}