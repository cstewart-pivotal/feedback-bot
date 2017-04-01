#!/usr/bin/env bash
set -e

# export GRADLE_OPTS=-Dorg.gradle.native=false
# version=`cat version/number`
cd git-repo/feedback_form
mvn clean package
# echo 'done building'
# cp build/libs/*.war ../artifact-dir/
