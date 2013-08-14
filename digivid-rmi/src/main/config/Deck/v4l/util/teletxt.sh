#!/bin/bash

/usr/local/bin/ivtvctl -d /dev/video0 -x 1 -b teletext
cat /dev/video0 > ../records/teletxt.mpg
#cat /dev/video16 >../records/teletxt.mpg