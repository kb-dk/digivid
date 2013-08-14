#!/bin/sh -e

# /vlcdeck -- script for the vlc stream server
#
# Written by Stephan Drescher <std@statsbiblioteket.dk>.

# Run bart as this user ID
DECK_USER="bart"
MODULE_NAME="vlc"
STREAMDECK_HOME="/home/${digividUser}/Deck/$MODULE_NAME"
SERVEREXEC=vlc
#Run the Daemon here
PWDIR="$STREAMDECK_HOME/run"

STREAMER_NAME=vlcdeckd
STREAMER_DAEMON="${STREAMDECK_HOME}/bin/${STREAMER_NAME}"
DESC1="The Bart Deck Stream Daemon"
STREAMER_DEFAULTS="/home/${digividUser}/Deck/deck.default"
PIDFILE="$PWDIR/$STREAMER_NAME.pid"

# Source defaults file; edit that file to configure this script.
if [ -e "${STREAMER_DEFAULTS}" ]; then
	. "${STREAMER_DEFAULTS}"
else echo "No default configuration"
fi

test -f "${STREAMER_DAEMON}" || exit 0


NO_ARGS=0
ARGS=$*

if [ $# -eq "$NO_ARGS" ] # Script invoked with no arguments
then 
        echo "Usage: `basename $0` options (rtscdfpnmh)"
        echo "streamctrl -r <run play|stop> -t <streamtype devUDP> -c <clientIP IP> -d <device 0|1|2> -p <port 123|n> -m <media tv|tape>" >&2
        echo "streamctrl -r <run replay|stop> -t <streamtype fileUDP> -c <IP>  -f <fileinputname filename.mpg> -p <port 123|n>" >&2
        echo "streamctrl -r <run play|stop> -t <streamtype devHTTP> -s <serverIP> -d <device 0|1|2> -p <port 8|n> -m <media tv|tape>" >&2
        echo "streamctrl -r <run replay|stop> -t <streamtype fileHTTP> -s <serverIP> -p <port 8|n> -f <filename.mpg>" >&2
        echo "streamctrl -r <run play|stop> -t <streamtype devRTP> -s <serverIP> -c <clientIP> -d <device 0|1|2> -p <port 123|n> -n <streamname> -m <media tv|tape>" >&2
        echo "streamctrl -r <run replay|stop> -t <streamtype fileRTP> -s <serverIP> -c <clientIP> -d <device 0|1|2> -p <port 8|n> -f <filename filename.mpg> -n <streamname> " >&2
  
fi

while getopts ":r:t:s:c:d:f:p:n:m:h" Option
do
        #echo "$ARGS"  
        #echo $Option  
        case $Option in
	r) echo " option -r [OPTIND=${OPTIND}] \"$OPTARG\""; RUN=$OPTARG;;
        t) echo " option -t [OPTIND=${OPTIND}] \"$OPTARG\""; STREAMTYPE=$OPTARG;;
        s) echo " option -s [OPTIND=${OPTIND}] \"$OPTARG\""; HTTP_RTP_SERVERIP=$OPTARG;;
        c) echo " option -c [OPTIND=${OPTIND}] \"$OPTARG\""; UDP_RTP_CLIENTIP=$OPTARG;;
        d) echo " option -d [OPTIND=${OPTIND}] \"$OPTARG\""; ENCODE_DEVICEID=$OPTARG;; 
        p) echo " option -p [OPTIND=${OPTIND}] \"$OPTARG\""; PORT=$OPTARG;;
        f) echo " option -f [OPTIND=${OPTIND}] \"$OPTARG\""; FILEINPUTNAME=$OPTARG;;
        n) echo " option -n [OPTIND=${OPTIND}] \"$OPTARG\""; RTP_STREAMNAME=$OPTARG;;
        m) echo " option -m [OPTIND=${OPTIND}] \"$OPTARG\""; MEDIA=$OPTARG;;
        h)
	echo "Usage: `basename $0` options (rtscdfpnmh)"
        echo "streamctrl -r <run play|stop> -t <streamtype: devUDP> -c <clientIP: IP> -d <device: 0|1|2> -p <port: 123|n> -m <media: tv|tape>" >&2
        echo "streamctrl -r <run replay|stop> -t <streamtype: fileUDP> -c <IP>  -f <fileinputname: eg. filename.mpg> -p <port: 123|n>" >&2
        echo "streamctrl -r <run play|stop> -t <streamtype: devHTTP> -s <serverIP> -d <device: 0|1|2> -p <port 8|n> -m <media: tv|tape>" >&2
        echo "streamctrl -r <run replay|stop> -t <streamtype: fileHTTP> -s <serverIP> -p <port: 8|n> -f <fileinputname: eg.filename.mpg>" >&2
        echo "streamctrl -r <run play|stop> -t <streamtype: devRTP> -s <serverIP> -c <clientIP> -d <device 0|1|2> -p <port: 123|n> -n <streamname: eg vhs.sdp> -m <media: tv|tape>" >&2
        echo "streamctrl -r <run replay|stop> -t <streamtype: fileRTP> -s <serverIP> -c <clientIP> -d <device 0|1|2> -p <port: 8|n> -f <filename: eg. filename.mpg> -n <streamname> " >&2
	exit 1 ;;
        *) echo "Unimplemented option choosen. $OPTION"
        exit 1
        ;;    
        esac
