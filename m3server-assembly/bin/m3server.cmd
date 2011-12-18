@echo off

set PROPS=

REM TODO:
REM if [ "$1" == "debug" ]
REM then
REM   PROPS="-Xdebug -Xrunjdwp:transport=dt_socket,address=8001,server=y,suspend=n"
REM fi

set PWD=%cd%

set PWD_URL=file:/%PWD:\=/%

set LOGGING_PROPS="-Dlog4j.debug=true -Dlog4j.configuration=%PWD_URL% -Djava.util.logging.config.file=conf\logging.properties -Dlogback.configurationFile=conf\logback.xml"
set PROXY_PROPS="-Dhttp.proxyHost=http://www-le.dienste.telekom.de -Dhttp.proxyPort=80"

echo "### Running java %PROPS% %LOGGING_PROPS% %PROXY_PROPS% -jar lib\org.apache.felix.main-4.0.2.jar"
java %PROPS% %LOGGING_PROPS% %PROXY_PROPS% -jar lib\org.apache.felix.main-4.0.2.jar
