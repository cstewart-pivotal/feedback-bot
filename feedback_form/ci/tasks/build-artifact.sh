
#!/bin/bash

set -e -x

pushd git-repo/feedback_form
  ./mvnw clean package
popd

cp git-repo/feedback_form/target/demo-web-app-0.0.1-SNAPSHOT.jar   build-output/.
