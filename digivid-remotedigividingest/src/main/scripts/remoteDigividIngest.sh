#!/bin/bash

source $HOME/services/conf/vhs-ingest/remoteDigividIngest.conf

# Get parameters
ARGS=$(getopt -l "fileDir:,filename:,comments:,quality:,encoderName:,startDate:,endDate:,channelLabel:,channelID:,captureFormat:,username:" -n "remoteDigividIngest.sh" -- "$@");

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
      shift;
      ;;
    --filename)
      shift;
      FILENAME="$1"
      shift;
      ;;
    --comments)
      shift;
      VHSLABEL="$1"
      shift;
      ;;
    --quality)
      shift;
      QUALITY="$1"
      shift;
      ;;
    --encoderName)
      shift;
      ENCODER="$1"
      shift;
      ;;
    --startDate)
      shift;
      STARTTIME=$(date -d "@$(($1/1000))" +"%Y-%M-%dT%H:%m:%S")
      shift;
      if [ $? -ne 0 ];
      then
        exit 1
      fi
      ;;
    --endDate)
      shift;
      STOPTIME=$(date -d "@$(($1/1000))" +"%Y-%M-%dT%H:%m:%S")
      shift;
      if [ $? -ne 0 ];
      then
        exit 1
      fi
      ;;
    --channelLabel)
      shift;
      shift;
      ;;
    --channelID)
      shift;
      shift;
      ;;
    --captureFormat)
      shift;
      shift;
      ;;
    --username)
      shift;
      shift;
      ;;
    --)
      shift;
      shift;
      break;
      ;;
    *)
      echo "Unknown parameter $1" > /dev/stderr
      exit 1;
      ;;
  esac
done

cd $(dirname $(readlink -f $0))

./ingestVHSFile.sh -inputvalue vhslabel "$VHSLABEL" -inputvalue starttime "$STARTTIME" -inputvalue stoptime "$STOPTIME" -inputvalue mpgfile "$ENCODER/$FILENAME" -inputvalue quality "$QUALITY" -inputvalue domsUser "$DOMSUSER" -inputvalue domsPass "$DOMSPASS" 
