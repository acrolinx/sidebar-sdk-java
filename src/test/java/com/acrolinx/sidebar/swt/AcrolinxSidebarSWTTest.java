/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.swt;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AcrolinxSidebarSWTTest
{
    @Test
    public void getURlFromJS()
    {
        String urlObj = "{\"url\":\"https://test-ssl.acrolinx.com/output/en/summerFlowers_dita_franziska_preiss_acrolinx_com_fc57c97ad061df73_615_report.html?apikey=eyJ0eXAiOiJKV1QiLCJhbGciOiJI\"}";
        String urlFromJS = AcrolinxSidebarSWT.getURlFromJS(urlObj);
        assertEquals(
                "https://test-ssl.acrolinx.com/output/en/summerFlowers_dita_franziska_preiss_acrolinx_com_fc57c97ad061df73_615_report.html?apikey=eyJ0eXAiOiJKV1QiLCJhbGciOiJI",
                urlFromJS);
    }
}