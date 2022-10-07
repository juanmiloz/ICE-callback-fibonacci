#! /bin/bash
echo "Running request.sh"
cd ICE-callback-fibonacci || exit
nohup java -jar ./client/build/libs/client.jar "$1"
