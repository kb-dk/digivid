#!/bin/bash -e

# /vlcdeck -- script for the vlc stream server
#
# Written by Stephan Drescher <std@statsbiblioteket.dk>.
# This is a start | stop script for the recording daemon which 
# uses the record-v4l2 script
# 
# Possible parameter to expose are e.g.:
# record-v4l2.pl \
#
# --duration 3595 \
# --directory . --output video.mpg \
# --input /dev/video1 --width 720 --height 480 \
# --standard PAL --type mpeg \
# --inputnum 6 --inputname svideo \
# --bitrate 6500000 --peakbitrate 8000000 \
# --save \
# --aspect 0 --audio-bitmask 233 --bframes 3 \
# --dnrmode 0 --dnrspatial 0 \
# --dnrtemporal 0 --dnrtype 0 \
# --framerate 0 --framespergop 12 \
# --gopclosure 1 --capture-last-gop 0 \
# --pulldown 0 --streamtype 2 --debug \
# --tuner-num 0 --output-settings 1 \
# --output-settings-name video.settings --output-settings-type shell
# 
# $ENCODE_DEVICEID      eg. 0|1 or 2
# $WIDTH                eg. 720
# $HEIGHT               eg. 480
# $FORMAT               eg. 1=MPEG-1, 2=MPEG-2
# $BITRATE              eg. 6500000  
# $MAXBITRATE           eg. 8000000  
# $DIRECTORY            eg. ../records
# $FILENAME             eg. video.mpg 
# $LENGTH               eg. 3595


# Run bart as this user ID
DECK_USER=bart
MODULE_NAME=v4l
RECORDERDECK_HOME="/home/${digividUser}/Deck/$MODULE_NAME"

CONF=$RECORDERDECK_HOME/etc/recorder-IVTV_0.4.conf

RECORDER_NAME=recordd
RECORDER_DAEMON="$RECORDERDECK_HOME/bin/$RECORDER_NAME"
DESC1="The Bart Deck Recording Daemon"
RECORDER_DEFAULTS=${HOME}/Deck/deck.default

# Run the daemon here
PWDIR="$RECORDERDECK_HOME/run"


TIMESTAMP=`date "+%y-%m-%d-%H-%M-%S-%s"`

# Source defaults file; edit that file to configure this script.
if [ -e "${RECORDER_DEFAULTS}" ]; then
	. "${RECORDER_DEFAULTS}"
else echo "No default configuration found" 1>&2
fi

configure() {
        CONF_FILE=${1}
        EXIT_VAL=${2}
        if [ -f ${CONF_FILE} ]; then
                . ${CONF_FILE}         
        else
                echo "unable to find ${CONF_FILE}" 2>&1
                exit ${EXIT_VAL}
        fi
}
configure ${CONF} 7

CAPTURELOG="${DEFAULT_CAPTURELOG}_${TIMESTAMP}"
CAPDEBUG="${DEFAULT_CAPDEBUG}"
CAPERROR="${DEFAULT_CAPERROR}"
touch ${CAPTURELOG} #1>&2
touch $CAPDEBUG #1>&2
touch $CAPERROR #1>&2
echo "CAPTURELOG: ${CAPTURELOG}" >> $CAPDEBUG
echo "CAPDEBUG: $CAPDEBUG"     >> $CAPDEBUG
echo "CAPERROR: $CAPERROR"     >> $CAPDEBUG

test -f "${RECORDER_DAEMON}" || exit 0

NO_ARGS=0
ARGS=$*

