#!/bin/bash
CONF=${HOME}/Deck/v4l/etc/recorder-IVTV_0.4.conf
. ${CONF}

NO_ARGS=0
ARGS=$*
if [ $# -eq "$NO_ARGS" ] # Script invoked with no arguments
then
        echo "start_recording.sh -d <device 0|1|2> -i <channelID> -a <captureFormat 1|2> -f <file name> -l <capture length (Min)> -o <original start> " 1>&2
fi

while getopts ":d:i:a:f:l:o:" Option
do
    case $Option in 
    d) DEVICE_NUMBER=$OPTARG;;
    i) CHANNEL_ID=$OPTARG;;
    a) CAPTURE_FORMAT=$OPTARG;;
    f) FILE_NAME_COMPONENT=$OPTARG;;
    l) CAPTURE_LENGTH=$OPTARG;;
    o) START_TIME=$OPTARG;;
    *) echo "Unimplemented option $OPTIND $OPTARG"
       exit 2
       ;;
    esac
done

echo -e "Starting recording with:\ndevice=${DEVICE_NUMBER}\nchannel=${CHANNEL_ID}\nformat=mpeg${CAPTURE_FORMAT}"\
        "\nname=${FILE_NAME_COMPONENT}\nlength=${CAPTURE_LENGTH}minutes\norignal start time=${START_TIME}"
DEVICE=${DEV}${DEVICE_NUMBER}

##check for already recording
dpid=`ps ww -C cat -o pid,args | grep "cat[[:space:]]$DEVICE"| cut -c 0-5`
if [ -n "$dpid" ]; then
  echo "Already recording on ${DEVICE}: process $dpid"
  exit 2
fi

##configure card
    ${BINDIR}/configure_card.sh -d ${DEVICE_NUMBER} -a ${CAPTURE_FORMAT}

##generate file name for output and log
    NAME_PREFIX=${START_TIME}_${USER}_${FILE_NAME_COMPONENT}_${CHANNEL_ID}_mpeg${CAPTURE_FORMAT}
    RECNAME=${NAME_PREFIX}.mpeg
    RECFILE=${RDIR}/${RECNAME}
    LOGNAME=${NAME_PREFIX}.log
    LOGFILE=${RDIR}/${LOGNAME}

##Write log file
    echo "Record File: ${RECFILE}"  >> ${LOGFILE}
    echo "Secondary Audio Program: " `${IVCTL} -d ${DEVICE} -Z`  >> ${LOGFILE}
    echo "Current Codec Params: " `${IVCTL} -d ${DEVICE} -C`  >> ${LOGFILE}
    echo "Current Control Params: " `${IVCTL} -d ${DEVICE} -Y`  >> ${LOGFILE}
    echo "Video Input: "  `${IVCTL} -d ${DEVICE} -P` >> ${LOGFILE}
    echo "Video Output: " `${IVCTL} -d ${DEVICE} -L` >> ${LOGFILE}
    echo "Video Signal: " `${IVCTL} -d ${DEVICE} -I` >> ${LOGFILE}
    echo "Video Standard: " `${IVCTL} -d ${DEVICE} -U` >> ${LOGFILE}
    echo "VBI Mode: " `${IVCTL} -d ${DEVICE} -B`  >> ${LOGFILE}
    echo "VBI Passthrough: " `${IVCTL} -d ${DEVICE} -W`  >> ${LOGFILE}
    echo "VBI Embeded: " `${IVCTL} -d ${DEVICE} -X` >> ${LOGFILE}
    echo "Driver Version: " `${IVCTL} -V`  >> ${LOGFILE}

echo "Record File: ${RECFILE}"

##start the capturing

cat $DEVICE > ${RECFILE} &
catpid=$!

##time the process

rectime=$((${CAPTURE_LENGTH}*60))

sleeptime=2
starttime=`date +%s`
nowtime=${starttime}
runtime=$((${nowtime}-${starttime}))
until [ ${runtime} -ge ${rectime} ] ; do
	    sleep ${sleeptime}
	    nowtime=`date +%s`
	    runtime=$((${nowtime}-${starttime}))
done

kill -9 $catpid

exit 0