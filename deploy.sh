#!/bin/bash

STAGE=$1

echo "$1"

GRADLE_PROPERTIES_FILE=gradle.properties

is_not_substring(){
    case "$2" in
        *$1*) return 1;;
        *) return 0;;
    esac
}

getProperty()
{
    PROP_KEY=$1
    PROP_VALUE=`cat $GRADLE_PROPERTIES_FILE | grep "$PROP_KEY" | cut -d'=' -f2`
    echo $PROP_VALUE
}

PROJECT_VERSION=$(getProperty "currentVersion")

if [ "$STAGE" = "snapshot" ]; then
        ./gradlew pP publish
        exit 1;
fi

if [ "$STAGE" = "release" ]; then
        ./gradlew pP publish
        if [ "$STAGE" = 'release' ] && is_not_substring "$SNAPSHOT" "$PROJECT_VERSION"; then
            ./gradlew pP publish publishDocsAndCreateGithubReleaseTag closeRepository
        fi
        exit 1;
fi