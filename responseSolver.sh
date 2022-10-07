#! C:\cygwin64\bin\expect.exe

set timeout 20
set password [lindex $argv 0]

eval spawn swarch@"$arvg 1" "./ICE-callback-fibonacci/request.sh $2"
expect "assword:"   # matches both 'Password' and 'password'
send -- "$password\r"; # -- for passwords starting with -, see https://stackoverflow.com/a/21280372/4575793
interact