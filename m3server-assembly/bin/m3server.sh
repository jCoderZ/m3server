#!/bin/bash

PROPS=
if [ "$1" == "debug" ]
then
  PROPS="-Xdebug -Xrunjdwp:transport=dt_socket,address=8001,server=y,suspend=n"
fi

echo java $PROPS -Dlogback.configurationFile=conf/logback.xml -Dhttp.proxyHost=http://www-le.dienste.telekom.de -Dhttp.proxyPort=80 -jar lib/org.apache.felix.main-4.0.2.jar
java $PROPS -Dlogback.configurationFile=conf/logback.xml -Dhttp.proxyHost=http://www-le.dienste.telekom.de -Dhttp.proxyPort=80 -jar lib/org.apache.felix.main-4.0.2.jar
