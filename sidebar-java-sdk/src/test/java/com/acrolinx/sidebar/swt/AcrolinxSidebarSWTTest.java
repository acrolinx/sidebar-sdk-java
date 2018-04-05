
package com.acrolinx.sidebar.swt;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class AcrolinxSidebarSWTTest
{
    @SuppressWarnings("ConstantConditions")
    @Test
    public void getURlFromJS() throws Exception
    {
        String urlObj = "{\"url\":\"https://test-ssl.acrolinx.com/output/en/summerFlowers_dita_franziska_preiss_acrolinx_com_fc57c97ad061df73_615_report.html?apikey=eyJ0eXAiOiJKV1QiLCJhbGciOiJI\"}";
        String uRlFromJS = AcrolinxSidebarSWT.getURlFromJS(urlObj);
        assertTrue(uRlFromJS.equalsIgnoreCase(
                "https://test-ssl.acrolinx.com/output/en/summerFlowers_dita_franziska_preiss_acrolinx_com_fc57c97ad061df73_615_report.html?apikey=eyJ0eXAiOiJKV1QiLCJhbGciOiJI"));
    }

}