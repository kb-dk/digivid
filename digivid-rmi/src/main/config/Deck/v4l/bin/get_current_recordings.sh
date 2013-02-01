#!/bin/bash

for catpid in `ps -C cat -o pid`
do
basename `readlink /proc/${catpid}/fd/1` 2>/dev/null
done
