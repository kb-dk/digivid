#!/bin/bash
CONF=${HOME}/Deck/v4l/etc/recorder-IVTV_0.4.conf
. ${CONF}
NO_ARGS=0
ARGS=$*
if [ $# -eq "$NO_ARGS" ] # Script invoked with no arguments
then
        echo "configure_card.sh -d <device 0|1|2> -a <captureFormat 1|2|mpeg1|mpeg2" 1>&2
fi

while getopts ":d:a:" Option
do
   case $Option in
   d) DEVICE=${DEV}${OPTARG};;
   a) FORMAT=${OPTARG};;
   esac
done

${IVCTL} -d ${DEVICE}
sleep .1
${IVCTL} -d ${DEVICE} -p ${DEFAULT_CARD_INPUT}
sleep .1

if [ ${FORMAT} = "1" ] || [ ${FORMAT} = "mpeg1" ]; then
   ${IVCTL} -d ${DEVICE} -u 1 -f width=352,height=288
   sleep .1
   ${IVCTL} -d ${DEVICE} -c stream_type=2,bitrate_mode=1,bitrate=1150000,bitrate_peak=1150000,audio=0xA9,framerate=25,dnr_mode=3,aspect=1
else
   ${IVCTL} -d ${DEVICE} -u 1 -f width=720,height=576
   sleep .1
   ${IVCTL} -d ${DEVICE} -c stream_type=10,audio=0xA9,bitrate_mode=1,bitrate=6500000,bitrate_peak=9600000,framerate=25,dnr_mode=3,aspect=1
fi
