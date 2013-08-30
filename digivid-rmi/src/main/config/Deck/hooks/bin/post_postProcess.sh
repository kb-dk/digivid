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
date >> $HOOKS_LOGDIR/post_postProcess.log
echo "$INGEST_COMMAND" "$QUOTE_ARGS" >> $HOOKS_LOGDIR/post_postProcess.log
echo >> $HOOKS_LOGDIR/post_postProcess.log

# Call command
$INGEST_COMMAND "$QUOTE_ARGS >> logs/post_postProcess_ingest_command.log 2>&1 < /dev/null &"
