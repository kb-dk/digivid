#!/bin/bash
# Just a stupid checker tool

RDIR=/home/mythtv/records

echo "Start Replay Recordings";
if [ -e $1 ];
then
	echo "please tell me which file you want to listen to!"
else
	aplay -f dat  ${RDIR}/$1.rpmc
fi