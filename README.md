# Example Service

## Quick Start

    vagrant up
    vagrant ssh
    /vagrant/make.sh

## Setup
Create your Vagrant VM:

    vagrant up

Login to your Vagrant VM:

    vagrant ssh

## Building
In your Vagrant VM:

    cd /vagrant/backend
    sbt stage
    docker build -t examplebackend .

    cd /vagrant/frontend
    docker build -t examplefrontend .

## Running
In your Vagrant VM:

    docker run --rm=true -i -t --name backend examplebackend
    docker run --rm=true -i -t --name frontend --link backend:backend -p 80:80 examplefrontend
