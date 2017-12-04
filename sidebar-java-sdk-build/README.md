## Releasing a new version of the acrolinx-sidebar-java-sdk

In the gradle.properties file, remove "SNAPSHOT" from the version number.
Commit the updated file.

Find the build job on Jenkins find project "acrolinx-sidebar-javasdk" und trigger a build (or wait for its automated triggering).

Once the build finished, you'll should have a new release in Github.

Last increment the version number, put a "SNAPSHOT" behind it, commit and push it.
