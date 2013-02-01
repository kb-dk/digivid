#!/bin/bash

# record-v4l2.pl [--channel CHANNEL] [--duration TIME]
#       [--directory DIRECTORY] [--output OUTPUT]
#       [--directory-format FORMAT] [--date-format FORMAT]
#       [--input VIDEO_DEV][--width WIDTH --height HEIGHT]
#       [--standard STANDARD] [--type TYPE]
#       [--inputnum INPUT#] [--inputname INPUT NAME]
#       [--freqtable FREQENCY MAP] [--frequency FREQUENCY]
#       [--bitrate-mode MODE]
#       [--bitrate BITRATE] [--peakbitrate PEAK_BITRATE]
#       [--profile PROFILE] [--list-freqtable] [--list-channels]
#       [--no-record] [--noreset] [--save] [--help] [--version]
#       [--aspect ASPECT] [--audio-bitmask AUDIO-BITMASK] [--bframes BFRAMES]
#       [--dnrmode DNRMODE] [--dnrspatial DNRSPATIAL]
#       [--dnrtemporal DNRTEMPORAL] [--dnrtype DNRTYPE]
#       [--framerate FRAMERATE] [--framespergop FRAMESPERGOP]
#       [--gopclosure GOPCLOSURE] [--capture-last-gop GOP_END]
#       [--pulldown PULLDOWN] [--streamtype STREAMTYPE] [--debug]
#       [--tuner-num TUNERNUM] [--output-settings BOOL]
#       [--output-settings-name FNAME] [--output-settings-type TYPE]
#       [--list-inputs] [--list-standards] [CHANNEL]

# $ENCODE_DEVICEID  	eg. 0|1 or 2
# $WIDTH		eg. 720
# $HEIGHT		eg. 480
# $FORMAT 		eg. 1=MPEG-1, 2=MPEG-2
# $BITRATE 		eg. 6500000  
# $MAXBITRATE   	eg. 8000000
# $DIRECTORY 		eg. ../records
# $FILENAME		eg. video.mpg 
# $LENGTH 		eg. 3595
# $SIZE

# record-v4l2.pl symlink
/home/bytestroop/Deck/v4l/bin/record-v4l2.pl \
 --input /dev/video0 --width 720 --height 576 \
 --pulldown 0 --streamtype 2 --debug \
 --bitrate 3000000 --peakbitrate 6000000 \
 --directory ../../records  \
 --duration 3595 \
 --standard PAL --type mpeg \
 --inputnum 6 --inputname svideo \
 --save \
 --aspect 0 --audio-bitmask 233 --bframes 3 \
 --dnrmode 0 --dnrspatial 0 \
 --dnrtemporal 0 --dnrtype 0 \
 --framerate 0 --framespergop 12 \
 --gopclosure 1 --capture-last-gop 0 \
 --tuner-num 0 --output-settings 1 \
 --output-settings-name video.settings --output-settings-type shell
  
 