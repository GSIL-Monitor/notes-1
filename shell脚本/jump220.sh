#!/bin/bash
temp_file=/opt/emc/scripts/hssh.1 
password='whtemp234'
ip=10.0.0.20
echo 'hello' 
sshpass -p  $password ssh root@$ip  -o StrictHostKeyChecking=no 2>$temp_file
