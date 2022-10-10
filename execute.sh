#! /usr/bin/bash
echo "Running execute.sh"
cd ICE-callback-fibonacci || exit
java -jar client/build/libs/client.jar "$1" >> ./data/output.txt