FROM jenkins:latest
MAINTAINER Bela Uzonyi <uzonyib@gmail.com>

USER root

RUN \
    apt-get update && \
    apt-get install -y openjdk-7-jdk xvfb

RUN \
    wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - && \
    echo "deb http://dl.google.com/linux/chrome/deb/ stable main" > /etc/apt/sources.list.d/google.list && \
    apt-get update && \
    apt-get install -y google-chrome-stable && \
    rm -rf /var/lib/apt/lists/*

ENV MVN_VERSION 3.3.9
RUN \
    wget http://www.us.apache.org/dist/maven/maven-3/${MVN_VERSION}/binaries/apache-maven-${MVN_VERSION}-bin.zip -P /tmp/ &&\
    mkdir -p /usr/local/apache-maven &&\
    unzip /tmp/apache-maven-${MVN_VERSION}-bin.zip -d /usr/local/apache-maven/ &&\
    rm -rf /tmp/apache-maven-${MVN_VERSION}-bin.zip

ENV M2_HOME /usr/local/apache-maven/apache-maven-${MVN_VERSION}
ENV M2 $M2_HOME/bin

ENV PATH ${M2}:${PATH}

USER jenkins

COPY plugins.txt /usr/share/jenkins/ref/
RUN /usr/local/bin/plugins.sh /usr/share/jenkins/ref/plugins.txt

