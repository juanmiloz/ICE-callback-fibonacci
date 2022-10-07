#! C:\cygwin64\bin\bash.exe

/usr/bin/expect <<
  spawn ssh swarch@"$1" "./ICE-callback-fibonacci/request.sh $2"
  expect "assword"
  send -- "$PWD\r"
  expect eof