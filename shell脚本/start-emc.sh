#!/bin/sh
if [ $# -lt 1 ]
then
  echo "not found jar name!"
  exit 1
fi


restart_server(){
	echo start server $1 ...
	ps -ef|grep $1 | grep -v grep | awk '{print $2}' > /opt/emc/scripts/process.txt
	while read line
	do
		echo "kill exist process pid = $line"
		kill -9 $line
	done < /opt/emc/scripts/process.txt

	nohup java -jar /opt/emc/$1.jar --spring.profiles.active=test > /opt/emc/$1.log 2>&1 &
	if [ $1 = 'emc-manager' ]
	then sleep 20s	
	fi
	echo Start server $1 end
}

ifManagerStarted(){
	echo check emc-manager ifStarted...
	sleep 2s
	ps -ef|grep emc-manager | grep -v grep | awk '{print $2}' > /opt/emc/scripts/process.txt
	read line < /opt/emc/scripts/process.txt
		if [[ -n '$line' ]] && [[ $line -gt 0 ]]
			then echo 'emc-manager service check ok...';return 1;
			else echo 'try again ...' ;ifManagerStarted;
		fi
}

until [ $# -eq 0 ]
do
	case  $1 in
		ma | manager)
			restart_server emc-manager;;

		r | receive)
			restart_server emc-receive;;

		mq | mqtt)
			ifManagerStarted;
			restart_server emc-mqtt;;			
		t | tcp) 
			ifManagerStarted;
			restart_server emc-tcp;;
			
		u | udp)
			ifManagerStarted;
			restart_server emc-udp;;

		c | coap) 
 			restart_server emc-coap;;
                       
		-a) /opt/emc/scripts/start-emc.sh ma r t u c mq;;
			
		*) echo 'Unknown jar name ' $1;;

	esac;
	shift 1
done
