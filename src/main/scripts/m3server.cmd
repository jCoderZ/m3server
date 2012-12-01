@echo off

set M3_LIBRARY_HOME="%~d0/"
echo M3_LIBRARY_HOME=%M3_LIBRARY_HOME%
java -version
java -DM3_LIBRARY_HOME=%M3_LIBRARY_HOME% -jar tools\lib\m3server-0.0.1-SNAPSHOT-alldeps.jar

pause
