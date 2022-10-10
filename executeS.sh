#! /usr/bin/bash
echo "Running executeS.sh"
cd ICE-fibonacci || exit
nohup java -jar server/build/libs/server.jar >> ./data/output_server.txt &