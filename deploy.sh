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
    if ./gradlew pP publish; then
        exit 0
    else
        exit 1
    fi
fi

if [ "$STAGE" = "release" ]; then
        echo "Releasing..."
        if ./gradlew pP publish -Psigning.keyId="$keyId" -Psigning.password="$password" -Psigning.secretKeyRingFile="../secring.gpg"; then
            echo "Done with first publish step."
            if is_not_substring "SNAPSHOT" "$PROJECT_VERSION"; then
                echo "ready for release!"
                if ./gradlew pP publish closeRepository -Psigning.keyId="$keyId" -Psigning.password="$password" -Psigning.secretKeyRingFile="../secring.gpg"; then
                    echo "Done with release"
                    exit 0
                else
                    echo "Failed to release"
                    exit 1
                fi
            else
                echo "Failed because is no release version on master, please move to develop branch. Published release version anyways."
                exit 1
            fi
        else
           exit 1
        fi
    else
        exit 1
fi