/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;

import com.acrolinx.sidebar.pojo.settings.AcrolinxSidebarInitParameter;
import com.acrolinx.sidebar.pojo.settings.AcrolinxSidebarInitParameter.AcrolinxSidebarInitParameterBuilder;

public class StartPageInstallerTest
{
    @Test
    public void getServerSelectorVersion()
    {
        final String serverSelectorVersion = StartPageInstaller.getStartPageVersion();
        assertNotNull(serverSelectorVersion);
    }

    @Test
    public void prepareSidebarUrlWithStartPage() throws Exception
    {
        final AcrolinxSidebarInitParameterBuilder builder = new AcrolinxSidebarInitParameterBuilder("123",
                new ArrayList<>());

        final AcrolinxSidebarInitParameter params = builder.build();

        assertEquals(StartPageInstaller.prepareSidebarUrl(params), StartPageInstaller.getStartPageURL());
    }

    @Test
    public void prepareSidebarUrlWithSidebar()
    {
        final AcrolinxSidebarInitParameterBuilder builder = new AcrolinxSidebarInitParameterBuilder("123",
                new ArrayList<>());
        builder.withSidebarUrl("https://127.0.0.1");
        builder.withShowServerSelector(false);

        final AcrolinxSidebarInitParameter params = builder.build();

        assertEquals("https://127.0.0.1", StartPageInstaller.prepareSidebarUrl(params));
    }

    @Test
    public void prepareSidebarUrlWithStartPageIfSidebarUrlIsSetBut() throws Exception
    {
        final AcrolinxSidebarInitParameterBuilder builder = new AcrolinxSidebarInitParameterBuilder("123",
                new ArrayList<>());
        builder.withSidebarUrl("https://127.0.0.1");
        builder.withShowServerSelector(true);

        final AcrolinxSidebarInitParameter params = builder.build();

        assertEquals(StartPageInstaller.prepareSidebarUrl(params), StartPageInstaller.getStartPageURL());
    }

    @Test
    public void prepareSidebarUrlWithStartPageIfServerUrlIsSetButServerSelector() throws Exception
    {
        final AcrolinxSidebarInitParameterBuilder builder = new AcrolinxSidebarInitParameterBuilder("123",
                new ArrayList<>());
        builder.withServerAddress("https://127.0.0.1");
        builder.withShowServerSelector(true);

        final AcrolinxSidebarInitParameter params = builder.build();

        assertEquals(StartPageInstaller.prepareSidebarUrl(params), StartPageInstaller.getStartPageURL());
    }

    @Test
    public void prepareSidebarUrlWithStartPageIfServerUrlIsSet() throws Exception
    {
        final AcrolinxSidebarInitParameterBuilder builder = new AcrolinxSidebarInitParameterBuilder("123",
                new ArrayList<>());
        builder.withServerAddress("https://127.0.0.1");
        builder.withShowServerSelector(false);

        final AcrolinxSidebarInitParameter params = builder.build();

        assertEquals(StartPageInstaller.prepareSidebarUrl(params), StartPageInstaller.getStartPageURL());
    }
}