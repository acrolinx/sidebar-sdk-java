/*
 * Copyright (c) 2016-2017 Acrolinx GmbH
 */

package com.acrolinx.sidebar.swt;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.acrolinx.sidebar.AcrolinxIntegration;
import com.acrolinx.sidebar.AcrolinxSidebar;
import com.acrolinx.sidebar.AcrolinxStorage;
import com.acrolinx.sidebar.adapter.NullEditorAdapter;
import com.acrolinx.sidebar.pojo.SidebarError;
import com.acrolinx.sidebar.pojo.document.*;
import com.acrolinx.sidebar.pojo.settings.AcrolinxURL;
import com.acrolinx.sidebar.pojo.settings.CheckOptions;
import com.acrolinx.sidebar.pojo.settings.DocumentSelection;
import com.acrolinx.sidebar.pojo.settings.SidebarConfiguration;
import com.acrolinx.sidebar.utils.*;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * SWT implementation of Acrolinx Sidebar.
 * @see AcrolinxSidebar
 */
@SuppressWarnings({"SameParameterValue", "WeakerAccess"})
public class AcrolinxSidebarSWT implements AcrolinxSidebar
{
    private final Logger logger = LoggerFactory.getLogger(AcrolinxSidebarSWT.class);

    private final Browser browser;
    private final AcrolinxIntegration client;
    private final AcrolinxStorage storage;
    private final AtomicReference<String> lastCheckedText = new AtomicReference<>("");
    private final AtomicReference<String> documentReference = new AtomicReference<>("");
    private final AtomicReference<String> currentDocumentReference = new AtomicReference<>("");
    private final AtomicReference<String> currentCheckId = new AtomicReference<>("");
    private final AtomicReference<Instant> checkStartTime = new AtomicReference<>();

    public AcrolinxSidebarSWT(Composite parent, AcrolinxIntegration client)
    {
        this(parent, client, null);
    }

    public AcrolinxSidebarSWT(Shell parent, AcrolinxIntegration client)
    {
        this(parent, client, null);
    }

    @SuppressWarnings("WeakerAccess")
    public AcrolinxSidebarSWT(Composite parent, AcrolinxIntegration client, AcrolinxStorage storage)
    {
        Preconditions.checkNotNull(parent, "Composite parent should not be null");
        Preconditions.checkNotNull(client, "AcrolinxIntegration client should not be null");
        Preconditions.checkNotNull(client.getEditorAdapter(),
                "EditorAdapter client.getEditorAdapter should return null");

        LogMessages.logJavaVersionAndUIFramework(logger, "Java SWT");

        SecurityUtils.setUpEnvironment();

        this.storage = storage;
        this.client = client;
        this.browser = new Browser(parent, SWT.NONE);
        initBrowser();
    }

    public AcrolinxSidebarSWT(Shell parent, AcrolinxIntegration client, AcrolinxStorage storage)
    {
        Preconditions.checkNotNull(parent, "Shell parent should not be null");
        Preconditions.checkNotNull(client, "AcrolinxIntegration client should not be null");
        Preconditions.checkNotNull(client.getEditorAdapter(),
                "EditorAdapter client.getEditorAdapter should not return null");

        this.storage = storage;
        this.client = client;
        this.browser = new Browser(parent, SWT.NONE);
        initBrowser();
    }

    private void initBrowser()
    {
        logger.debug("Initializing Browser...");
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        browser.setLayoutData(gridData);
        browser.setJavascriptEnabled(true);
        try {
            String sidebarUrl = client.getInitParameters().getSidebarUrl();
            logger.debug("Loading sidebar from " + sidebarUrl);
            browser.setUrl(sidebarUrl);
        } catch (Exception e) {
            logger.error("Error while loading sidebar!", e);
            browser.setText(SidebarUtils.sidebarErrorHTML);
        }
        browser.addProgressListener(new ProgressListener() {
            @Override
            public void completed(ProgressEvent event)
            {
                if (storage != null) {
                    initLocalStorage();
                }
                initSidebar();
            }

            @Override
            public void changed(ProgressEvent event)
            {
            }
        });
    }

