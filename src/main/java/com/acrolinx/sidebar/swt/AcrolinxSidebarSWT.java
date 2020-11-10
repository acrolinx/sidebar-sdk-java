/* Copyright (c) 2016-present Acrolinx GmbH */

package com.acrolinx.sidebar.swt;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
import com.acrolinx.sidebar.pojo.document.AbstractMatch;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatch;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatchFromJSON;
import com.acrolinx.sidebar.pojo.document.AcrolinxMatchWithReplacement;
import com.acrolinx.sidebar.pojo.document.CheckResult;
import com.acrolinx.sidebar.pojo.document.CheckResultFromJSON;
import com.acrolinx.sidebar.pojo.document.CheckedDocumentPart;
import com.acrolinx.sidebar.pojo.document.IntRange;
import com.acrolinx.sidebar.pojo.document.externalContent.ExternalContent;
import com.acrolinx.sidebar.pojo.settings.AcrolinxURL;
import com.acrolinx.sidebar.pojo.settings.DocumentSelection;
import com.acrolinx.sidebar.pojo.settings.SidebarConfiguration;
import com.acrolinx.sidebar.utils.LogMessages;
import com.acrolinx.sidebar.utils.LoggingUtils;
import com.acrolinx.sidebar.utils.SecurityUtils;
import com.acrolinx.sidebar.utils.SidebarUtils;
import com.acrolinx.sidebar.utils.StartPageInstaller;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

/**
 * SWT implementation of Acrolinx Sidebar.
 *
 * @see AcrolinxSidebar
 */
