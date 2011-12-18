#!/bin/bash

PROPS=
if [ "$1" == "debug" ]
then
  PROPS="-Xdebug -Xrunjdwp:transport=dt_socket,address=8001,server=y,suspend=n"
fi

CWD=$(pwd)

case $(uname) in
  CYGWIN*)
    LOG4J_CONFIG="file:/$(cygpath -m $PWD)/conf/log4j.properties" 
    ;;
  *)
    LOG4J_CONFIG="file://$PWD/conf/log4j.properties"
    ;;
esac


LOGGING_PROPS="-Dlog4j.debug=true -Dlog4j.configuration=$LOG4J_CONFIG -Djava.util.logging.config.file=conf/logging.properties -Dlogback.configurationFile=conf/logback.xml"
PROXY_PROPS="-Dhttp.proxyHost=http://www-le.dienste.telekom.de -Dhttp.proxyPort=80"

echo "### Running java $PROPS $LOGGING_PROPS $PROXY_PROPS -jar lib/org.apache.felix.main-4.0.2.jar"
java $PROPS $LOGGING_PROPS $PROXY_PROPS -jar lib/org.apache.felix.main-4.0.2.jar
