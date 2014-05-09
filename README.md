# Docker Example App

## Building
    cd /vagrant
    docker build -t webservice .

## Running
    docker run --rm=false -i -t -p 8000:8000 webservice
