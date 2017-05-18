#!/bin/sh

if [ -r ./env.sh ]; then
  . ./env.sh
fi
export PATH=$(echo $PATH | sed 's/8/7/g')
mvn -s settings.xml clean install -Dsolutions.codechecking.phase=verify