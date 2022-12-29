#!/bin/sh

source ~/.bashrc
if [[ $? -ne 0 ]]; then
   echo "ERROR: error importing .bashrc"
   exit -1
fi

export PROJECT_ROOT=`pwd`

mkdir ../letsdata-data-interface-jars
# download the letsdata-data-interface jar
curl https://testletsdatawebtemplates.s3.amazonaws.com/downloads/letsdata-data-interface-1.0-SNAPSHOT.jar --output ../letsdata-data-interface-jars/letsdata-data-interface-1.0-SNAPSHOT.jar
# download the letsdata-data-interface sources jar
curl https://testletsdatawebtemplates.s3.amazonaws.com/downloads/letsdata-data-interface-1.0-SNAPSHOT-sources.jar --output ../letsdata-data-interface-jars/letsdata-data-interface-1.0-SNAPSHOT-sources.jar

# install the letsdata-data-interface jar in the maven repo
mvn -e install:install-file -Dfile=../letsdata-data-interface-jars/letsdata-data-interface-1.0-SNAPSHOT.jar -Dsources=../letsdata-data-interface-jars/letsdata-data-interface-1.0-SNAPSHOT-sources.jar -DgroupId=com.resonance.letsdata -DartifactId=letsdata-data-interface -Dpackaging=jar -Dversion=1.0-SNAPSHOT

# cd to project root dir
cd $PROJECT_ROOT
if [[ $? -ne 0 ]]; then
   echo "ERROR: project root directory not found"
   exit -1
fi

# compile the project
mvn clean compile assembly:single package
if [[ $? -ne 0 ]]; then
   echo "ERROR: build project FAILED"
   exit -1
fi

# install the lets-data-common-crawl jar in the maven repo
mvn -e install:install-file -Dfile=./target/letsdata-common-crawl-1.0-SNAPSHOT-jar-with-dependencies.jar -Dsources=./target/letsdata-common-crawl-1.0-SNAPSHOT-sources.jar -DgroupId=com.letsdata.common.crawl -DartifactId=letsdata-common-crawl -Dpackaging=jar -Dversion=1.0-SNAPSHOT
