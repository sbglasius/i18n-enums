#!/usr/bin/env bash

set -e
echo "TRAVIS_REPO_SLUG    : $TRAVIS_REPO_SLUG"
echo "TRAVIS_TAG          : $TRAVIS_TAG"
echo "TRAVIS_BRANCH       : $TRAVIS_BRANCH"
echo "TRAVIS_PULL_REQUEST : $TRAVIS_PULL_REQUEST"
echo "Publishing archives for branch $TRAVIS_BRANCH"

./gradlew clean check install --stacktrace

EXIT_STATUS=0
echo "Publishing archives for branch $TRAVIS_BRANCH"
if [[ -n ${TRAVIS_TAG} ]] || [[ ${TRAVIS_BRANCH} == 'master' && ${TRAVIS_PULL_REQUEST} == 'false' ]]; then
  if [[ -n ${TRAVIS_TAG} ]]; then
    echo "Pushing plugin version $TRAVIS_TAG to Bintray"
    ./gradlew -PpluginVersion=${TRAVIS_TAG} bintrayUpload notifyPluginPortal || EXIT_STATUS=$?

    if [[ ${EXIT_STATUS} == 0 ]]; then
        echo "Publishing documentation on gh-pages"
        # Prepare documentation
        ./gradlew -PpluginVersion=${TRAVIS_TAG} docs || EXIT_STATUS=$?

        git config --global user.name "$GIT_NAME"
        git config --global user.email "$GIT_EMAIL"
        git config --global credential.helper "store --file=~/.git-credentials"
        echo "https://$GH_TOKEN:@github.com" > ~/.git-credentials

        # Checkout gh-pages
        cd build
        git clone https://${GH_TOKEN}@github.com/${TRAVIS_REPO_SLUG}.git -b gh-pages gh-pages --single-branch > /dev/null
        cd gh-pages

        cp -r ../docs/. .
        git add *

        # Commit latest version
        git commit -a -m "Updating docs for Travis build: https://travis-ci.org/$TRAVIS_REPO_SLUG/builds/$TRAVIS_BUILD_ID"
        git push origin HEAD
        cd ..
        rm -rf gh-pages
        cd ..
    fi
  else
    echo "Publishing snapshot to OJO"
    ./gradlew artifactoryPublish || EXIT_STATUS=$?
  fi
fi
exit ${EXIT_STATUS}
