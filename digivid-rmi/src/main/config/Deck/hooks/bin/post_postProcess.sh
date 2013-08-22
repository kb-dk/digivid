#!/bin/bash


echo "$PWD" >> ~/post_postProcess.log

while [ ! -z "$1" ]; do
        echo -n "$1 " >>  ~/post_postProcess.log
        shift
done


echo -e "\n\n"  >>  ~/post_postProcess.log