done
shift $(($OPTIND - 1))

# Look for rotatelogs/rotatelogs2
#if [ -x /usr/sbin/rotatelogs ]; then
#	ROTATELOGS=/usr/sbin/rotatelogs
#else
#	ROTATELOGS=/usr/sbin/rotatelogs2
#fi

# Clean up and set permissions on required files
STREAMLOG=$STREAMDECK_HOME/logs/streamlog.out

#rm -rf  "$STREAMDECK_HOME"/run/* # "$STREAMDECK_HOME/logs/streamlog.out"
echo "Create $STREAMDECK_HOME/logs/streamlog.out"
if [ -z $STREAMLOG ]; then
    mkfifo -m700 "$STREAMLOG"
fi

#/bin/bash -c "$ROTATELOGS \"${STREAMDECK_HOME}/logs/stream_%F.log\" 86400" < "${STREAMDECK_HOME}/logs/streamlog.out" &

chown --dereference "$DECK_USER" "$STREAMDECK_HOME/etc" \
               	"$STREAMDECK_HOME/etc/deck-users" \
                "$STREAMDECK_HOME/logs" "$STREAMDECK_HOME/temp" \
                "$STREAMDECK_HOME/logs/streamlog.out" || true    



# Consider our options
case $ENCODE_DEVICEID in
        0)
            PIDFILE="${STREAMER_NAME}_0.pid"
            PIDFILE_PATH="$PWDIR/$PIDFILE"
        ;;
        1)
            PIDFILE="${STREAMER_NAME}_1.pid"
            PIDFILE_PATH="$PWDIR/$PIDFILE"
        ;;
        2)
            PIDFILE="${STREAMER_NAME}_2.pid"
            PIDFILE_PATH="$PWDIR/$PIDFILE"
        ;;
        3)
            PIDFILE="${STREAMER_NAME}_3.pid"
            PIDFILE_PATH="$PWDIR/$PIDFILE"
        ;;
        *) echo "Incorrect or missing capture device parameter please take either 0| 1 | 2 or 3, You had ${d}" 1>&2
esac


case "$STREAMTYPE" in
        devUDP)
        ## !!!!!!!!!!!  change -1 if you have a second card  !!!!!!!!!!!!!!	
        STREAMER_OPTIONS="$STREAMTYPE $UDP_RTP_CLIENTIP $ENCODE_DEVICEID $PORT $MEDIA"
    ;;
        fileUDP)
	STREAMER_OPTIONS="$STREAMTYPE $UDP_RTP_CLIENTIP $FILEINPUTNAME $PORT"
    ;;
        devHTTP)
        ## !!!!!!!!!!!  change -1 if you have a second card  !!!!!!!!!!!!!!	
        STREAMER_OPTIONS="$STREAMTYPE $HTTP_RTP_SERVERIP $ENCODE_DEVICEID $PORT $MEDIA"
        echo "$MEDIA"
    ;;
        fileHTTP)
	 STREAMER_OPTIONS="$STREAMTYPE $HTTP_RTP_SERVERIP $FILEINPUTNAME $PORT"
    ;;
        devRTP)
	STREAMER_OPTIONS="$STREAMTYPE $HTTP_RTP_SERVERIP $UDP_RTP_CLIENTIP $ENCODE_DEVICEID $PORT $RTP_STREAMNAME $MEDIA"
    ;;    
        fileRTP)
        STREAMER_OPTIONS="$STREAMTYPE $HTTP_RTP_SERVERIP $UDP_RTP_CLIENTIP $FILEINPUTNAME $PORT $RTP_STREAMNAME"
    ;;    
        *)
        echo "Stream type not supported, exiting: ${OPPTIND}"
        exit 1
    ;;    
esac

	
startstream () {	
    	echo -n "Starting ${DESC1}: "  >> $STREAMLOG
    	#dir=`dpkg-statoverride --list $PWDIR`
    	test -z "$PWDIR" || mkdir -p $PWDIR
 	
	#DEBUG 	
 	echo "PIDFILE: ${PIDFILE_PATH}"   >> $STREAMLOG
 	echo "DAEMON:  ${STREAMER_DAEMON}"   >> $STREAMLOG
 	echo "NAME:    ${STREAMER_NAME}"  >> $STREAMLOG
 	#CMD="$STREAMER_NAME"
 	#ls  $PIDFILE_PATH >> $STREAMLOG 2>&1
    echo contents of $PWDIR >> $STREAMLOG
    ls -l $PWDIR >> $STREAMLOG

    	if [ -f $PIDFILE_PATH ]; then
         	STREAMER_PID=`cat $PIDFILE_PATH`  & > /dev/null
         	echo "already running: $STREAMER_PID"  >> $STREAMLOG
         	exit 2; 
        else
            echo "Start the $STREAMER_NAME with the following Options: $STREAMER_OPTIONS" >> "${STREAMDECK_HOME}/logs/streamlog.out" 2>&1
    	    echo "DAEMON EXEC: daemon -d -v -F $PIDFILE_PATH $STREAMER_NAME $STREAMER_OPTIONS"  #>> $STREAMLOG
            daemon -d -v -F $PIDFILE_PATH $STREAMER_DAEMON $STREAMER_OPTIONS >> $STREAMLOG &
		
		RETVAL=$?
		echo
		[ $RETVAL -eq 0 ] && touch ${STREAMDECK_HOME}/lock/${STREAMER_NAME}_$ENCODE_DEVICEID
		return $RETVAL
		echo " Started ${STREAMER_NAME}."   >> $STREAMLOG
	fi
    	
}

