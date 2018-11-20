#£¡bin/bash
if [ $# -lt 1 ]
then
  echo "not found jar name!";
  exit 1;
fi

clearLog(){
	if [ -f /opt/emc/emc-$1.log ]
	then 
		if [ `ls -l /opt/emc/emc-$1.log | awk '{print $5}'` -gt $((1*1024*1024*1024)) ]
		then
			echo -ne $(date +%Y-%m-%d\ %H:%M:%S);
			find /opt/emc/ -name emc-$1.log -type f -print -exec truncate -s 0 {} \;
			echo '-------------------------------clear log successfully-------------------------------------';
			return;
		fi
	fi
}

until [ $# -eq 0 ]
do
	case  $1 in
		ma | manager)
			clearLog manager;;

		g | gateway)
			clearLog gateway;;

		mq | mqtt)
			clearLog mqtt;;			
		t | tcp) 
			clearLog tcp;;
			
		u | udp)
			clearLog udp;;

		c | coap) 
 			clearLog coap;;

		ms |msgcore)
			clearLog msgcore;;
                       
		-a) /opt/emc/scripts/clearlog.sh ma g mq t u c ms;;
			
		*) echo 'Unknown jar name ' $1;;
	esac;
	shift 1
done