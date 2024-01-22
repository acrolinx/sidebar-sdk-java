#!/bin/bash

echo "Starting snapshot publishing script"

getProperty() {
    PROP_KEY=$1
    PROP_VALUE=`cat "./gradle.properties" | grep "$PROP_KEY" | cut -d'=' -f2`
    echo $PROP_VALUE
}

readonly PROJECT_VERSION=$(getProperty "currentVersion")
echo "Current Version: $PROJECT_VERSION"

if [[ "$PROJECT_VERSION" == *"SNAPSHOT"* ]]; then
    echo "Publishing snapshot version to snapshot repo..."
    if ./gradlew publishToSonatype; then
        echo "Published snapshot version to snapshot repo..."
        exit 0
    else
        exit 1
    fi
else
  echo "Could not publish snapshot as the current version does not have -SNAPSHOT"
fi
