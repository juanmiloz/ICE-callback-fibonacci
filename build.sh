#! /bin/bash
echo "Running build.sh"
cd ICE-callback-fibonacci || exit
chmod +x ./gradlew
./gradlew build
chmod +x ./execute.sh