    private void initLocalStorage()
    {
        new BrowserFunction(browser, "getItemP") {
            @Override
            public Object function(final Object[] arguments)
            {
                String key = arguments[0].toString();
                logger.debug("Requesting " + key + " from local storage.");
                String item = storage.getItem(key);
                logger.debug("Got item: " + item);
                return item;
            }
        };

        new BrowserFunction(browser, "removeItemP") {
            @Override
            public Object function(final Object[] arguments)
            {
                String key = arguments[0].toString();
                logger.debug("Removing " + key + " from local storage.");
                storage.removeItem(key);
                return null;
            }
        };

        new BrowserFunction(browser, "setItemP") {
            @Override
            public Object function(final Object[] arguments)
            {
                String key = arguments[0].toString();
                String data = arguments[1].toString();
                logger.debug("Setting " + key + " in local storage to " + data + ".");
                storage.setItem(key, data);
                return null;
            }
        };

        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("localStorageScript.js");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String script = sb.toString();
            reader.close();
            browser.evaluate(script);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void initSidebar()
    {
        new BrowserFunction(browser, "overwriteJSLoggingP") {
            @Override
            public Object function(final Object[] arguments)
            {
                logger.debug("JSLogging: " + arguments[0]);
                return null;
            }
        };

        new BrowserFunction(browser, "getInitParamsP") {
            @Override
            public Object function(final Object[] arguments)
            {
                return client.getInitParameters().toString();
            }
        };

        new BrowserFunction(browser, "getTextP") {
            @Override
            public Object function(final Object[] arguments)
            {
                LogMessages.logCheckRequested(logger);
                checkStartTime.set(Instant.now());
                final String requestText = client.getEditorAdapter().getContent();
                lastCheckedText.set(requestText);
                if (Strings.isNullOrEmpty(requestText)) {
                    return "<unsupported/>";
                }
                return requestText;
            }

        };

        new BrowserFunction(browser, "onInitFinishedNotificationP") {
            @Override
            public Object function(final Object[] arguments)
            {
                String result = arguments[0].toString();
                JsonParser parser = new JsonParser();
                JsonObject json = (JsonObject) parser.parse(result);
                JsonObject error = json.getAsJsonObject("error");
                if (error != null) {
                    SidebarError sidebarError = new Gson().fromJson(error, SidebarError.class);
                    client.onInitFinished(Optional.of(sidebarError));
                } else {
                    client.onInitFinished(Optional.empty());
                }
                return null;
            }
        };

        new BrowserFunction(browser, "canCheck") {
            @Override
            public Object function(final Object[] arguments)
            {
                boolean canCheck = client.getEditorAdapter() != null
                        && !(client.getEditorAdapter() instanceof NullEditorAdapter);
                if (!canCheck) {
                    logger.warn("Current File Editor not supported for checking or no file present.");
                }
                return canCheck;
            }
        };

        new BrowserFunction(browser, "getInputFormatP") {
            @Override
            public Object function(final Object[] arguments)
            {
                return client.getEditorAdapter().getInputFormat().toString();
            }
        };

        new BrowserFunction(browser, "onCheckResultP") {
            @Override
            public Object function(final Object[] arguments)
            {
                Instant checkEndedTime = Instant.now();
                LogMessages.logCheckFinishedWithDurationTime(logger,
                        Duration.between(checkStartTime.get(), checkEndedTime).toMillis());
                String checkResult = arguments[0].toString();
                try {
                    CheckResultFromJSON checkResultObj = new Gson().fromJson(checkResult, CheckResultFromJSON.class);
                    CheckResult result = checkResultObj.getAsCheckResult();
                    currentCheckId.set(result.getCheckedDocumentPart().getCheckId());
                    currentDocumentReference.set(documentReference.get());
                    client.onCheckResult(result);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
                return null;
            }
        };

        new BrowserFunction(browser, "getCurrentSelectionRangesP") {
            @Override
            public Object function(final Object[] arguments)
            {
                List<IntRange> currentSelection = client.getEditorAdapter().getCurrentSelection();
                if (currentSelection == null) {
                    return null;
                } else {
                    DocumentSelection selection = new DocumentSelection(currentSelection);
                    logger.debug("got selection ranges: " + selection.toString());
                    return selection.toString();
                }
            }
        };

        new BrowserFunction(browser, "selectRangesP") {
            @SuppressWarnings("unchecked")
            @Override
            public Object function(final Object[] arguments)
            {
                LogMessages.logSelectingRange(logger);
                List<AcrolinxMatchFromJSON> match = new Gson().fromJson((String) arguments[1],
                        new TypeToken<List<AcrolinxMatchFromJSON>>() {}.getType());
                List<AcrolinxMatch> result = match.stream().map(AcrolinxMatchFromJSON::getAsAcrolinxMatch).collect(
                        Collectors.toCollection(ArrayList::new));
                client.getEditorAdapter().selectRanges(currentCheckId.get(), Collections.unmodifiableList(result));
                return null;
            }

        };
        new BrowserFunction(browser, "replaceRangesP") {
            @SuppressWarnings("unchecked")
            @Override
            public Object function(final Object[] arguments)
            {
                LogMessages.logReplacingRange(logger);
                List<AcrolinxMatchFromJSON> match = new Gson().fromJson((String) arguments[1],
                        new TypeToken<List<AcrolinxMatchFromJSON>>() {}.getType());
                List<AcrolinxMatchWithReplacement> result = match.stream().map(
                        AcrolinxMatchFromJSON::getAsAcrolinxMatchWithReplacement).collect(
                                Collectors.toCollection(ArrayList::new));
                client.getEditorAdapter().replaceRanges(currentCheckId.get(), Collections.unmodifiableList(result));
                return null;
            }
        };
        new BrowserFunction(browser, "getDocUrlP") {
            @Override
            public Object function(final Object[] arguments)
            {
                String documentRef = client.getEditorAdapter().getDocumentReference();
                documentReference.set(documentRef);
                return documentRef;
            }

        };

        new BrowserFunction(browser, "notifyAboutSidebarConfigurationP") {
            @Override
            public Object function(final Object[] arguments)
            {
                return null;
            }
        };

        new BrowserFunction(browser, "downloadP") {
            @Override
            public Object function(final Object[] arguments)
            {
                return null;
            }
        };

        new BrowserFunction(browser, "openWindowP") {
            @Override
            public Object function(final Object[] arguments)
            {
                String result = arguments[0].toString();
                String url = AcrolinxSidebarSWT.getURlFromJS(result);
                if ("".equals(url)) {
                    logger.warn("Called to open URL but no URL to open is present.");
                    return null;
                }
                if (SidebarUtils.isValidURL(url)) {
                    Program.launch(url);
                } else
                    logger.warn("Attempt to open invalid URL: " + url);
                return null;
            }
        };

        new BrowserFunction(browser, "openLogFileP") {
            @Override
            public Object function(final Object[] arguments)
            {
                Program.launch(new File(LoggingUtils.getLogFileLocation()).getParent());
                return null;
            }
        };

        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("acrolinxPluginScript.js");
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            String script = sb.toString();
            reader.close();
            browser.evaluate(script);

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @SuppressWarnings("unused")
    public Browser getSidebarBrowser()
    {
        return this.browser;
    }

    @Override
    public void configure(SidebarConfiguration configuration)
    {
        browser.execute("window.acrolinxSidebar.configure(" + configuration.toString() + ");");
    }

    @Override
    public CompletableFuture<String> checkGlobal(String content, CheckOptions options)
    {
        CompletableFuture<String> future = new CompletableFuture<>();
        String result = (String) browser.evaluate(
                "JSON.stringify(window.acrolinxSidebar.checkGlobal(" + content + "," + options.toString() + "));");
        future.complete(result);
        return future;
    }

    @Override
    public void onGlobalCheckRejected()
    {
        LogMessages.logCheckRejected(logger);
        browser.execute("window.acrolinxSidebar.onGlobalCheckRejected();");
    }

    private static String buildStringOfCheckedDocumentRanges(java.util.List<CheckedDocumentPart> checkedDocumentParts)
    {
        return checkedDocumentParts.stream().map(CheckedDocumentPart::getAsJS).collect(Collectors.joining(", "));
    }

    @Override
    public void invalidateRanges(List<CheckedDocumentPart> invalidDocumentParts)
    {
        String js = buildStringOfCheckedDocumentRanges(invalidDocumentParts);
        browser.execute("window.acrolinxSidebar.invalidateRanges([" + js + "]);");
    }

    @Override
    public void invalidateRangesForMatches(List<? extends AbstractMatch> matches)
    {
        List<CheckedDocumentPart> invalidDocumentParts = matches.stream().map((match) -> new CheckedDocumentPart(
                currentCheckId.get(),
                new IntRange(match.getRange().getMinimumInteger(), match.getRange().getMaximumInteger()))).collect(
                        Collectors.toList());
        invalidateRanges(invalidDocumentParts);
    }

    @Override
    public void loadSidebarFromServerLocation(String serverAddress)
    {
        this.client.getInitParameters().setServerAddress(serverAddress);
        this.client.getInitParameters().setShowServerSelector(false);
        this.initBrowser();
    }

    @Override
    public void reload()
    {
        if (this.client.getInitParameters().getShowServerSelector()) {
            try {
                StartPageInstaller.exportStartPageResources();
            } catch (Exception e) {
                logger.error("Error while exporting start page resources!");
                browser.setText(SidebarUtils.sidebarErrorHTML);
            }
        }
        browser.refresh();
    }

    @Override
    public String getLastCheckedDocumentReference()
    {
        if (!"".equals(currentDocumentReference.get())) {
            return currentDocumentReference.get();
        }
        return null;
    }

    @Override
    public String getLastCheckedDocument()
    {
        if (!"".equals(lastCheckedText.get())) {
            return lastCheckedText.get();
        }
        return null;
    }

    @SuppressWarnings("WeakerAccess")
    protected static String getURlFromJS(String jsonStr)
    {
        Gson gson = new Gson();
        AcrolinxURL element = gson.fromJson(jsonStr, AcrolinxURL.class);
        if (element != null) {
            return element.getUrl();
        } else
            return null;
    }
}
