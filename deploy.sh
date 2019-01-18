#!/bin/bash

STAGE=$1

echo "Stage: $1"

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

if [ "$STAGE" = "snapshot" ]; then
    if ./gradlew publish; then
        exit 0
    else
        exit 1
    fi
fi

if [ "$STAGE" = "release" ]; then
        echo "Publishing release version to staging repo..."
        if ./gradlew publish -Psigning.keyId="$keyId" -Psigning.password="$password" -Psigning.secretKeyRingFile="../secring.gpg"; then
            echo "Done with publish step."
        else
           exit 1
        fi
    else
        exit 1
fi