if [ $# -eq "$NO_ARGS" ] # Script invoked with no arguments
then 
        echo "Usage: `basename $0` options (rtucdiwhabmsflkh)" >> $CAPERROR &
        echo "capturectrl.sh -r <run start|stop> -t <capturetype eg. record> -u <user name> "\
        "-c <clientIP YourIP> -d <device 0|1|2> -i <channelID> -w <width> -h <height> -a <captureFormat> "\
        "-b <bitrate> -m <maxbitrate> -s <storage directory> -f <file name> -l <capture length (Min)> -k <capture size (MB)> -o <original start> -h <help>" 1>&2
        
fi

while getopts ":r:t:u:c:d:i:w:g:a:b:m:s:f:n:l:k:o:h" Option
do
        #echo "$ARGS"  
        #echo $Option  
        case $Option in
        r) echo " option -r [OPTIND=${OPTIND}] \"$OPTARG\"" > /dev/null; RUN=$OPTARG;;
        t) echo " option -t [OPTIND=${OPTIND}] \"$OPTARG\"" > /dev/null; CAPTURETYPE=$OPTARG;;      # TV or TAPE
        u) echo " option -u [OPTIND=${OPTIND}] \"$OPTARG\"" > /dev/null; USERNAME=$OPTARG;;
        c) echo " option -c [OPTIND=${OPTIND}] \"$OPTARG\"" > /dev/null; CLIENTIP=$OPTARG;;         # not used yet make sure a vie
        d) echo " option -d [OPTIND=${OPTIND}] \"$OPTARG\"" > /dev/null; DEVICEID=$OPTARG;;
        i) echo " option -i [OPTIND=${OPTIND}] \"$OPTARG\"" > /dev/null; CHANNELID=$OPTARG;;
        w) echo " option -w [OPTIND=${OPTIND}] \"$OPTARG\"" > /dev/null; WIDTH=$OPTARG;;            # not configurable yet
        g) echo " option -g [OPTIND=${OPTIND}] \"$OPTARG\"" > /dev/null; HEIGHT=$OPTARG;;           # not configurable yet
        a) echo " option -a [OPTIND=${OPTIND}] \"$OPTARG\"" > /dev/null; FORMAT=$OPTARG;;
        b) echo " option -b [OPTIND=${OPTIND}] \"$OPTARG\"" > /dev/null; BITRATE=$OPTARG;;          # not configurable yet
        m) echo " option -m [OPTIND=${OPTIND}] \"$OPTARG\"" > /dev/null; MAXBITRATE=$OPTARG;;       # not configurable yet
        s) echo " option -s [OPTIND=${OPTIND}] \"$OPTARG\"" > /dev/null; DIRECTORY=$OPTARG;;
        f) echo " option -f [OPTIND=${OPTIND}] \"$OPTARG\"" > /dev/null; FILENAME=$OPTARG;;
        n) echo " option -n [OPTIND=${OPTIND}] \"$OPTARG\"" > /dev/null; FILESERIAL=$OPTARG;;
        l) echo " option -l [OPTIND=${OPTIND}] \"$OPTARG\"" > /dev/null; LENGTH=$OPTARG;;
        k) echo " option -k [OPTIND=${OPTIND}] \"$OPTARG\"" > /dev/null; MAXSIZE=$OPTARG;;
        o) echo " option -o [OPTIND=${OPTIND}] \"$OPTARG\"" > /dev/null; OSTART=$OPTARG;;
        h)
            echo "Usage: `basename $0` options (rtucdiwgabmsfnlkoh)"	>> $CAPERROR &
            echo "capturectrl.sh -r <run start|stop> -t <capturetype eg. record> -u <user name> "\
            "-c <clientIP YourIP> -d <device 0|1|2> -i <channelID> -w <width> -h <height> -a <captureFormat> " \
            "-b <bitrate> -m <maxbitrate> -s <storage directory> -f <file name> -l <capture length (Min)> -k <capture size (MB)> -o <original start> -h <help>" >> $CAPDEBUG
            exit 1 ;;
        *) echo "Unimplemented option choosen. $OPTIND $OPTARG" >> $CAPERROR &
        exit 1
        ;;    
        esac
done
shift $(($OPTIND - 1))

if [ -n ${BITRATE} ]; then
    if [ ${FORMAT} = "1" ]; then
        BITRATE=1150000
        MAXBITRATE=1150000
    else
        BITRATE=6500000
        MAXBITRATE=9600000
    fi
fi

# Clean up and set permissions on required files
rm -rf  "$RECORDERDECK_HOME/run/*" "$RECORDERDECK_HOME/logs/recordlog.out" >> $CAPDEBUG
mkfifo -m700 "$RECORDERDECK_HOME"/logs/recordlog.out >> $CAPERROR # 1>&2
                
