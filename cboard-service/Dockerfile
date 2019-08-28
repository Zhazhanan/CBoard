##################
#Dockerfile to build springBoot cboard container images
#Base on cenos
##################

# Set the base image to centos
FROM centos:7

# File Author / Maintainer
MAINTAINER wangkun (chinesepandahuha@yahoo.com)

LABEL Description="huha-cboard images "  Version="dev"

# install jdk
RUN yum install -y java-1.8.0-openjdk java-1.8.0-openjdk-devel wget vim

# install Chinese font
RUN yum install -y bitmap-fonts bitmap-fonts-cjk
RUN yum install -y kde-l10n-Chinese

RUN yum -y install bzip2

# remote download
RUN wget https://bitbucket.org/ariya/phantomjs/downloads/phantomjs-2.1.1-linux-x86_64.tar.bz2 -P install

RUN tar -jxf install/phantomjs-2.1.1-linux-x86_64.tar.bz2 -C /opt \
    && ln -s /opt/phantomjs-2.1.1-linux-x86_64/bin/phantomjs /opt/phantomjs-2.1.1

# phantomjs requirements
#RUN yum install -y glibc.i686 zlib.i686 fontconfig freetype freetype-devel fontconfig-devel libstdc++ libfreetype.so.6 libfontconfig.so.1 libstdc++.so.6
RUN yum -y install wget fontconfig

ADD phantom.js /usr/share/phantom/
ADD application.yml application.yml
ADD cboard.jar huhaCboard.jar

#RUN bash -c 'touch /huhaCboard.jar'
ENV JAVA_OPTS="-Xms1024m -Xmx1024m -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m "

ENTRYPOINT ["sh", "-c","java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar huhaCboard.jar --spring.config.location=file:application.yml"]
