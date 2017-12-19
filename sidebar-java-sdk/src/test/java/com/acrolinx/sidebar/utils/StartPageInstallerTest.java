
package com.acrolinx.sidebar.utils;

import static org.apache.commons.io.FileUtils.deleteDirectory;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
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
        String serverSelectorVersion = StartPageInstaller.getStartPageVersion();
        assertTrue(serverSelectorVersion != null);
    }

    @Test
    public void exportStartpageFromDifferentThreadsDoesNotCrash() throws Exception
    {
        String startPageURL = StartPageInstaller.getStartPageURL();
        String replace = startPageURL.replace("file:", "");
        Path path = Paths.get(replace);
        Path startpageDir = path.getParent();
        if (Files.exists(startpageDir)) {
            deleteDirectory(startpageDir.toFile());
        }
        Runnable runnable1 = () -> {
            try {
                String startPageURL1 = StartPageInstaller.getStartPageURL();
                Assert.assertTrue(startPageURL1.contains("index.html"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        };
        Runnable runnable2 = () -> {
            try {
                String startPageURL1 = StartPageInstaller.getStartPageURL();
                Assert.assertTrue(startPageURL1.contains("index.html"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        };
        Runnable runnable3 = () -> {
            try {
                String startPageURL1 = StartPageInstaller.getStartPageURL();
                Assert.assertTrue(startPageURL1.contains("index.html"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        };
        runnable1.run();
        runnable2.run();
        runnable3.run();
        Assert.assertTrue(Files.exists(path));
        if (Files.exists(startpageDir)) {
            deleteDirectory(startpageDir.toFile());
        }
    }
}