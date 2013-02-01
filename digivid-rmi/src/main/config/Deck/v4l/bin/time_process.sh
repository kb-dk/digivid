#!/bin/bash

rectime=$1  #seconds
cpid=$2

sleeptime=2
starttime=`date +%s`
nowtime=${starttime}
runtime=$((${nowtime}-${starttime}))
until [ ${runtime} -ge ${rectime} ] ; do
	    sleep ${sleeptime}
	    nowtime=`date +%s`
	    runtime=$((${nowtime}-${starttime}))
done

kill -9 $cpid