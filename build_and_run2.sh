#!/usr/bin/env bash
mvn clean package -DskipTests; \
java -jar target/my-first-app-1.0-SNAPSHOT-fat.jar \
-conf src/main/conf/my-application-conf.json
