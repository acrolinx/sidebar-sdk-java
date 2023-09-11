/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.swt;

import com.acrolinx.sidebar.utils.IconUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ChildShell
{
    private final Shell child;

    public ChildShell(Shell parent, Display display)
    {
        child = new Shell(parent, SWT.DIALOG_TRIM);
        child.setLayout(new FillLayout());
        child.setText("Acrolinx Sidebar");
        child.setImage(new Image(display, IconUtils.getAcrolinxIcon_24_24_AsStream()));
    }

    public Shell getShell()
    {
        return child;
    }
}