chown --dereference "$RECORDERDECK_USER" "$RECORDERDECK_HOME/etc" \
               	"$RECORDERDECK_HOME/etc/deck-users" \
                "$RECORDERDECK_HOME/logs" "$RECORDERDECK_HOME/temp" \
                "$RECORDERDECK_HOME/logs/recordlog.out" || true   # 1>&2 

# Look for rotatelogs/rotatelogs2
if [ -x /usr/sbin/rotatelogs ]; then
	ROTATELOGS=/usr/sbin/rotatelogs
else
	ROTATELOGS=/usr/sbin/rotatelogs2
fi

case $DEVICEID in
        0)
            DEVICE=${DEV}0
            PIDFILE="${RECORDER_NAME}_0.pid"
            PIDFILE_PATH="$PWDIR/$PIDFILE"
        ;;
        1)
            DEVICE=${DEV}1
            PIDFILE="${RECORDER_NAME}_1.pid"
            PIDFILE_PATH="$PWDIR/$PIDFILE"
        ;;
        2)
            DEVICE=${DEV}2
            PIDFILE="${RECORDER_NAME}_2.pid"
            PIDFILE_PATH="$PWDIR/$PIDFILE"
        ;;
        3)
            DEVICE=${DEV}3
            PIDFILE="${RECORDER_NAME}_3.pid"
            PIDFILE_PATH="$PWDIR/$PIDFILE"
        ;;
        *) echo "Incorrect or missing capture device parameter please take either 0 | 1 | 2 or 3, You had ${d}" 1>&2
esac



# Consider our options
case "$CAPTURETYPE" in
    tv)
    	echo "Configured TV Options: $CAPTURETYPE $USERNAME $CLIENTIP $DEVICE $CHANNELID $WIDTH $HEIGHT $FORMAT $BITRATE $MAXBITRATE $DIRECTORY $FILENAME $FILESERIAL $LENGTH $MAXSIZE $OSTART" > $CAPDEBUG
        RECORDER_OPTIONS="$CAPTURETYPE $USERNAME $CLIENTIP $DEVICE $CHANNELID $WIDTH $HEIGHT $FORMAT $BITRATE $MAXBITRATE $DIRECTORY $FILENAME $FILESERIAL $LENGTH $MAXSIZE $OSTART"
    	RECNAME="${FILESERIAL}_${USERNAME}_${FILENAME}_${CHANNELID}"
    ;;
    tape)
    	echo "Configured Tape Options: $CAPTURETYPE $USERNAME $CLIENTIP $DEVICE $CHANNELID $WIDTH $HEIGHT mpeg$FORMAT $BITRATE $MAXBITRATE $DIRECTORY $FILENAME $FILESERIAL $LENGTH $MAXSIZE $OSTART" > $CAPDEBUG
    	RECORDER_OPTIONS="$CAPTURETYPE $USERNAME $CLIENTIP $DEVICE $CHANNELID $WIDTH $HEIGHT mpeg$FORMAT $BITRATE $MAXBITRATE $DIRECTORY $FILENAME $FILESERIAL $LENGTH $MAXSIZE $OSTART"
    	RECNAME="${OSTART}_${USERNAME}_${FILENAME}_${CHANNELID}"
    ;;       
    *)
        echo "Capture type not supported, exiting: ${OPTIND}" >> $CAPERROR &
        exit 1
    ;;    
esac

date2stamp () {
	date --utc --date "$1" +%s
}

stamp2date (){
	date --utc --date "1970-01-01 $1 sec" "+%Y-%m-%d %T"
}

dateDiff (){
	case $1 in
		-s) sec=1; shift;;
		-m) sec=60; shift;;
		-h) sec=3600; shift;;
		-d) sec=86400; shift;;
		*) sec=86400;;
	esac
	dte1=$(date2stamp $1)
	dte2=$(date2stamp $2)
	diffSec=$((dte2-dte1))
	if ((diffSec < 0)); then 
		abs=-1; 
	else 
		abs=1; 
	fi
	echo $((diffSec/sec*abs))
}
	
