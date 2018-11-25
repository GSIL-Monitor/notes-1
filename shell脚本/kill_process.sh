#!/bin/bash
if [ $# -lt 1 ]
then
  echo "[ERROR]:not find parameter for process_name"
  exit 1
fi

kill_server(){
	echo start kill server $1 ...
	ps -ef|grep $1 | grep -v grep | awk '{print $2}' > /opt/emc/scripts/process.txt
	while read line
	do
		echo "kill exist process pid = $line"
		kill -9 $line
	done < /opt/emc/scripts/process.txt
	echo kill server $1 end	...

}

until [ $# -eq 0 ]
do
	case $1 in
		ma | manager)
			kill_server emc-manager.jar;;

		r | receive)
			kill_server emc-receive.jar;;

		mq | mqtt)
			kill_server emc-mqtt.jar;;
			
		t | tcp) 
			kill_server emc-tcp.jar;;
			
		u | udp)
			kill_server emc-udp.jar;;

		c | coap) 
 			kill_server emc-coap.jar;;
                       
		-a) /opt/emc/scripts/kill-process.sh ma r t u c mq;;
			
		*) echo 'Unknown jar name ' $1;;

	esac;
	shift 1
done
