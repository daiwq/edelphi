mvn clean package
VERSION=$(mvn org.apache.maven.plugins:maven-help-plugin:2.1.1:evaluate -Dexpression=project.version|grep -Ev '(^\[|Download\w+:)')
docker build -t edelphi .
docker tag $(docker images -q edelphi) metatavu/edelphi:$VERSION
docker push metatavu/edelphi
