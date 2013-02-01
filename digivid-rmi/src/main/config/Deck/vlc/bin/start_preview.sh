#!/bin/bash
CONF=${HOME}/Deck/vlc/etc/streamer-IVTV_0.4.conf
. ${CONF}

NO_ARGS=0
ARGS=$*
if [ $# -eq "$NO_ARGS" ] # Script invoked with no arguments
then
        echo "start_preview.sh (-d <device 0|1|2> | -f <file>) -p <port>"
fi

while getopts ":d:p:f:" Option
do
    case $Option in
    d) DEVICE_NUMBER=$OPTARG;;
    p) PORT=$OPTARG;;
    f) FILE=$OPTARG;;
    esac
done
DEVICE=${DEV}${DEVICE_NUMBER}

## Start vlc
ip=`hostname -i`

if [ -n "$DEVICE_NUMBER" ]; then
  ## Configure mpeg1 streaming
  $BINDIR/configure_card.sh -d $DEVICE_NUMBER -a 1
  vpid=`ps ww -C vlc -O pid,args | grep ".*${DEVICE}.*"|cut -c 0-5`
  if [ -n "$vpid" ] ; then
     exit 0
  fi
  vlc -vvv --intf dummy pvr:${DEVICE} --sout '#standard{access=http,mux=ps,dst='$ip':'$PORT'}' &
elif [ -n "$FILE" ]; then
  $BINDIR/stop_preview.sh -p ${PORT}
  vlc -vvv --intf dummy ${FILE} --sout '#standard{access=http,mux=ps,dst='$ip':'$PORT'}' &
fi