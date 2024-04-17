# Acrolinx Sidebar Java SDK

[![Build](https://github.com/acrolinx/sidebar-sdk-java/actions/workflows/build.yml/badge.svg)](https://github.com/acrolinx/sidebar-sdk-java/actions/workflows/build.yml)

This is a Java library for integrating the [Acrolinx](https://www.acrolinx.com/) Sidebar into different Java UI framework-based applications (JFX, Swing, and SWT).

You can use the Maven artifact [`com.acrolinx.client:sidebar-sdk`](https://central.sonatype.com/artifact/com.acrolinx.client/sidebar-sdk)
to integrate Acrolinx in your Java application.

See: [Build With Acrolinx](https://support.acrolinx.com/hc/en-us/categories/10209837818770-Build-With-Acrolinx)

## The Acrolinx Sidebar

The Acrolinx Sidebar is designed to show up beside the window where you edit your content.
You use it for checking, reviewing, and correcting your content.
To get an impression what the Sidebar looks like in existing integrations, have a look at
[Sidebar Quick Start](https://support.acrolinx.com/hc/en-us/articles/10252588984594-Sidebar-Quick-Start).

## Prerequisites

Please contact [Acrolinx SDK support](https://github.com/acrolinx/acrolinx-coding-guidance/blob/main/topics/sdk-support.md)
for consulting and getting your integration certified.

Before you start developing your own integration, you might benefit from looking into:

* [Build With Acrolinx](https://support.acrolinx.com/hc/en-us/categories/10209837818770-Build-With-Acrolinx),
* the [Guidance for the Development of Acrolinx Integrations](https://github.com/acrolinx/acrolinx-coding-guidance),
* the [Acrolinx SDKs](https://github.com/acrolinx?q=sdk), and
* the [Acrolinx Demo Projects](https://github.com/acrolinx?q=demo).

## Getting Started

### Build the Project

1. You need Java 11 to build this project.
2. This project uses [Gradle](https://gradle.org/).
To build this project with the [Gradle Wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html#sec:using_wrapper), execute the following command:

```bash
./gradlew build
```

on an UNIX system, or

```batch
gradlew build
```

on a Windows computer.

### Using the SDK

1. Reference the Maven artifact `com.acrolinx.client:sidebar-sdk` which is available on
[Maven Central](https://central.sonatype.com/artifact/com.acrolinx.client/sidebar-sdk).
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
    + SWT
2. `LookupRangesDiff` - Provides [lookup](https://github.com/acrolinx/acrolinx-coding-guidance/blob/main/topics/text-lookup.md)
functionality.
3. **Start page**: Provides an interactive way to sign in to Acrolinx with built-in error handling.
4. Provides [logging](https://github.com/acrolinx/acrolinx-coding-guidance/blob/main/topics/logging.md).
Logging can be activated via:

    ```java
    LoggingUtils.setupLogging("AcrolinxDemoClientJFX");
    ```

5. Provides an `AcrolinxStorage` that can be used to persist Sidebar settings in the data store of the host editors.
If not set, the SDK will default to the browsers LocalStorage.

6. Provides `MultiSidebar` usage that can be used to create and manage multiple Sidebars. Every document can get its own Sidebar.
Helping preserve Acrolinx results switching between documents.

7. Provides Batch Checking functionality to check multiple documents with a single click.

### Using Sidebar v15 with SWT

_Only for **Windows OS** based Integrations_

If you choose to use Sidebar v15 with SWT, you additionally need to install the **WebView2 Runtime** on the user's system.

This is required as Sidebar v15 **doesn't** support **Internet Explorer**.

Follow the guidelines provided by Microsoft to install the [WebView2 Runtime](https://developer.microsoft.com/en-us/microsoft-edge/webview2/).

## SDK Architecture

![Java SDK Overview](img/SketchJavaSDKComponents.png)

## References

* The [Sidebar DEMO Java](https://github.com/acrolinx/acrolinx-sidebar-demo-java) is built based on this SDK.
* The API documentation is published on the [GitHub Pages](https://acrolinx.github.io/sidebar-sdk-java/).
* The Sidebar SDKs are based on the [Acrolinx Sidebar Interface](https://acrolinx.github.io/sidebar-interface/).
