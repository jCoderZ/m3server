#!/bin/bash

export M3_LIBRARY_HOME="$(dirname $0)"

echo M3_LIBRARY_HOME=$M3_LIBRARY_HOME

java -version
java -jar $M3_LIBRARY_HOME/tools/lib/m3server-0.0.1-SNAPSHOT-jar-with-dependencies.jar -DM3_LIBRARY_HOME=$M3_LIBRARY_HOME

