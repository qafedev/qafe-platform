#!/bin/sh
echo $PATH
if [ -r ./env.sh ]; then
  . ./env.sh
  echo 'env.sh found'
fi
export PATH=$(echo $PATH | sed 's/8/7/g')
echo $PATH
mvn -s settings.xml clean install -Dsolutions.codechecking.phase=verify