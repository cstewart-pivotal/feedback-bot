#!/bin/bash

set -e -x


pushd git-repo/feedback-service/
  ./mvnw clean package
popd

cp git-repo/feedback-service/target/feedback-service-0.0.1-SNAPSHOT.jar   build-output/.
