/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.settings;

public class AcrolinxPluginConfiguration
{
    private final boolean base64EncodedGzippedDocumentContent;

    public AcrolinxPluginConfiguration(boolean base64EncodedGzippedDocumentContent)
    {
        this.base64EncodedGzippedDocumentContent = base64EncodedGzippedDocumentContent;
    }

    /**
     * @return If this returns true, the Acrolinx Server is set up to check zipped documents.
     */
    public boolean getBase64EncodedGzippedDocumentContent()
    {
        return base64EncodedGzippedDocumentContent;
    }
}
