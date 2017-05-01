#!/bin/bash

set -e -x


pushd git-repo/feedback-service/
  ./mvnw clean package
popd

cp git-repo/feedback-service/target/demo-web-app-0.0.1-SNAPSHOT.jar   build-output/.
