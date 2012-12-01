#!/bin/bash

export M3_LIBRARY_HOME="$(dirname $0)"

echo M3_LIBRARY_HOME=$M3_LIBRARY_HOME

java -version
java -DM3_LIBRARY_HOME=$M3_LIBRARY_HOME -jar $M3_LIBRARY_HOME/tools/lib/m3server-0.0.1-SNAPSHOT-alldeps.jar

