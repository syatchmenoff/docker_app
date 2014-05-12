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

    cd /vagrant
    docker build -t exampleservice .

## Running
In your Vagrant VM:

    docker run --rm=true -i -t -p 8000:8000 exampleservice
