#!/bin/bash
CONF=${HOME}/Deck/v4l/etc/recorder-IVTV_0.4.conf
. ${CONF}
NO_ARGS=0
ARGS=$*
if [ $# -eq "$NO_ARGS" ] # Script invoked with no arguments
then
        echo "stop_recording.sh -d <device 0|1|2> " 1>&2
fi

while getopts ":d:" Option
do
    case $Option in
    d) DEVICE_NUMBER=$OPTARG;;
    esac
done

if [ -z "$DEVICE_NUMBER" ] ; then
  echo "No device number specified"
  exit 2
fi

#First find the start_recording process

rpid=`ps ww  -C start_recording.sh -o pid,args | grep ".*start_recording.sh.*-d[[:space:]]${DEVICE_NUMBER}[[:space:]].*"| cut -c 1-5`

#Now find the cat process
cpid=`ps ww  -C cat -o pid,args | grep "cat[[:space:]]${DEV}${DEVICE_NUMBER}"| cut -c 1-5`

echo Killing $rpid $cpid
[ ! -z "$rpid" ] &&  echo $rpid | xargs kill -9
[ ! -z "$cpid" ] && echo $cpid | xargs kill -9

exit 0