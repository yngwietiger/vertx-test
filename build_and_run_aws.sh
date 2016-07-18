#!/usr/bin/env bash
mvn clean package -DskipTests; \
sudo java -jar target/my-first-app-1.0-SNAPSHOT-fat.jar \
-conf src/main/conf/aws.json
