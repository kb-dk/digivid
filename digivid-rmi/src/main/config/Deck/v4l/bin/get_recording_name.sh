#!/bin/bash
CONF=${HOME}/Deck/v4l/etc/recorder-IVTV_0.4.conf
. ${CONF}

NO_ARGS=0
ARGS=$*
if [ $# -eq "$NO_ARGS" ] # Script invoked with no arguments
then
        echo "get_recording_name.sh -d <device 0|1|2> -i <channelID> -a <captureFormat 1|2> -f <file name> -l <capture length (Min)> -o <original start> " 1>&2
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

DEVICE=${DEV}${DEVICE_NUMBER}


##generate file name for output and log
    NAME_PREFIX=${START_TIME}_${USER}_${FILE_NAME_COMPONENT}_${CHANNEL_ID}_mpeg${CAPTURE_FORMAT}
    RECNAME=${NAME_PREFIX}.mpeg
    RECFILE=${RDIR}/${RECNAME}
    LOGNAME=${NAME_PREFIX}.log
    LOGFILE=${RDIR}/${LOGNAME}

## Output final filename for the other scripts to process
    echo "Record File: ${RECFILE}"

