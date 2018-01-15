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
            if is_not_substring "$SNAPSHOT" "$PROJECT_VERSION"; then
                echo "ready for release!"
                if ./gradlew pP publish publishDocsAndCreateGithubReleaseTag closeRepository -Psigning.keyId="$keyId" -Psigning.password="$password" -Psigning.secretKeyRingFile="../secring.gpg"; then
                    echo "Done with release"
                    exit 0
                else
                    exit 1
                fi
            else
                exit 1
            fi
        else
           exit 1
        fi
    else
        exit 1
fi