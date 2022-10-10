#! /usr/bin/bash
echo "Running execute.sh"
cd ICE-fibonacci || exit
java -jar client/build/libs/client.jar "$1" >> ./data/output.txt