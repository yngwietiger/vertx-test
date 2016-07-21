#!/usr/bin/env bash
mvn clean package -DskipTests; \
sudo java -jar target/classflow-alexa-app-1.0-SNAPSHOT-fat.jar \
-conf src/main/conf/local.json
