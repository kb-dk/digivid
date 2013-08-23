#!/bin/bash
cd $(dirname $(readlink -f $0))
. ./../../deck.default

# Collect and quote parameters for passing
QUOTE_ARGS=''
for ARG in "$@"
do
  ARG=$(printf "%q" "$ARG")
  QUOTE_ARGS="${QUOTE_ARGS} $ARG"
done

# Log execution
mkdir -p $HOOKS_LOGDIR
echo "$PWD" >> $HOOKS_LOGDIR/post_postProcess.log
bash -c "echo $QUOTE_ARGS" >> $HOOKS_LOGDIR/post_postProcess.log
echo -e "\n"  >>  $HOOKS_LOGDIR/post_postProcess.log

# Call command
$INGEST_COMMAND "$QUOTE_ARGS"
