#!/bin/sh

export PATH=$(echo $PATH | sed 's/8/7/g')
-T 4C clean install -s settings.xml -Dsolutions.codechecking.phase=verify