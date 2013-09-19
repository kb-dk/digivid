#!/bin/bash
sed -i -e 's/encoderIP/encoderName/g;s/130.225.25.167/digibox01/g;s/130.225.24.221/digibox02/g;s/172.18.243.243/digibox03/g;s/172.18.198.122/digibox04/g;s/172.18.238.237/digibox05/g' *.comments
rename 130.225.25.167 digibox01 *
rename 130.225.24.221 digibox02 *
rename 172.18.243.243 digibox03 *
rename 172.18.198.122 digibox04 *
rename 172.18.238.237 digibox05 *
