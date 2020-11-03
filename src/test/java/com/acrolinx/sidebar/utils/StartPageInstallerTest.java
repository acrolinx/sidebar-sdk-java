/* Copyright (c) 2017-present Acrolinx GmbH */

package com.acrolinx.sidebar.utils;

import static org.apache.commons.io.FileUtils.deleteDirectory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.Assert;
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

        assertEquals(StartPageInstaller.prepareSidebarUrl(params), "https://127.0.0.1");
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

    @Test
    public void exportStartpageFromDifferentThreadsDoesNotCrash() throws Exception
    {
        final String startPageURL = StartPageInstaller.getStartPageURL();
        final Path path = Paths.get(new URI(startPageURL));
        final Path startPageDirectory = path.getParent();
        if (Files.exists(startPageDirectory)) {
            deleteDirectory(startPageDirectory.toFile());
        }
        final Runnable runnable1 = () -> {
            try {
                final String startPageURL1 = StartPageInstaller.getStartPageURL();
                Assert.assertTrue(startPageURL1.contains("index.html"));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        };
        final Runnable runnable2 = () -> {
            try {
                final String startPageURL1 = StartPageInstaller.getStartPageURL();
                Assert.assertTrue(startPageURL1.contains("index.html"));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        };
        final Runnable runnable3 = () -> {
            try {
                final String startPageURL1 = StartPageInstaller.getStartPageURL();
                Assert.assertTrue(startPageURL1.contains("index.html"));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        };
        runnable1.run();
        runnable2.run();
        runnable3.run();
        Assert.assertTrue(Files.exists(path));
        Assert.assertTrue(path.toFile().length() > 0);
        if (Files.exists(startPageDirectory)) {
            deleteDirectory(startPageDirectory.toFile());
        }
    }
}