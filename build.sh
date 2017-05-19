#!/bin/sh
echo $PATH

export PATH=$(echo $PATH | sed 's/8/7/g')
echo $PATH

export JAVA_HOME=$(echo $JAVA_HOME | sed 's/8/7/g')
echo $JAVA_HOME
mvn -s settings.xml clean install -Dsolutions.codechecking.phase=verify