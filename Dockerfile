FROM ubuntu:14.04
RUN apt-get update
RUN apt-get -y install python
ADD app /opt/app
EXPOSE 8000
ENTRYPOINT ["/opt/app/start"]