@SuppressWarnings({"unused", "SameParameterValue", "WeakerAccess"}) public class AcrolinxSidebarSWT
        implements AcrolinxSidebar
{
    private final Logger logger = LoggerFactory.getLogger(AcrolinxSidebarSWT.class);

    private final Browser browser;
    private final AcrolinxIntegration client;
    private final AcrolinxStorage storage;
    private final AtomicReference<String> currentlyCheckedText = new AtomicReference<>("");
    private final AtomicReference<String> lastCheckedText = new AtomicReference<>("");
    private final AtomicReference<String> lastCheckedDocumentReference = new AtomicReference<>("");
    private final AtomicReference<String> currentDocumentReference = new AtomicReference<>("");
    private final AtomicReference<String> currentCheckId = new AtomicReference<>("");
    private final AtomicReference<Instant> checkStartTime = new AtomicReference<>();

    public AcrolinxSidebarSWT(final Composite parent, final AcrolinxIntegration client)
    {
        this(parent, client, null);
    }

    public AcrolinxSidebarSWT(final Shell parent, final AcrolinxIntegration client)
    {
        this(parent, client, null);
    }

    @SuppressWarnings("WeakerAccess")
    public AcrolinxSidebarSWT(final Composite parent, final AcrolinxIntegration client, final AcrolinxStorage storage)
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

    public AcrolinxSidebarSWT(final Shell parent, final AcrolinxIntegration client, final AcrolinxStorage storage)
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
        final GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        browser.setLayoutData(gridData);
        browser.setJavascriptEnabled(true);
        try {
            final String sidebarUrl = StartPageInstaller.prepareSidebarUrl(client.getInitParameters());
            logger.debug("Loading sidebar from " + sidebarUrl);
            browser.setUrl(sidebarUrl);
        } catch (final Exception e) {
            logger.error("Error while loading sidebar!", e);
            browser.setText(SidebarUtils.sidebarErrorHTML);
        }
        browser.addProgressListener(new ProgressListener()
        {
            @Override
            public void completed(final ProgressEvent event)
            {
                if (storage != null) {
                    initLocalStorage();
                }
                initSidebar();
            }

            @Override
            public void changed(final ProgressEvent event)
            {
                // we only need completed event to be handled
            }
        });
    }

    private void initLocalStorage()
    {
        new BrowserFunction(browser, "getItemP")
        {
            @Override
            public Object function(final Object[] arguments)
            {
                final String key = arguments[0].toString();
                logger.debug("Requesting " + key + " from local storage.");
                final String item = storage.getItem(key);
                logger.debug("Got item: " + item);
                return item;
            }
        };

        new BrowserFunction(browser, "removeItemP")
        {
            @Override
            public Object function(final Object[] arguments)
            {
                final String key = arguments[0].toString();
                logger.debug("Removing " + key + " from local storage.");
                storage.removeItem(key);
                return null;
            }
        };

        new BrowserFunction(browser, "setItemP")
        {
            @Override
            public Object function(final Object[] arguments)
            {
                final String key = arguments[0].toString();
                final String data = arguments[1].toString();
                logger.debug("Setting " + key + " in local storage to " + data + ".");
                storage.setItem(key, data);
                return null;
            }
        };

        try {
            final ClassLoader classLoader = this.getClass().getClassLoader();
            final InputStream inputStream = classLoader.getResourceAsStream("localStorageScript.js");
            final BufferedReader reader;
            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line;
                final StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                final String script = sb.toString();
                reader.close();
                browser.evaluate(script);
            }
        } catch (final Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void initSidebar()
    {
        new BrowserFunction(browser, "overwriteJSLoggingInfoP")
        {
            @Override
            public Object function(final Object[] arguments)
            {
                logger.info("Java Script: " + arguments[0]);
                return null;
            }
        };

        new BrowserFunction(browser, "overwriteJSLoggingErrorP")
        {
            @Override
            public Object function(final Object[] arguments)
            {
                logger.debug("Java Script: " + arguments[0]);
                return null;
            }
        };

        new BrowserFunction(browser, "getInitParamsP")
        {
            @Override
            public Object function(final Object[] arguments)
            {
                return client.getInitParameters().toString();
            }
        };

        new BrowserFunction(browser, "getTextP")
        {
            @Override
            public Object function(final Object[] arguments)
            {
                LogMessages.logCheckRequested(logger);
                checkStartTime.set(Instant.now());
                final String requestText = client.getEditorAdapter().getContent();
                currentlyCheckedText.set(requestText);
                if (Strings.isNullOrEmpty(requestText)) {
                    return "<unsupported/>";
                }
                return requestText;
            }

        };

        new BrowserFunction(browser, "onInitFinishedNotificationP")
        {
            @Override
            public Object function(final Object[] arguments)
            {
                final String result = arguments[0].toString();
                final JsonObject json = (JsonObject) JsonParser.parseString(result);
                final JsonObject error = json.getAsJsonObject("error");
                if (error != null) {
                    final SidebarError sidebarError = new Gson().fromJson(error, SidebarError.class);
                    client.onInitFinished(Optional.of(sidebarError));
                } else {
                    client.onInitFinished(Optional.empty());
                }
                return null;
            }
        };

        new BrowserFunction(browser, "canCheck")
        {
            @Override
            public Object function(final Object[] arguments)
            {
                final boolean canCheck = (client.getEditorAdapter() != null)
                        && !(client.getEditorAdapter() instanceof NullEditorAdapter);
                if (!canCheck) {
                    logger.warn("Current File Editor not supported for checking or no file present.");
                }
                return canCheck;
            }
        };

        new BrowserFunction(browser, "getInputFormatP")
        {
            @Override
            public Object function(final Object[] arguments)
            {
                return client.getEditorAdapter().getInputFormat().toString();
            }
        };

        new BrowserFunction(browser, "getExternalContentP")
        {
            @Override
            public Object function(final Object[] arguments)
            {
                LogMessages.logExternalContentRequested(logger);
                final ExternalContent externalContent = client.getEditorAdapter().getExternalContent();
                if (externalContent == null) {
                    return null;
                }
                return externalContent.toString();
            }
        };

        new BrowserFunction(browser, "onCheckResultP")
        {
            @Override
            public Object function(final Object[] arguments)
            {
                final Instant checkEndedTime = Instant.now();
                LogMessages.logCheckFinishedWithDurationTime(logger,
                        Duration.between(checkStartTime.get(), checkEndedTime).toMillis());
                final String checkResult = arguments[0].toString();
                try {
                    final CheckResultFromJSON checkResultObj = new Gson().fromJson(checkResult,
                            CheckResultFromJSON.class);
                    final CheckResult result = checkResultObj.getAsCheckResult();
                    if (result == null) {
                        logger.info("Check finished with errors.");
                    } else {
                        currentCheckId.set(result.getCheckedDocumentPart().getCheckId());
                        lastCheckedDocumentReference.set(currentDocumentReference.get());
                        lastCheckedText.set(currentlyCheckedText.get());
                        client.onCheckResult(result);
                    }
                } catch (final Exception e) {
                    logger.error(e.getMessage());
                }
                return null;
            }
        };

        new BrowserFunction(browser, "getCurrentSelectionRangesP")
        {
            @Override
            public Object function(final Object[] arguments)
            {
                final List<IntRange> currentSelection = client.getEditorAdapter().getCurrentSelection();
                if (currentSelection == null) {
                    return null;
                } else {
                    final DocumentSelection selection = new DocumentSelection(currentSelection);
                    logger.debug("got selection ranges: " + selection.toString());
                    return selection.toString();
                }
            }
        };

        new BrowserFunction(browser, "selectRangesP")
        {
            @Override
            public Object function(final Object[] arguments)
            {
                LogMessages.logSelectingRange(logger);
                final List<AcrolinxMatchFromJSON> match = new Gson().fromJson((String) arguments[1],
                        new TypeToken<List<AcrolinxMatchFromJSON>>() {}.getType());
                final List<AcrolinxMatch> result = match.stream().map(
                        AcrolinxMatchFromJSON::getAsAcrolinxMatch).collect(Collectors.toCollection(ArrayList::new));
                client.getEditorAdapter().selectRanges(currentCheckId.get(), Collections.unmodifiableList(result));
                return null;
            }

        };
        new BrowserFunction(browser, "replaceRangesP")
        {
            @Override
            public Object function(final Object[] arguments)
            {
                LogMessages.logReplacingRange(logger);
                final List<AcrolinxMatchFromJSON> match = new Gson().fromJson((String) arguments[1],
                        new TypeToken<List<AcrolinxMatchFromJSON>>() {}.getType());
                final List<AcrolinxMatchWithReplacement> result = match.stream().map(
                        AcrolinxMatchFromJSON::getAsAcrolinxMatchWithReplacement).collect(
                        Collectors.toCollection(ArrayList::new));
                client.getEditorAdapter().replaceRanges(currentCheckId.get(), Collections.unmodifiableList(result));
                return null;
            }
        };
        new BrowserFunction(browser, "getDocUrlP")
        {
            @Override
            public Object function(final Object[] arguments)
            {
                final String documentRef = client.getEditorAdapter().getDocumentReference();
                currentDocumentReference.set(documentRef);
                return documentRef;
            }

        };

        new BrowserFunction(browser, "notifyAboutSidebarConfigurationP")
        {
            @Override
            public Object function(final Object[] arguments)
            {
                return null;
            }
        };

        new BrowserFunction(browser, "downloadP")
        {
            @Override
            public Object function(final Object[] arguments)
            {
                return null;
            }
        };

        new BrowserFunction(browser, "openWindowP")
        {
            @Override
            public Object function(final Object[] arguments)
            {
                final String result = arguments[0].toString();
                final String url = AcrolinxSidebarSWT.getURlFromJS(result);
                if ("".equals(url)) {
                    logger.warn("Called to open URL but no URL to open is present.");
                } else if (SidebarUtils.isValidURL(url)) {
                    Program.launch(url);
                } else {
                    logger.warn("Attempt to open invalid URL: " + url);
                }
                return null;
            }
        };

        new BrowserFunction(browser, "openLogFileP")
        {
            @Override
            public Object function(final Object[] arguments)
            {
                final String logFileLocation = LoggingUtils.getLogFileLocation();
                if (logFileLocation != null) {
                    if (!SidebarUtils.openSystemSpecific(logFileLocation)) {
                        Program.launch(new File(logFileLocation).getParent());
                    }
                }
                return null;
            }
        };

        try {
            final ClassLoader classLoader = this.getClass().getClassLoader();
            final InputStream inputStream = classLoader.getResourceAsStream("acrolinxPluginScript.js");
            final BufferedReader reader;
            if (inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line;
                final StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                final String script = sb.toString();
                reader.close();
                browser.evaluate(script);
            }

        } catch (final Exception e) {
            logger.error(e.getMessage());
        }
    }

    @SuppressWarnings("unused")
    public Browser getSidebarBrowser()
    {
        return this.browser;
    }

    @Override
    public void configure(final SidebarConfiguration configuration)
    {
        browser.execute("window.acrolinxSidebar.configure(" + configuration.toString() + ");");
    }

    @Override
    public void checkGlobal()
    {
        browser.execute("window.acrolinxPlugin.requestGlobalCheck({selection: false});");
    }

    @Override
    public void onGlobalCheckRejected()
    {
        LogMessages.logCheckRejected(logger);
        browser.execute("window.acrolinxSidebar.onGlobalCheckRejected();");
    }

    private static String buildStringOfCheckedDocumentRanges(
            final java.util.List<CheckedDocumentPart> checkedDocumentParts)
    {
        return checkedDocumentParts.stream().map(CheckedDocumentPart::getAsJS).collect(Collectors.joining(", "));
    }

    @Override
    public void invalidateRanges(final List<CheckedDocumentPart> invalidDocumentParts)
    {
        final String js = buildStringOfCheckedDocumentRanges(invalidDocumentParts);
        browser.execute("window.acrolinxSidebar.invalidateRanges([" + js + "]);");
    }

    @Override
    public void invalidateRangesForMatches(final List<? extends AbstractMatch> matches)
    {
        final List<CheckedDocumentPart> invalidDocumentParts = matches.stream().map(
                (match) -> new CheckedDocumentPart(currentCheckId.get(),
                        new IntRange(match.getRange().getMinimumInteger(),
                                match.getRange().getMaximumInteger()))).collect(Collectors.toList());
        invalidateRanges(invalidDocumentParts);
    }

    @Override
    public void loadSidebarFromServerLocation(final String serverAddress)
    {
        this.client.getInitParameters().setServerAddress(serverAddress);
        this.client.getInitParameters().setShowServerSelector(false);
        this.initBrowser();
    }

    @Override
    public void reload()
    {
        if (StartPageInstaller.isExportRequired(this.client.getInitParameters())) {
            try {
                StartPageInstaller.exportStartPageResources();
            } catch (final Exception e) {
                logger.error("Error while exporting start page resources!");
                browser.setText(SidebarUtils.sidebarErrorHTML);
            }
        }
        browser.refresh();
    }

    @Override
    public String getLastCheckedDocumentReference()
    {
        if (!"".equals(lastCheckedDocumentReference.get())) {
            return lastCheckedDocumentReference.get();
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
    protected static String getURlFromJS(final String jsonStr)
    {
        final Gson gson = new Gson();
        final AcrolinxURL element = gson.fromJson(jsonStr, AcrolinxURL.class);
        if (element != null) {
            return element.getUrl();
        } else {
            return null;
        }
    }
}
