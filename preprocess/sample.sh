#!/bin/bash

if [ $# -ne 2 ]
then
    echo 'Specify a username and password.'
    exit 1
fi

wget --user="$1" --password="$2" --read-timeout=0 https://stream.twitter.com/1.1/statuses/sample.json
