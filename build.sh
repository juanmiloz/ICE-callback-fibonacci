#! /usr/bin/bash
echo "Running build.sh"
cd ICE-fibonacci || exit
chmod +x ./gradlew
./gradlew build
chmod +x ./execute.sh
chmod +x ./executeS.sh
