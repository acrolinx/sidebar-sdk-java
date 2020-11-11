#!/bin/sh -f

# Trigger a new Travis-CI job.

body="{
\"request\": {
  \"branch\":\"master\"
  automated request
}}"

# "%2F" creates a literal "/" in the URL, that is not interpreted as a
# segment or directory separator.
curl -s -X POST \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -H "Travis-API-Version: 3" \
  -H "Authorization: token ${TRAVIS_ACCESS_TOKEN}" \
  -d "$body" \
  "https://api.travis-ci.com/github/5133080/acrolinx-sidebar-demo-java/requests" \
 | tee /tmp/travis-request-output.$$.txt

if grep -q '"@type": "error"' /tmp/travis-request-output.$$.txt; then
    exit 1
fi
if grep -q 'access denied' /tmp/travis-request-output.$$.txt; then
    exit 1
fi