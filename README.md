# Acrolinx Sidebar Java SDK

[![Maven Central](https://img.shields.io/maven-central/v/com.acrolinx.client/sidebar-sdk)](https://search.maven.org/artifact/com.acrolinx.client/sidebar-sdk)
![Build Status](https://github.com/acrolinx/sidebar-sdk-java/actions/workflows/build-deploy.yml/badge.svg)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=acrolinx_sidebar-sdk-java&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=acrolinx_sidebar-sdk-java)

This is a library for integrating the [Acrolinx](https://www.acrolinx.com/) Sidebar
into different Java UI framework-based applications (JFX, Swing, and SWT).

You can use the Maven artifact [`com.acrolinx.client:sidebar-sdk`](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.acrolinx.client%22%20a%3A%22sidebar-sdk%22%20)
to integrate Acrolinx in your Java application.

See: [Build With Acrolinx](https://support.acrolinx.com/hc/en-us/categories/10209837818770-Build-With-Acrolinx)

## Live Demo

[Acrolinx Sidebar Java Live Demo](https://github.com/acrolinx/acrolinx-sidebar-demo-java#live-demo)

## The Acrolinx Sidebar

The Acrolinx Sidebar is designed to show up beside the window where you edit your content.
You use it for checking, reviewing, and correcting your content.
To get an impression what the Sidebar looks like in existing integrations, have a look at
[Sidebar Quick Start](https://support.acrolinx.com/hc/en-us/articles/10252588984594-Sidebar-Quick-Start).

## Prerequisites

Please contact [Acrolinx SDK support](https://github.com/acrolinx/acrolinx-coding-guidance/blob/main/topics/sdk-support.md)
for consulting and getting your integration certified.
This sample works with a test license on an internal Acrolinx URL.
This license is only meant for demonstration and developing purposes.
Once you finished your integration, you'll have to get a license for your integration from Acrolinx.

Acrolinx offers different other SDKs, and examples for developing integrations.

Before you start developing your own integration, you might benefit from looking into:

* [Build With Acrolinx](https://support.acrolinx.com/hc/en-us/categories/10209837818770-Build-With-Acrolinx),
* the [Guidance for the Development of Acrolinx Integrations](https://github.com/acrolinx/acrolinx-coding-guidance),
* the [Acrolinx SDKs](https://github.com/acrolinx?q=sdk), and
* the [Acrolinx Demo Projects](https://github.com/acrolinx?q=demo).

## Getting Started

### Build the Project

* Cone the project.
* Run `gradlew build` to build the SDK locally.

### Using the SDK

Note that, if you’re using Java version 11 or later you’ll need to provide the following modules JavaFX modules:

* "javafx.web"
* "javafx.swing"

1. Just reference the Maven artifact `com.acrolinx.client:sidebar-sdk` that is available on
[Maven Central](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.acrolinx.client%22%20a%3A%22sidebar-sdk%22%20).
Have a look at the [`build.gradle`](build.gradle) file if you use Gradle.
2. Implement:
    + `AcrolinxIntegrationInterface`, and the
    + `InputAdapterInterface`.
    + The `AcrolinxSidebarInitParameterBuilder` helps you initialize the Acrolinx Sidebar.
3. Check out the [Sidebar SDK Java API Reference](https://acrolinx.github.io/sidebar-sdk-java/) for more details.

![Architecture and Interfaces](img/ArchitectureInterfaces.png)

## SDK Features

1. Support for UI-frameworks:
    + JavaFX
    + Swing
    + SWT (For Windows WebView2 required for Sidebar version 15 or higher, other platforms will use default Browser)
2. `LookupRangesDiff` - Provides [lookup](https://github.com/acrolinx/acrolinx-coding-guidance/blob/main/topics/text-lookup.md)
functionality.
3. **Start page**: Provides an interactive way to sign in to Acrolinx with built-in error handling.
4. Provides [logging](https://github.com/acrolinx/sidebar-sdk-dotnet/blob/main/Acrolinx.Sidebar/Util/Logging/Logger.cs).
Logging can be activated via:

    ```java
    LoggingUtils.setupLogging("AcrolinxDemoClientJFX");
    ```

5. Provides an `AcrolinxStorage` that can be used to persist Sidebar settings in the data store of the host editors.
If not set, the SDK will default to the browsers LocalStorage.

6. Provides `MultiSidebar` usage that can be used to create and manage multiple Sidebars. Every document can get its own Sidebar.
Helping preserve Acrolinx results switching between documents.

7. Provides Batch Checking functionality to check multiple documents with a single click.

### SWT Dependency Resolution
If you want to use the SWT UI framework:
The Maven property `osgi.platform` doesn't seem to be handled by Gradle. Instead, you'll have to do a bit of special dependency resolution to correctly grab the platform-specific dependencies.
Create a variable `SWT_VERSION` specifying the desired version for example: `SWT_VERSION=3.117.0`

* Add SWT dependency
Use dependency configuration as required (example: `implementation, api, compile, etc`) [Example Dependency Configuration](https://github.com/acrolinx/acrolinx-sidebar-demo-java/blob/80c8a26005722f9d07d79f041a6ff1fd2119d479/build.gradle#L140)

* Fetch Native SWT dependency based on Operating System [Example OS based fetch](https://github.com/acrolinx/acrolinx-sidebar-demo-java/blob/80c8a26005722f9d07d79f041a6ff1fd2119d479/build.gradle#L24)

* Dependency substitution configuration [Example Configuration](https://github.com/acrolinx/acrolinx-sidebar-demo-java/blob/80c8a26005722f9d07d79f041a6ff1fd2119d479/build.gradle#L85)

### Using Sidebar v15 with SWT

_Only for **Windows OS** based Integrations_

If you choose to use Sidebar v15 with SWT, you additionally need to install the **WebView2 Runtime** on the user's system.

This is required as Sidebar v15 **doesn't** support **Internet Explorer**.

Follow the guidelines provided by Microsoft to install the [WebView2 Runtime](https://developer.microsoft.com/en-us/microsoft-edge/webview2/).

## SDK Architecture

![Java SDK Overview](img/SketchJavaSDKComponents.png)

## Instructions for Contributing Code

## References

* The [Sidebar DEMO Java](https://github.com/acrolinx/acrolinx-sidebar-demo-java) is built based on this SDK.
* The API documentation is published on the [GitHub Pages](https://acrolinx.github.io/sidebar-sdk-java/).
* The Sidebar SDKs are based on the [Acrolinx Sidebar Interface](https://acrolinx.github.io/sidebar-interface/).
* This project depends on (unmodified) Logback for logging.
Logback is Copyright (C) 1999-2017, QOS.ch and licensed under the EPL-1.0. You can get the source from [github.com/qos-ch/logback](https://github.com/qos-ch/logback).
The Logback website is at [logback.qos.ch/license.html](https://logback.qos.ch/license.html)

## License

Copyright 2016-present Acrolinx GmbH

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at:

[https://www.apache.org/licenses/LICENSE-2.0](https://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

For more information visit: [https://www.acrolinx.com](https://www.acrolinx.com)
