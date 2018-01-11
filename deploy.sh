#!/bin/bash

STAGE = $1

GRADLE_PROPERTIES_FILE=gradle.properties

function getProperty {
    PROP_KEY=$1
    PROP_VALUE=`cat $GRADLE_PROPERTIES_FILE | grep "$PROP_KEY" | cut -d'=' -f2`
    echo $PROP_VALUE
}
PROJECT_VERSION=$(getProperty "currentVersion")

if [ "$(STAGE)" == 'snapshot' ]; then
        ./gradlew pP publish
        exit 1;
fi

if [ "$(STAGE)" == 'release' ]; then
        ./gradlew pP publish
        if [ "$(STAGE)" == 'release' && "$(PROJECT_VERSION)" != *"$SNAPSHOT"*]; then
            ./gradlew pP publish publishDocsAndCreateGithubReleaseTag closeRepository
        fi
        exit 1;
fi