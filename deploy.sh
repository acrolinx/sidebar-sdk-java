#!/bin/bash

STAGE = $1

if [ "$(STAGE)" == 'develop' ]; then
        ./gradlew pP publish
        exit 1;
fi

if [ "$(STAGE)" == 'production' ]; then
        ./gradlew pP publish pP publish publishDocsAndCreateGithubReleaseTag closeRepostitory
        exit 1;
fi