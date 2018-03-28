/*
 * Copyright (c) 2016-2017 Acrolinx GmbH
 */

package com.acrolinx.sidebar.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.acrolinx.sidebar.utils.IconUtils;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ChildShell
{
    @SuppressWarnings("WeakerAccess")
    final Shell child;

    public ChildShell(Shell parent, Display display)
    {
        child = new Shell(parent, SWT.DIALOG_TRIM);
        child.setLayout(new FillLayout());
        child.setText("Acrolinx Sidebar");
        Image small = new Image(display, IconUtils.getAcrolinxIcon_24_24_AsStream());
        child.setImage(small);
    }

    public Shell getShell()
    {
        return child;
    }
}
