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


PROJECT_VERSION=$(getProperty "CURRENT_VERSION")
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
fi