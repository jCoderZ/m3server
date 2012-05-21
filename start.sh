#!/bin/bash

cd ../m3server-assembly/target
unzip m3server-assembly-1.0.0.SNAPSHOT-bin.zip
cd m3server-assembly-1.0.0.SNAPSHOT
./m3server.sh debug

