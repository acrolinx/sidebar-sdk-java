/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.acrolinx.sidebar.pojo.settings.AcrolinxSidebarInitParameter;
import com.acrolinx.sidebar.pojo.settings.AcrolinxSidebarInitParameter.AcrolinxSidebarInitParameterBuilder;

class StartPageInstallerTest
{
    @Test
    void getServerSelectorVersion()
    {
        final String serverSelectorVersion = StartPageInstaller.getStartPageVersion();
        assertNotNull(serverSelectorVersion);
    }

    @Test
    void prepareSidebarUrlWithStartPage() throws Exception
    {
        final AcrolinxSidebarInitParameterBuilder builder = new AcrolinxSidebarInitParameterBuilder("123",
                new ArrayList<>());

        final AcrolinxSidebarInitParameter params = builder.build();

        assertEquals(StartPageInstaller.prepareSidebarUrl(params), StartPageInstaller.getStartPageURL());
    }

    @Test
    void prepareSidebarUrlWithSidebar()
    {
        final AcrolinxSidebarInitParameterBuilder builder = new AcrolinxSidebarInitParameterBuilder("123",
                new ArrayList<>());
        builder.withSidebarUrl("https://127.0.0.1");
        builder.withShowServerSelector(false);

        final AcrolinxSidebarInitParameter params = builder.build();

        assertEquals("https://127.0.0.1", StartPageInstaller.prepareSidebarUrl(params));
    }

    @Test
    void prepareSidebarUrlWithStartPageIfSidebarUrlIsSetBut() throws Exception
    {
        final AcrolinxSidebarInitParameterBuilder builder = new AcrolinxSidebarInitParameterBuilder("123",
                new ArrayList<>());
        builder.withSidebarUrl("https://127.0.0.1");
        builder.withShowServerSelector(true);

        final AcrolinxSidebarInitParameter params = builder.build();

        assertEquals(StartPageInstaller.prepareSidebarUrl(params), StartPageInstaller.getStartPageURL());
    }

    @Test
    void prepareSidebarUrlWithStartPageIfServerUrlIsSetButServerSelector() throws Exception
    {
        final AcrolinxSidebarInitParameterBuilder builder = new AcrolinxSidebarInitParameterBuilder("123",
                new ArrayList<>());
        builder.withServerAddress("https://127.0.0.1");
        builder.withShowServerSelector(true);

        final AcrolinxSidebarInitParameter params = builder.build();

        assertEquals(StartPageInstaller.prepareSidebarUrl(params), StartPageInstaller.getStartPageURL());
    }

    @Test
    void prepareSidebarUrlWithStartPageIfServerUrlIsSet() throws Exception
    {
        final AcrolinxSidebarInitParameterBuilder builder = new AcrolinxSidebarInitParameterBuilder("123",
                new ArrayList<>());
        builder.withServerAddress("https://127.0.0.1");
        builder.withShowServerSelector(false);

        final AcrolinxSidebarInitParameter params = builder.build();

        assertEquals(StartPageInstaller.prepareSidebarUrl(params), StartPageInstaller.getStartPageURL());
    }
}
