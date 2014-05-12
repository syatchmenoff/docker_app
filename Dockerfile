FROM ubuntu:14.04
RUN apt-get update
RUN apt-get -y install openjdk-7-jre-headless
ADD target/universal/stage /opt/app
EXPOSE 8000
ENTRYPOINT ["/opt/app/bin/example-service"]
