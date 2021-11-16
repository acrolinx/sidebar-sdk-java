/* Copyright (c) 2021-present Acrolinx GmbH */

package com.acrolinx.sidebar.swt;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.AcrolinxStorage;
import com.acrolinx.sidebar.utils.AcrolinxException;

// TODO: Hide default view on addition of first sidebar.
public class AcrolinxMultiViewSidebarSWT
{

    private AcrolinxStorage acrolinxStorage = null;
    private final Composite container;
    private final StackLayout stackLayout;

    private final Map<String, AcrolinxSidebarSWT> sidebarSWTMap = new HashMap<>();

    public AcrolinxMultiViewSidebarSWT(final Composite parent, final AcrolinxStorage storage)
    {
        this.container = new Composite(parent, SWT.NONE);
        this.container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        this.stackLayout = new StackLayout();
        this.container.setLayout(this.stackLayout);
        this.acrolinxStorage = storage;
    }

    public AcrolinxMultiViewSidebarSWT(final Shell parent, final AcrolinxStorage storage)
    {
        this.container = new Composite(parent, SWT.NONE);
        this.container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        this.stackLayout = new StackLayout();
        this.container.setLayout(this.stackLayout);
        this.acrolinxStorage = storage;
    }

    public void createDefaultSidebar()
    {
        // TODO: Create default view (Show Acrolinx Image or some text).
    }

    public void addSidebar(String documentId, AcrolinxIntegration client) throws AcrolinxException
    {
        final AcrolinxSidebarSWT existingSidebar = this.sidebarSWTMap.get(documentId);
        if (existingSidebar != null) {
            throw new AcrolinxException("Sidebar already exists for document id: " + documentId);
        }
        this.hideAllSidebars();
        final AcrolinxSidebarSWT acrolinxSidebarSWT;
        if (this.container instanceof Shell) {
            acrolinxSidebarSWT = new AcrolinxSidebarSWT((Shell) container, client, this.acrolinxStorage);
        } else {
            acrolinxSidebarSWT = new AcrolinxSidebarSWT(container, client, this.acrolinxStorage);
        }
        this.sidebarSWTMap.put(documentId, acrolinxSidebarSWT);
        acrolinxSidebarSWT.reload();
        this.stackLayout.topControl = acrolinxSidebarSWT.browser;
        container.layout(true, true);
        acrolinxSidebarSWT.setVisible(true);
    }

    public void switchSidebar(String documentId) throws AcrolinxException
    {
        this.hideAllSidebars();
        final AcrolinxSidebarSWT acrolinxSidebarSWT = this.sidebarSWTMap.get(documentId);
        if (acrolinxSidebarSWT == null) {
            throw new AcrolinxException("Existing sidebar not found for document Id.");
        }
        acrolinxSidebarSWT.setVisible(true);
        this.stackLayout.topControl = acrolinxSidebarSWT.browser;
        container.layout(true, true);
    }

    public void removeSidebar(String documentId) throws AcrolinxException
    {
        final AcrolinxSidebarSWT sidebarRemoved = this.sidebarSWTMap.remove(documentId);
        if (sidebarRemoved == null) {
            throw new AcrolinxException("Sidebar doesn't exist for the given document Id");
        }
        sidebarRemoved.setVisible(false);
        sidebarRemoved.browser.dispose();
        if (this.sidebarSWTMap.isEmpty()) {
            showDefaultSidebar();
        }
    }

    public AcrolinxSidebarSWT getActiveSidebar()
    {
        for (Map.Entry<String, AcrolinxSidebarSWT> entry : this.sidebarSWTMap.entrySet()) {
            if (entry.getValue().isVisible()) {
                return entry.getValue();
            }
        }
        return null;
    }

    private void showDefaultSidebar()
    {
        // TODO: Show default view
    }

    private void hideAllSidebars()
    {
        for (Map.Entry<String, AcrolinxSidebarSWT> entry : this.sidebarSWTMap.entrySet()) {
            if (entry.getValue().isVisible()) {
                entry.getValue().setVisible(false);
            }
        }
    }
}
