#!/bin/bash

# Get parameters
ARGS=$(getopt -l "fileDir:,filename:,comments:,quality:,encoderIP:,startDate:,endDate:,channelLabel:,channelID:,captureFormat:,username:" -n "remoteDigividIngest.sh" -- "$@");

#Bad arguments
if [ $? -ne 0 ];
then
  exit 1
fi

eval set -- "$ARGS";

while true; do
  case "$1" in
    --fileDir)
      shift;
      ;;
    --filename)
      shift;
      FILENAME='$1'
      ;;
    --comments)
      shift;
      VHSLABEL='$1'
      ;;
    --quality)
      shift;
      QUALITY='$1'
      ;;
    --encoderIP)
      shift;
      ENCODER='$1'
      ;;
    --startDate)
      shift;
      STARTTIME=$(date -d "@$(($1/1000))" +"%Y-%M-%dT%H:%m:%S")
      if [ $? -ne 0 ];
      then
        exit 1
      fi
      ;;
    --endDate)
      shift;
      STOPTIME=$(date -d "@$(($1/1000))" +"%Y-%M-%dT%H:%m:%S")
      if [ $? -ne 0 ];
      then
        exit 1
      fi
      ;;
    --channelLabel)
      shift;
      ;;
    --channelID)
      shift;
      ;;
    --captureFormat)
      shift;
      ;;
    --username)
      shift;
      ;;
    --)
      shift;
      break;
      ;;
  esac
done

cd $(dirname $(readlink -f $0))
source remoteDigividIngestSetEnv.sh
cd $VHSINGEST_HOME
./bin/vhsFileIngest.sh -inputvalue vhslabel "$VHSLABEL" -inputvalue starttime "$STARTTIME" -inputvalue stoptime "$STOPTIME" -inputvalue mpgfile "$ENCODER/$FILENAME"  -inputvalue domsUser "$DOMSUSER" -inputvalue domsPass "$DOMSPASS"