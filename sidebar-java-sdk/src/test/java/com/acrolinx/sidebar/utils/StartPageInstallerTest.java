
package com.acrolinx.sidebar.utils;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StartPageInstallerTest
{
    @Test
    public void getServerSelectorVersion() throws Exception
    {
        String serverSelectorVersion = StartPageInstaller.getStartPageVersion();
        assertTrue(serverSelectorVersion != null);
    }

}