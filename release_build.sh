#!/bin/sh
echo $PATH

export PATH=$(echo $PATH | sed 's/8/7/g')
echo $PATH

export JAVA_HOME=$(echo $JAVA_HOME | sed 's/8/7/g')
echo $JAVA_HOME
mvn -s settings.xml jgitflow:release-start -DreleaseVersion=$RELEASE -DdevelopmentVersion=$DEVELOP -DskipTests -Dmaven.javadoc.skip=true
mvn -s settings.xml jgitflow:release-finish -DskipTests -Dmaven.javadoc.skip=true