replaystream () {	
    	echo -n "Starting ${DESC1}: "
    	#dir=`dpkg-statoverride --list $PWDIR`
    	test -z "$PWDIR" || mkdir -p $PWDIR
 	
	#DEBUG 	
 	echo "PIDFILE: ${PIDFILE}"
 	echo "DAEMON:  ${STREAMER_DAEMON}"
 	echo "NAME:    ${STREAMER_NAME}"
 	CMD=$STREAMER_NAME
 	 
    	if [ -f $PIDFILE_PATH ]; then
         	STREAMER_PID=`cat $PIDFILE_PATH`
         	echo "already running: $STREAMER_PID"
         	exit 2; 
        else 
         	
    		#/bin/bash -c "$ROTATELOGS \"$STREAMDECK_HOME/logs/stream_%F.log\" 86400" < "${STREAMDECK_HOME}/logs/streamlog.out" &
    		
    		#echo "Move to executable daemon directory" 
    		#`cd $STREAMDECK_HOME/bin`
    		#echo `ls -la`
    		echo "Start the $STREAMER_NAME with the following Optiones: $STREAMER_OPTIONS" >> "${STREAMDECK_HOME}/logs/streamlog.out" 2>&1
    		
    		echo "DAEMON EXEC: $STREAMER_NAME $STREAMER_OPTIONS"  
    		daemon -d -v -F $PIDFILE_PATH $STREAMER_DAEMON $STREAMER_OPTIONS &
		
		RETVAL=$?
		echo
		[  $RETVAL -eq 0 ] && touch ${STREAMDECK_HOME}/lock/${STREAMER_NAME}_$ENCODE_DEVICEID
		return $RETVAL
		echo " Started ${STREAMER_NAME}."
	fi
    	
}

stopstream () {
    echo -n "Stopping ${DESC1}: " # >>"$STREAMDECK_HOME/logs/streamlog.out" 2>&1
    rm -f ${STREAMDECK_HOME}/lock/${STREAMER_NAME}_$ENCODE_DEVICEID

    ## We always kill by port number since we can be reasonably sure that only one
    ## streamserver is operating per port

    spid=`ps ww -eo pid,args | grep "vlc[[:space:]].*:$PORT"| cut -c 0-5`

    if [ -z $spid ]; then
    	# if process not present return null
    	echo "StreamServer Process $spid was not running" >>"${STREAMDECK_HOME}/logs/streamlog.out" 2>&1
    	exit 2
    fi
    kill -9 $spid
    #dpid=`pidof $STREAMER_NAME` # Find pid of process
    #if [ -z $dpid ]; then
    #	# if process not present return null
    #	echo "DAEMON Process $NAME was not running" >>"${STREAMDECK_HOME}/logs/streamlog.out" 2>&1
    #	exit 2
    #fi
    #kill -9 $dpid

	echo "stopped" >>"$STREAMDECK_HOME/logs/streamlog.out" 2>&1
	echo
	echo "${STREAMER_NAME}."
	return 0
}	

# Consider our options
case "$RUN" in 
	play) 
    	startstream
    ;;
	stop)
	stopstream
    ;;
	replay)
	replaystream
    ;;
    	restart)
	$0 stop
	exec $0 start
    ;;
*)
 	echo "Usage: `basename $0` options (rtscdfpnmh)"
        echo "streamctrl -r <run play|stop> -t <streamtype devUDP> -c <clientIP IP> -d <device 0|1|2> -p <port 123|n> -m <media tv|tape>" >&2
        echo "streamctrl -r <run replay|stop> -t <streamtype fileUDP> -c <IP>  -f <fileinputname filename.mpg> -p <port 123|n>" >&2
        echo "streamctrl -r <run play|stop> -t <streamtype devHTTP> -s <serverIP> -d <device 0|1|2> -p <port 8|n> -m <media tv|tape>" >&2
        echo "streamctrl -r <run replay|stop> -t <streamtype fileHTTP> -s <serverIP> -p <port 8|n> -f <filename.mpg>" >&2
        echo "streamctrl -r <run play|stop> -t <streamtype devRTP> -s <serverIP> -c <clientIP> -d <device 0|1|2> -p <port 123|n> -n <streamname> -m <media tv|tape>" >&2
        echo "streamctrl -r <run replay|stop> -t <streamtype fileRTP> -s <serverIP> -c <clientIP> -d <device 0|1|2> -p <port 8|n> -f <filename filename.mpg> -n <streamname> " >&2

exit 1
;;
esac

exit 0