start_capture () {

	MAXSIZE=$((${LENGTH}*${BITRATE}/8000000000))


	if [ "${MAXSIZE}" -eq "0" ]; then
		echo " No limitation defined type limit in MB " >> ${CAPTURELOG}

		KICKOFF=`date --utc +%s`
		capture &
	else
		#megabytes file size limitation  
        FILE="${RECNAME}.mpeg"
                	
		echo "Recording now on ${d}: ${RECNAME}" >> "${CAPTURELOG}"
		KICKOFF=`date --utc +%s`
		capture &
		
		echo "monitor the file ${FILE} not to rise above ${MAXSIZE}" >> ${CAPTURELOG}
		cd ${DIRECTORY} &> /dev/null

		sleep 5
		FILESIZE=0
			
		until [ $FILESIZE -ge $MAXSIZE ]; do
			FILESIZE=(`du -m $FILE`) ;
			FILESIZESHOW=(`stat -c %s $FILE`) ;
			echo "Current value file size: ${FILESIZE} " >> ${CAPTURELOG}
			KICKDOWN=`date --utc +%s`	 > /dev/null
			let "LAPSE = (( $KICKOFF - $KICKDOWN ))"  > /dev/null
			echo "Current value time elapsed ${LAPSE}" >> ${CAPTURELOG}
			sleep 2
		done
		
		#until [ ${LENGTH} -ge ${LAPSE} ]; do
		#	KICKDOWN= `date --utc +%s`	>> "${CAPTURELOG}"
		#	LAPSE=dateDiff -s "${KICKOFF}" "${KICKDOWN}";
		#	echo "Current value time elapsed ${LAPSE}" >> ${CAPTURELOG}
		#	sleep 2
		#done
		
		echo "FileSizeEnd: ${FILESIZE} MB" >> $RECORDLOG
		echo "Add End Date" >> $RECDEBUG
		stop_capture &
			
	fi
}		
		
capture () {			
    	echo -n "Starting ${DESC1}: " >> "${CAPTURELOG}"
    	#dir=`dpkg-statoverride --list $PWDIR`
    	test -z "$PWDIR" || mkdir -p $PWDIR
 	
        #DEBUG
        echo "PIDFILE: $PIDFILE"                >> $CAPDEBUG
        echo "PIDFILE_PATH: $PIDFILE_PATH"      >> $CAPDEBUG
        echo "DAEMON NAME: $RECORDER_NAME"      >> $CAPDEBUG
        echo "DAEMON PATH: $RECORDER_DAEMON"    >> $CAPDEBUG

    	if [ -f $PIDFILE_PATH ]; then

         	RECORDER_PID=`cat $PIDFILE_PATH` & > /dev/null
         	echo "already running: $RECORDER_PID" >> $CAPERROR &
         	exit 2; 
        else 
    		echo "Move to executable daemon directory" >> $CAPDEBUG &
    		/bin/bash -c "$ROTATELOGS \"$RCAPDEBUG_%F.log\" 86400" < "$CAPDEBUG" &

    		# cd $RECORDERDECK_HOME/bin
    		# echo `ls -la`
    		# echo "Start the $RECORDER_NAME with the following Optiones: $RECORDER_OPTIONS" >> $CAPDEBUG # "$RECORDERDECK_HOME/logs/recordlog.out" >> $CAPDEBUG
    		
    		# daemon -d --dbglog=$RECORDERDECK_HOME/logs/recordlog.out ${CMD} ${RECORDER_OPTIONS}
		    # echo "DAEMON EXEC $RECORDER_NAME $RECORDER_OPTIONS" >> $CAPDEBUG &
		    # daemon -d -v -P $PWDIR --dbglog=$RECORDERDECK_HOME/logs/recordlog.out $CMD $RECORDER_OPTIONS
            # -F $PIDFILE_PATH

            echo "${FILESERIAL}"
		    daemon -F $PIDFILE_PATH $RECORDER_DAEMON $RECORDER_OPTIONS & > /dev/null
		
		    test -z $RECORDERDECK_HOME/lock || mkdir -p $RECORDERDECK_HOME/lock & > /dev/null
		    RETVAL=$?
		    echo
		    [  $RETVAL -eq 0 ] && touch $RECORDERDECK_HOME/lock/${RECORDER_NAME}_$DEVICEID
		    return $RETVAL
		    echo " Started ${RECORDER_NAME}." >> ${CAPTURELOG} &
	    fi
}

