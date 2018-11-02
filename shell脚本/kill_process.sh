#!/bin/bash
if [ $# -lt 1 ]
then
  echo "[ERROR]:not find parameter for process_name"
  exit 1
fi

echo "start kill $1 ..."
ps -ef|grep $1 | grep -v grep | awk '{print " " $2}' > /opt/emc/script/emc_process.txt
while read line
do
  echo "pid = $line"
  kill -9 $line
done < /opt/emc/script/emc_process.txt
