#! C:\cygwin64\bin\bash.exe

list="10.147.19.226
10.147.19.231
10.147.19.36
10.147.19.215
10.147.19.121
10.147.19.245
10.147.19.177
10.147.19.71
10.147.19.161
10.147.19.229
10.147.19.188
10.147.19.233
10.147.19.201
10.147.19.3
10.147.19.113
10.147.19.92
10.147.19.79
10.147.19.204
10.147.19.142
10.147.19.50
10.147.19.227
10.147.19.207
10.147.19.252
10.147.19.63
10.147.19.219
10.147.19.69
10.147.19.228
10.147.19.224
10.147.19.236
10.147.19.212
"
serverIP="10.147.19.36"
nValues="10000 50000 100000 200000 300000"

echo "Running script.sh"

TransferFunction() {
  SSHPASS='swarch' sshpass -e scp -o StrictHostKeyChecking=no swarch@"$1"
}

BuildFunction() {
  SSHPASS='swarch' sshpass -e ssh -o StrictHostKeyChecking=no swarch@"$1" "chmod +x ./ICE-callback-fibonacci/build.sh"
  SSHPASS='swarch' sshpass -e ssh -o StrictHostKeyChecking=no swarch@"$1" "./ICE-callback-fibonacci/build.sh $1"
}
ClientExecuteFunction() {
  SSHPASS='swarch' sshpass -e ssh -o StrictHostKeyChecking=no swarch@"$1" "./ICE-callback-fibonacci/execute.sh $2"
}
ServerExecuteFunction() {
  SSHPASS='swarch' sshpass -e ssh -o StrictHostKeyChecking=no swarch@"$1" "./ICE-callback-fibonacci/executeS.sh"
}
GetServerDataFunction() {
  SSHPASS='swarch' sshpass -e scp -o StrictHostKeyChecking=no swarch@"$1":/home/swarch/ICE-callback-fibonacci/data/output_server.txt ./data/output_server.txt
}

for i in $list; do
  BuildFunction "$i"
done

ServerExecuteFunction $serverIP

for j in $nValues; do
  for k in {1..3}; do
    for i in $list; do
      ClientExecuteFunction "$i" "$j" &
    done
    sleep 30s
  done
done

GetServerDataFunction $serverIP