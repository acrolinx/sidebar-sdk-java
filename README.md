# Acrolinx Sidebar Java SDK

| Master |
| ------ |
| [![Build Status Master](https://travis-ci.org/acrolinx/sidebar-sdk-java.svg?branch=master)](https://travis-ci.org/acrolinx/sidebar-sdk-java) |

This is a library for integrating the [Acrolinx](https://www.acrolinx.com/) Sidebar
into different Java UI framework-based applications (JFX, Swing, and SWT).

You can use the Maven artifact [`com.acrolinx.client:sidebar-sdk`](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.acrolinx.client%22%20a%3A%22sidebar-sdk%22%20)
to integrate Acrolinx in your Java application.

See: [Getting Started with Custom Integrations](https://docs.acrolinx.com/customintegrations)

## Live Demo

[Acrolinx Sidebar Java Live Demo](https://github.com/acrolinx/acrolinx-sidebar-demo-java#live-demo)

## The Acrolinx Sidebar

The Acrolinx Sidebar is designed to show up beside the window where you edit your content.
You use it for checking, reviewing, and correcting your content.
To get an impression what the Sidebar looks like in existing integrations, have a look at
[Get Started With the Sidebar](https://support.acrolinx.com/hc/en-us/articles/205697451-Get-Started-With-the-Sidebar).

## Prerequisites

Please contact [Acrolinx SDK support](https://github.com/acrolinx/acrolinx-coding-guidance/blob/master/topics/sdk-support.md)
for consulting and getting your integration certified.
This sample works with a test license on an internal Acrolinx URL.
This license is only meant for demonstration and developing purposes.
Once you finished your integration, you'll have to get a license for your integration from Acrolinx.
  
Acrolinx offers different other SDKs, and examples for developing integrations.

Before you start developing your own integration, you might benefit from looking into:

* [Getting Started with Custom Integrations](https://docs.acrolinx.com/customintegrations),
* the [Guidance for the Development of Acrolinx Integrations](https://github.com/acrolinx/acrolinx-coding-guidance),
* the [Acrolinx SDKs](https://github.com/acrolinx?q=sdk), and
* the [Acrolinx Demo Projects](https://github.com/acrolinx?q=demo).

## Getting Started

### Build the Project

### Using the SDK

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
    + JavaFx
    + Swing
    + SWT
2. `LookupRangesDiff` - Provides [lookup](https://github.com/acrolinx/acrolinx-coding-guidance/blob/master/topics/text-lookup.md)
  functionality.
3. **Start page**: Provides interactive way to sign in to Acrolinx with built-in error handling.
4. Provides [logging](https://github.com/acrolinx/sidebar-sdk-dotnet/blob/master/Acrolinx.Sidebar/Util/Logging/Logger.cs).
   Logging can be activated via:

    ```java
    LoggingUtils.setupLogging("AcrolinxDemoClientJFX");
    ```

5. Provides an `AcrolinxStorage` that can be used to persist Sidebar settings in the data store of the host editors.
   If not set, the SDK will default to the browsers LocalStorage.

## SDK Architecture

![Java SDK Overview](img/SketchJavaSDKComponents.png)

## Instructions for Contributing Code

### Branches and Releasing

1. Please add new features using the master branch, or submit a pull request.

   ```bash
   git checkout master
   # make your changes
   git commit
   git push
   ```

    If your build on Travis was successful, a new snapshot version will be automatically available via [Maven snapshot repository](https://oss.sonatype.org/content/repositories/snapshots/com/acrolinx/client/sidebar-sdk/).

2. Once you tested your new features, remove the snapshot from the currentVersion property in the `gradle.properties` file.

3. Commit and push your changes. If all goes right, the artifact is released to [Maven Central](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.acrolinx.client%22%20a%3A%22sidebar-sdk%22%20).
Note that it might take quite a while until the new version shows up in [Maven Central](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.acrolinx.client%22%20a%3A%22sidebar-sdk%22%20), but it will be immediately available in the [oss staging repository](https://oss.sonatype.org/content/groups/staging/com/acrolinx/client/sidebar-sdk/).

If that build fails, you might have to login into [Nexus Repository Manager](https://oss.sonatype.org/#welcome) and drop falsely created repositories, before triggering a new release build.

4. Run the Gradle Task for creating a release tag and pushing it GitHub:

   ```bash
   ./gradlew createGithubReleaseTag
   ```

5. Once the tag is pushed to GitHub, TravisCI will automatically update the [API documentation on the GitHub Pages](https://acrolinx.github.io/sidebar-sdk-java/).

6. Don't forget to commit and push a new SNAPSHOT version.

### Dependency Updates

To check if the SDK uses any outdated libraries, run:

```bash
./gradlew dependencyUpdates -Drevision=release
```

This will generate a report in the projects' build folder. Check the report to find any outdated libraries.

To update the dependency libraries, run the following Gradle task. This will update to the latest stable release version
and exclude any alpha or beta versions.

```bash
./gradlew useLatestVersions && ./gradlew useLatestVersionsCheck
```

### Vulnerable Dependencies Check

```bash
./gradlew dependencyCheckAnalyze --info
```

This generates an HTML report in the projects build folder. It lists all vulnerable dependencies and where they're
referenced found by the [Dependency-Check-Gradle](https://github.com/jeremylong/dependency-check-gradle) plugin.

## References

* The [Sidebar DEMO Java](https://github.com/acrolinx/acrolinx-sidebar-demo-java) is built based on this SDK.
* The API documentation is published on the [GitHub Pages](https://acrolinx.github.io/sidebar-sdk-java/).
* The Sidebar SDKs are based on the [Acrolinx Sidebar Interface](https://acrolinx.github.io/sidebar-sdk-js/).
* This project depends on (unmodified) Logback for logging.
  Logback is Copyright (C) 1999-2017, QOS.ch and licensed under the EPL-1.0. You can get the source from [github.com/qos-ch/logback](https://github.com/qos-ch/logback).
  The Logback website is at [logback.qos.ch/license.html](https://logback.qos.ch/license.html)

## License

Copyright 2016-present Acrolinx GmbH

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at:

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

For more information visit: [https://www.acrolinx.com](https://www.acrolinx.com)
