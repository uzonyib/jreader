FROM openjdk:7-jdk
MAINTAINER Bela Uzonyi <uzonyib@gmail.com>

RUN \
  apt-get update && \
  apt-get install -y curl git

ENV MVN_VERSION 3.3.9
RUN \
   wget http://www.us.apache.org/dist/maven/maven-3/${MVN_VERSION}/binaries/apache-maven-${MVN_VERSION}-bin.zip -P /tmp/ &&\
   mkdir -p /usr/local/apache-maven &&\
   unzip /tmp/apache-maven-${MVN_VERSION}-bin.zip -d /usr/local/apache-maven/ &&\
   rm -rf /tmp/apache-maven-${MVN_VERSION}-bin.zip

ENV M2_HOME /usr/local/apache-maven/apache-maven-${MVN_VERSION}
ENV M2 $M2_HOME/bin

ENV PATH ${M2}:${PATH}

EXPOSE 8080 8081

RUN useradd -ms /bin/bash jreader-dev
USER jreader-dev

