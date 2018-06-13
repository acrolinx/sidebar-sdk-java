
package com.acrolinx.sidebar.utils;

import static org.apache.commons.io.FileUtils.deleteDirectory;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Assert;
import org.junit.Test;

public class StartPageInstallerTest
{
    @Test
    public void getServerSelectorVersion() throws Exception
    {
        final String serverSelectorVersion = StartPageInstaller.getStartPageVersion();
        assertTrue(serverSelectorVersion != null);
    }

    @Test
    public void exportStartpageFromDifferentThreadsDoesNotCrash() throws Exception
    {
        final String startPageURL = StartPageInstaller.getStartPageURL();
        final Path path = Paths.get(new URI(startPageURL));
        final Path startpageDir = path.getParent();
        if (Files.exists(startpageDir)) {
            deleteDirectory(startpageDir.toFile());
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
        if (Files.exists(startpageDir)) {
            deleteDirectory(startpageDir.toFile());
        }
    }
}