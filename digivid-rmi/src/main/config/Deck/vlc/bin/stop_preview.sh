#!/bin/bash
CONF=${HOME}/Deck/vlc/etc/streamer-IVTV_0.4.conf
. ${CONF}

NO_ARGS=0
ARGS=$*
if [ $# -eq "$NO_ARGS" ] # Script invoked with no arguments
then
        echo "stop_preview.sh  -p <port>"
fi

while getopts ":p:" Option
do
    case $Option in
    p) PORT=$OPTARG;;
    esac
done

vpid=`ps ww -C vlc -O pid,args | grep ".*:${PORT}.*"|cut -c 0-5`
if [ -n "$vpid" ] ; then
     kill -9 $vpid
fi