#!/bin/sh
export PATH=$(echo $PATH | sed 's/8/7/g')
mvn -s settings.xml clean install -Dsolutions.codechecking.phase=verify