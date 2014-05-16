#!/usr/bin/env bash

set -e

if [ ! -n "$1" ]; then
  echo "Usage: `basename $0` REPOBASE"
  exit 85
fi

REPOBASE="$1"

cd "$( dirname "${BASH_SOURCE[0]}" )"
ROOT=$( pwd )

cd $ROOT/backend
sbt stage
docker build -t "$REPOBASE"/examplebackend .
docker push "$REPOBASE"/examplebackend

cd $ROOT/frontend
docker build -t "$REPOBASE"/examplefrontend .
docker push "$REPOBASE"/examplefrontend
