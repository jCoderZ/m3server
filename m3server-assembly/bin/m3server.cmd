@echo off

java -Dlogback.configurationFile=conf\logback.xml -Dhttp.proxyHost=http://www-le.dienste.telekom.de -Dhttp.proxyPort=80 -jar lib\org.apache.felix.main-4.0.2.jar
