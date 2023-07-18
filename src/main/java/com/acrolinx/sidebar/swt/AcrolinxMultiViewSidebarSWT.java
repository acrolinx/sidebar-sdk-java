/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.swt;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Composite;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.AcrolinxStorage;
import com.acrolinx.sidebar.localization.LocalizedStrings;
import com.acrolinx.sidebar.localization.Localizer;
import com.acrolinx.sidebar.utils.AcrolinxException;

// TODO: Hide default view on addition of first sidebar.
public class AcrolinxMultiViewSidebarSWT
{
    private AcrolinxStorage acrolinxStorage;
    private final Composite container;
    private final StackLayout stackLayout;
    private CLabel noFileOpenedLabel;
    private final Map<String, AcrolinxSidebarSWT> sidebarSWTMap = new HashMap<>();

    /**
     * @param parent SWT Composite to which sidebar would be child of.
     * @param acrolinxStorage Acrolinx storage to act as external storage to sidebar local storage
     */
    public AcrolinxMultiViewSidebarSWT(final Composite parent, final AcrolinxStorage acrolinxStorage)
    {
        this.container = new Composite(parent, SWT.NONE);
        this.container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        this.stackLayout = new StackLayout();
        this.container.setLayout(this.stackLayout);
        this.acrolinxStorage = acrolinxStorage;
        createDefaultSidebar();
        showDefaultSidebar();
    }

    /**
     * @param parent SWT Shell to which sidebar would be child of.
     * @param acrolinxStorage Acrolinx storage to act as external storage to sidebar local storage
     */
    public AcrolinxMultiViewSidebarSWT(final Shell parent, final AcrolinxStorage acrolinxStorage)
    {
        this.container = new Composite(parent, SWT.NONE);
        this.container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        this.stackLayout = new StackLayout();
        this.container.setLayout(this.stackLayout);
        this.acrolinxStorage = acrolinxStorage;
        createDefaultSidebar();
        showDefaultSidebar();
    }

    /**
     * @param acrolinxIntegration New integration dedicated per sidebar instance
     * @param documentId Unique document Id for the sidebar instance eg: file path
     * @throws AcrolinxException Throws exception is sidebar already exists for the document
     */
    public void addSidebar(String documentId, AcrolinxIntegration acrolinxIntegration) throws AcrolinxException
    {
        final AcrolinxSidebarSWT existingSidebar = this.sidebarSWTMap.get(documentId);
        if (existingSidebar != null) {
            throw new AcrolinxException("Sidebar already exists for document id: " + documentId);
        }
        this.hideAllSidebars();
        final AcrolinxSidebarSWT acrolinxSidebarSWT;
        if (this.container instanceof Shell) {
            acrolinxSidebarSWT = new AcrolinxSidebarSWT((Shell) container, acrolinxIntegration, this.acrolinxStorage);
        } else {
            acrolinxSidebarSWT = new AcrolinxSidebarSWT(container, acrolinxIntegration, this.acrolinxStorage);
        }
        this.sidebarSWTMap.put(documentId, acrolinxSidebarSWT);
        acrolinxSidebarSWT.reload();
        this.stackLayout.topControl = acrolinxSidebarSWT.browser;
        container.layout(true, true);
        acrolinxSidebarSWT.setVisible(true);
    }

    /**
     * @param documentId Document Id of the sidebar instance to switch active sidebar to.
     * @throws AcrolinxException Throws if sidebar not found for provided document id.
     */
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

    /**
     * @param documentId Document Id for the sidebar instance to be removed
     * @throws AcrolinxException Throws if sidebar not found for the provided document id.
     */
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

    /**
     * @return Get Active sidebar from the list. Returns sidebar with visiblity set to true. Null if not
     *         found.
     */
    public AcrolinxSidebarSWT getActiveSidebar()
    {
        for (Map.Entry<String, AcrolinxSidebarSWT> entry : this.sidebarSWTMap.entrySet()) {
            if (entry.getValue().isVisible()) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * Creates default empty view with CLabel informing the user about having to open a file before
     * being able to interact with acrolinx
     */
    private void createDefaultSidebar()
    {
        noFileOpenedLabel = new CLabel(this.container, SWT.WRAP);
        noFileOpenedLabel.setText(Localizer.getInstance().getText(LocalizedStrings.NO_CHECK_CONTENT_AVAILABLE_MESSAGE));
        noFileOpenedLabel.setMargins(5, 5, 0, 5);
    }

    /**
     * Show the default sidebar view.
     */
    private void showDefaultSidebar()
    {
        this.stackLayout.topControl = noFileOpenedLabel;
    }

    /**
     * Set visibility of all the sidebars in that map to false
     */
    private void hideAllSidebars()
    {
        for (Map.Entry<String, AcrolinxSidebarSWT> entry : this.sidebarSWTMap.entrySet()) {
            if (entry.getValue().isVisible()) {
                entry.getValue().setVisible(false);
            }
        }
    }
}
