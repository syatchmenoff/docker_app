#!/usr/bin/env bash

set -e

cd $( dirname "${BASH_SOURCE[0]}" )

sbt stage
docker build -t exampleservice .

docker run --rm=true -i -t -p 8000:8000 exampleservice
