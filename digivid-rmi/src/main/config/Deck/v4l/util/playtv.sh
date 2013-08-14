#!/bin/bash
# just a stupid checker tool

RDIR=/home/mythtv/records

echo "Start Replay Recordings"
if  [ -e $1 ] ;
then
	echo -n "Please tell me which file you want to watch!"
else
	/usr/bin/mplayer -vo x11 -ao alsa ${RDIR}/$1.mpeg
fi