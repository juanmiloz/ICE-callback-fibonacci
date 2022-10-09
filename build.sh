#! /bin/bash
echo "Running build.sh"
cd ICE-callback-fibonacci || exit
TARGET_KEY="Callback.Endpoints"
CONFIG_FILE="client/src/main/resources/config.client"
REPLACEMENT_VALUE="default -p 9090 -h $1"
sed -c -i "s/\($TARGET_KEY *= *\).*/\1$REPLACEMENT_VALUE/" $CONFIG_FILE
chmod +x ./gradlew
./gradlew build
chmod +x ./execute.sh
chmod +x ./executeS.sh
