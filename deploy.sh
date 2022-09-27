#!/bin/bash

echo "Starting publish script"

GRADLE_PROPERTIES_FILE=gradle.properties

is_not_substring(){
     if [ -z "${2##*$1*}" ]; then
            return 1
        else return 0
     fi
}

getProperty()
{
    PROP_KEY=$1
    PROP_VALUE=`cat "./gradle.properties" | grep "$PROP_KEY" | cut -d'=' -f2`
    echo $PROP_VALUE
}


PROJECT_VERSION=$(getProperty "currentVersion")
echo "Current Version: $PROJECT_VERSION"

if ! [[ "$PROJECT_VERSION" =~ ^[0-9]+((.[0-9]+)?)+$ ]]; then
    echo "Publishing snapshot version to snapshot repo..."
    if ./gradlew publishToSonatype; then
        echo "Published snapshot version to snapshot repo..."
        exit 0
    else
        exit 1
    fi
fi