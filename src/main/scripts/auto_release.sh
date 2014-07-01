#!/usr/bin/expect

set RELEASE_VER [lrange $argv 0 0]
set RELEASE_TAG [lrange $argv 1 1]
set NEW_DEV_VER [lrange $argv 2 2]

set timeout -1
spawn ../release_prepare

expect "What is the release version for"
send "$RELEASE_VER\r"

expect "What is SCM release tag or label for"
send "$RELEASE_TAG\r"

expect "What is the new development version for"
send "$NEW_DEV_VER\r"

expect eof
