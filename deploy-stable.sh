#!/bin/bash

echo "Starting stable publishing script"

getProperty() {
    PROP_KEY=$1
    PROP_VALUE=`cat "./gradle.properties" | grep "$PROP_KEY" | cut -d'=' -f2`
    echo $PROP_VALUE
}

readonly PROJECT_VERSION=$(getProperty "currentVersion")
echo "Current Version: $PROJECT_VERSION"

if [[ ! "$PROJECT_VERSION" == *"SNAPSHOT"* ]]; then
    echo "Publishing release version to staging repo..."
        if ./gradlew publishToSonatype; then
            echo "Done with publish step."
            echo "Starting close and release step"
            if ./gradlew closeAndReleaseRepository; then
                echo "Done with release step."
                echo "Trying to create Github Release Tag"
                if ./gradlew createGithubReleaseTag; then
                    echo "::set-output name=TAGNAME::release-$PROJECT_VERSION"
                    echo "::set-output name=RELEASE::true"
                    exit 0
                else
                    echo "Can't create Github Release Tag. Please do manually."
                    exit 1
                fi
            else
                exit 1
            fi
        else
            exit 1
        fi
else
  echo "Could not publish stable as the current version has -SNAPSHOT"
fi
