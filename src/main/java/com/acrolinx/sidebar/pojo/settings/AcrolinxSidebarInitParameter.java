/* Copyright (c) 2018-present Acrolinx GmbH */
package com.acrolinx.sidebar.pojo.settings;

import com.acrolinx.sidebar.utils.LoggingUtils;
import com.acrolinx.sidebar.utils.SidebarUtils;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AcrolinxSidebarInitParameter
{
    private String serverAddress;
    private final String clientSignature;
    private String sidebarUrl;
    private String clientLocale;
    private final List<SoftwareComponent> clientComponents;
    private boolean showServerSelector;
    private CheckSettings checkSettings;
    private CheckSettings defaultCheckSettings;
    private boolean enableSingleSignOn;
    private boolean enforceHTTPS;
    private String logFileLocation;
    private String minimumSidebarVersion;
    private Integer minimumJavaVersion;
    private PluginSupportedParameters supported;
    private final Logger logger = LoggerFactory.getLogger(AcrolinxSidebarInitParameter.class);

    AcrolinxSidebarInitParameter(final AcrolinxSidebarInitParameterBuilder acrolinxSidebarInitParameterBuilder)
    {
        this.serverAddress = acrolinxSidebarInitParameterBuilder.serverAddress;
        this.clientSignature = acrolinxSidebarInitParameterBuilder.clientSignature;
        this.sidebarUrl = acrolinxSidebarInitParameterBuilder.sidebarUrl;
        this.clientLocale = acrolinxSidebarInitParameterBuilder.clientLocale;
        final List<SoftwareComponent> copy = new ArrayList<>();
        copy.addAll(acrolinxSidebarInitParameterBuilder.softwareComponents);
        copy.add(SidebarUtils.getJavaSdkSoftwareComponent());

        String osID = ("os." + System.getProperty("os.name").replace(" ", "."));
        String javaID = ("java.runtime." + System.getProperty("java.vendor")).replace(" ", ".");

        copy.add(new SoftwareComponent(osID, System.getProperty("os.name"), System.getProperty("os.version"),
                SoftwareComponentCategory.DETAIL));
        copy.add(new SoftwareComponent(javaID, System.getProperty("java.runtime.name"),
                System.getProperty("java.runtime.version"), SoftwareComponentCategory.DETAIL));

        this.clientComponents = copy;
        this.showServerSelector = acrolinxSidebarInitParameterBuilder.showServerSelector;
        this.checkSettings = acrolinxSidebarInitParameterBuilder.checkSettings;
        this.defaultCheckSettings = acrolinxSidebarInitParameterBuilder.defaultCheckSettings;
        this.enableSingleSignOn = acrolinxSidebarInitParameterBuilder.enableSingleSignOn;
        this.enforceHTTPS = acrolinxSidebarInitParameterBuilder.enforceHttps;
        this.logFileLocation = LoggingUtils.getLogFileLocation();
        this.minimumSidebarVersion = acrolinxSidebarInitParameterBuilder.minimumSidebarVersion;
        this.minimumJavaVersion = acrolinxSidebarInitParameterBuilder.minimumJavaVersion;
        this.supported = acrolinxSidebarInitParameterBuilder.supported;

        this.clientComponents.stream().forEach(component -> logger.info("Software component: {}", component));

        if (this.clientLocale != null) {
            logger.info("Plugin initialized with client locale: {}", this.clientLocale);
        }

        logger.info("Plugin running on Java VM: {}", SidebarUtils.getSystemJavaVmName());

        if (SidebarUtils.getSystemJavaVmName().indexOf("OpenJDK") < 0
                && SidebarUtils.getSystemJavaVmName().indexOf("HotSpot") < 0) {
            logger.warn("You are using an unsupported Java VM. This might cause errors during runtime.");
        }

        logger.info("Plugin running on Java VM Version: {}", SidebarUtils.getFullCurrentJavaVersionString());
        logger.info("Plugin running with JRE Path: {}", SidebarUtils.getPathOfCurrentJavaJre());

        if (this.minimumJavaVersion != 0) {
            logger.info("Required Java Version is: {}", this.minimumJavaVersion);

            if (SidebarUtils.getSystemJavaVersion() < this.minimumJavaVersion) {
                logger.warn("The current Java version {} does not fulfill the requirements.",
                        SidebarUtils.getSystemJavaVersion());
            }
        }

        logger.info("Plugin initialized with force https: {}", this.enforceHTTPS);
        logger.info("Plugin initialized with enableSingleSignOn: {}", this.enableSingleSignOn);

        if (this.minimumSidebarVersion != null) {
            logger.info("Plugin expects minimum sidebar version: {}", this.minimumSidebarVersion);
        }
    }

    public String getServerAddress()
    {
        return serverAddress;
    }

    public String getClientSignature()
    {
        return clientSignature;
    }

    public String getSidebarUrl()
    {
        return sidebarUrl;
    }

    public boolean getShowServerSelector()
    {
        return this.showServerSelector;
    }

    public void setServerAddress(final String serverAddress)
    {
        this.serverAddress = serverAddress;
    }

    public void setShowServerSelector(final boolean showServerSelector)
    {
        this.showServerSelector = showServerSelector;
    }

    public void setClientLocale(final String clientLocale)
    {
        if ((this.clientLocale == null) || !this.clientLocale.equalsIgnoreCase(clientLocale)) {
            logger.info("Set client locale to: {}", clientLocale);
            this.clientLocale = clientLocale;
        }
    }

    public String getLogFileLocation()
    {
        return this.logFileLocation;
    }

    public PluginSupportedParameters getSupported()
    {
        return this.supported;
    }

    @Override
    public String toString()
    {
        final Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static class AcrolinxSidebarInitParameterBuilder
    {
        String serverAddress;
        final String clientSignature;
        String sidebarUrl;
        String clientLocale;
        final List<SoftwareComponent> softwareComponents;
        boolean showServerSelector;
        CheckSettings checkSettings;
        CheckSettings defaultCheckSettings;
        boolean enableSingleSignOn;
        boolean enforceHttps;
        String minimumSidebarVersion;
        PluginSupportedParameters supported;
        int minimumJavaVersion;

        /**
         * Class to build the parameters to initialize the Acrolinx Sidebar. Two parameters have to be set,
         * all others are optional.
         *
         * @param clientSignature This signature will be provided by Acrolinx once the integration got
         *        certified.
         * @param softwareComponents This contains id, name and version of the Acrolinx integration as well
         *        as other software components that where used.
         * @see SoftwareComponent
         */
        public AcrolinxSidebarInitParameterBuilder(final String clientSignature,
                final List<SoftwareComponent> softwareComponents)
        {
            this.clientSignature = clientSignature;
            this.softwareComponents = softwareComponents;
        }

        /**
         * Configure the server that should be used to check the content. If this is not set, set member
         * 'showServerSelector' to true.
         *
         * @param serverAddress Address of the Acrolinx Server that is used to check the content.
         * @return Returns the AcrolinxInitParameterBuilder for chaining.
         */
        public AcrolinxSidebarInitParameterBuilder withServerAddress(final String serverAddress)
        {
            this.serverAddress = serverAddress;
            return this;
        }

        /**
         * The url of the Acrolinx Sidebar. If this is not set the publicly available Sidebar will be used
         * by default.
         *
         * @return Returns the AcrolinxInitParameterBuilder for chaining.
         */
        public AcrolinxSidebarInitParameterBuilder withSidebarUrl(final String sidebarUrl)
        {
            this.sidebarUrl = sidebarUrl;
            return this;
        }

        /**
         * By default the client locale is set to 'en'. It can be set to "fr", "de", etc.
         *
         * @param clientLocale Should be "en", "fr", "de", "sv", "ja", etc.
         * @return Returns the AcrolinxInitParameterBuilder for chaining.
         */
        public AcrolinxSidebarInitParameterBuilder withClientLocale(final String clientLocale)
        {
            this.clientLocale = clientLocale;
            return this;
        }

        /**
         * If this parameter is set to 'true' the Acrolinx Sidebar will provide an input field to set an url
         * for the Acrolinx Server.
         *
         * @return Returns the AcrolinxInitParameterBuilder for chaining.
         */
        public AcrolinxSidebarInitParameterBuilder withShowServerSelector(final Boolean showServerSelector)
        {
            this.showServerSelector = showServerSelector;
            return this;
        }

        /**
         * This parameter defines the check settings that will apply to all triggered checks. If this
         * parameter is set, then the default check settings and the check settings saved by the user will
         * be ignored.
         *
         * @return Returns the AcrolinxInitParameterBuilder for chaining.
         */
        public AcrolinxSidebarInitParameterBuilder withCheckSettings(final CheckSettings checkSettings)
        {
            this.checkSettings = checkSettings;
            return this;
        }

        /**
         * These check settings will be used as the initial check settings when the Acrolinx Sidebar is used
         * for the first time.
         *
         * @return Returns the AcrolinxInitParameterBuilder for chaining.
         */
        public AcrolinxSidebarInitParameterBuilder withDefaultCheckSettings(final CheckSettings defaultCheckSettings)
        {
            this.defaultCheckSettings = defaultCheckSettings;
            return this;
        }

        /**
         * If the Acrolinx Server is set up for single sign on, this parameter has to be set to 'true' in
         * order enable single sign on from the integration.
         *
         * @return Returns the AcrolinxInitParameterBuilder for chaining.
         */
        public AcrolinxSidebarInitParameterBuilder withEnableSingleSignOn(final boolean enableSingleSignOn)
        {
            this.enableSingleSignOn = enableSingleSignOn;
            return this;
        }

        /**
         * This setting will prevent any connection with an Acrolinx Server except via 'https'.
         *
         * @return Returns the AcrolinxInitParameterBuilder for chaining.
         */
        public AcrolinxSidebarInitParameterBuilder withEnforceHTTPS(final boolean enforceHttps)
        {
            this.enforceHttps = enforceHttps;
            return this;
        }

        /**
         * This can be set to require a minimum version of the sidebar. (eg. "4.4", "4.4.1")
         *
         * @return Returns the AcrolinxInitParameterBuilder for chaining.
         */
        public AcrolinxSidebarInitParameterBuilder withMinimumSidebarVersion(final String minimumSidebarVersion)
        {
            this.minimumSidebarVersion = minimumSidebarVersion;
            return this;
        }

        /**
         * Add a parameter for integrations to enable check selection in the sidebar. Requires minimum
         * sidebar version 14.5.0.
         *
         * @return Returns the AcrolinxInitParameterBuilder for chaining.
         */
        public AcrolinxSidebarInitParameterBuilder withPluginSupportedParameters(
                final PluginSupportedParameters supported)
        {
            this.supported = supported;
            return this;
        }

        /**
         * This can be set to require a minimum version of Java JRE. (eg. "8", "11")
         *
         * @return Returns the AcrolinxInitParameterBuilder for chaining.
         */
        public AcrolinxSidebarInitParameterBuilder withMinimumJavaVersion(final int minimumJavaVersion)
        {
            this.minimumJavaVersion = minimumJavaVersion;
            return this;
        }

        /**
         * Builds the AcrolinxInitParameters.
         *
         * @return Returns the AcrolinxInitParameters.
         */
        public AcrolinxSidebarInitParameter build()
        {
            return new AcrolinxSidebarInitParameter(this);
        }
    }
}
