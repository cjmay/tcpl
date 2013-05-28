#!/bin/bash

wget --user="$1" --password="$2" --read-timeout=0 https://stream.twitter.com/1.1/statuses/sample.json