stop_capture () {

    	echo -n "Stopping ${DESC1}: " >> ${CAPTURELOG}
    	cd ${DIRECTORY} &> /dev/null
    	#echo  " RENAME ${RECNAME} WITH ORIG DATE ${OSTART}" >> ${CAPTURELOG}
    	#find . -name "${RECNAME}*" | sed "s/\(.*\)/mv '${RECNAME}.mpeg' '${RECNAME}_${OSTART}_${LAPSE}.mpeg'/" | (sh) &
    	#echo  `find . -name "${RECNAME}*"` >> ${CAPTURELOG}
    	#echo `ls -la`

    	#sleep 2

    	rm -f $RECORDERDECK_HOME/lock/${RECORDER_NAME}_$DEVICEID & > /dev/null
	    rm -f $PIDFILE_PATH & > /dev/null



    	##Kill the recordd process
    	#dpid=`ps ww -eo pid,args | grep "/usr/bin/recordd.*[[:space:]]$DEVICE[[:space:]]"| cut -c 0-5`
    	dpid=`ps ww  -C recordd -o pid,args | grep ".*bin/recordd.*[[:space:]]$DEVICE[[:space:]]"| cut -c 0-5`
    	echo "Stopping process $dpid" >> ${CAPTURELOG}
    	##dpid=`pidof $RECORDER_NAME` # Find pid of process
    	if [ -z $dpid ]; then
    	    # if process not present return null
    		echo "DAEMON Process $NAME was not running" >> $CAPERROR
    		#exit 2
    	fi
    	kill -9 $dpid 1>&2

        ##Kill the cat process
    	#dpid=`ps ww -eo pid,args | grep "cat[[:space:]]$DEVICE"| cut -c 0-5`
    	dpid=`ps ww -C cat -o pid,args | grep "cat[[:space:]]$DEVICE"| cut -c 0-5`
    	echo "Stopping process $dpid" >> ${CAPTURELOG}
    	##dpid=`pidof $RECORDER_NAME` # Find pid of process
    	if [ -z $dpid ]; then
    	    # if process not present return null
    		echo "DAEMON Process $NAME was not running" >> $CAPERROR
    		#exit 2
    	fi
    	kill -9 $dpid 1>&2

    	for PROC in `ps fax | grep $DEVICE | grep -v grep | cut -c 0-6`; do
               PROCESS="$PROC";
               echo "Terminating recording on card $DEVICEID cat process $PROCESS" >> ${CAPTURELOG}
               #test -n "$PROCESS" || 
               kill -9 "$PROCESS" & > /dev/null
               echo "All dead" >> ${CAPTURELOG}
	done
	return 0
	
}	

# Consider our options
case "${RUN}" in 
	start)
	echo "start recording" > ${CAPTURELOG}
    	capture &
    ;;
	stop)
	    echo "stop recording" > ${CAPTURELOG}
	    stop_capture &
    ;;
    restart)
    	echo "restart recording" > ${CAPTURELOG} 
	    $0 stop
	    exec $0 start
    ;;
*)
 	echo "Usage: `basename $0` options (rtucdiwhabmsfnlkoh)" >> $CAPERROR
	echo "capturectrl.sh -r <run start|stop> -t <capturetype eg. record> -u <user name> "\
	"-c <clientIP YourIP> -d <device 0|1|2> -i <channelID> -w <width> -h <height> -a <captureFormat> "\
	"-b <bitrate> -m <maxbitrate> -s <storage directory> -f <file name> -l <capture length (Min)> -k <capture size (MB)> -o <original start> -h <help>" 1>&2
                       
exit 1
;;
esac

exit 0
