#!/bin/bash
# this requires for execution to be on a Unix box with java J2SE installed
# one network interface (eth0)
SCRIPT_PATH=$(dirname $(readlink -f $0))
SERVER_RMI_HOME=${SCRIPT_PATH}/..
#CLASSERVER_DEFAULTS="/etc/default/deck.default"
TIMESTAMP=`date "+%y-%m-%d_%H-%M-%S-%s"`
HOSTNAME=`hostname -f`

echo "-----Starting Class Server------"

#if [ -e "${CLASSERVER_DEFAULTS}" ]; then
#	. "${CLASSERVER_DEFAULTS}"
#else echo "No default configuration"
#fi
CONF=$SERVER_RMI_HOME/etc/classerver.conf

rmi_configure() {
	CONF_FILE=${1}
	EXIT_VAL=${2}
	if [ -f ${CONF_FILE} ]; then
		. ${CONF_FILE}
	else
		echo "unable to find ${CONF_FILE}"
		exit ${EXIT_VAL}
	fi
}

rmi_configure ${CONF} 7

mkdir -p ${SERVER_RMI_HOME}/logs

CLASSLOG="${DEFAULT_CLASSLOG}_${TIMESTAMP}"
RMIDEBUG="${DEFAULT_RMIDEBUG}"
RMIERROR="${DEFAULT_RMIERROR}"
touch $CLASSLOG
touch $RMIDEBUG
touch $RMIERROR

debug () {
    ## admin params
    echo "ADMINIP $ADMINIP"
    echo "ADMINPORT $ADMINPORT"
    echo "PROJECTROOT $PROJECTROOT"

    ## server params
    echo "HOSTNAME: ${HOSTNAME}"
    echo "NETINTERFACE: $NETINTERFACE"
    echo "JAVA_HOME: $JAVA_HOME"
    echo "POLICY_PATH: $POLICY_PATH"
    echo "CLASSPATH: $CLASSPATH"

    ## loggin params
    echo "TIMESTAMP $TIMESTAMP"
    echo "DEFAULT_CLASSLOG $DEFAULT_CLASSLOG"
    echo "DEFAULT_RMIDEBUG $DEFAULT_RMIDEBUG"
    echo "DEFAULT_RMIERROR $DEFAULT_RMIERROR"
    echo "CLASSSERVER LOG: $CLASSLOG"
    echo "RMI DEBUG: $RMIDEBUG"
    echo "RMI ERROR: $RMIERROR"
}

NO_ARGS=0
ARGS=$*

if [ $# -eq "$NO_ARGS" ] # Script invoked with no arguments
then
        echo "Usage: `basename $0` <start|stop|restart|debug)" >&2
fi

getJPID () {
   CMD="java"
   for PID in `ps -C $CMD -o pid=`; do
    	if [ -n "${PID}" ]; then
    	#echo "ALL PIDS ${PID}"
    	RMIPID=`ps -p ${PID} -f | grep 'rmi'`
    	# echo "this pid ${RMIPID}"
    		if [ -n "${RMIPID}" ] ; then 
    			echo "RMI PID $PID" >> $RMIDEBUG
    			return 1;
    		fi
    	fi
    done
}


startengine() {

    ${JAVA_HOME}/bin/java -DSun.rmi.loader.logLevel=${DEBUG} \
    -Djava.security.policy=$POLICY_PATH -Djava.rmi.server.useCodebaseOnly=true \
    -classpath $CLASSPATH \
    -Djava.rmi.server.codebase=http://${ADMINIP}:${ADMINPORT}/${PROJECTROOT}/rmi/client.jar \
    -Djava.rmi.server.hostname=${HOSTNAME} dk.statsbiblioteket.deck.server.engine.ComputeEngine > $CLASSLOG &

    sleep 1
       
    getJPID; #> /dev/null        
    
    if [ -n ${PID} ]; then 
    	echo "class server up PID ${PID}"
    else 
    	echo "start class server failed" >> $RMIERROR && >&2
    fi	
}

stopengine() {

    getJPID; #> /dev/null
    echo "kill java PID: $PID"
    kill -s HUP $PID
    
    CMD=`basename $0`
    PID=`ps -C $CMD -o pid=`
    echo "kill shell PID: $PID"
    kill -s HUP $PID
    
    echo "class server down"  >> $RMIERROR && >&2
}

# Consider our options
case "${1}" in
	start)
    	startengine &
    ;;
	stop)
	    stopengine
    ;;
    debug)
	    debug &
    ;;
    restart)
	    $0 stop
	    exec $0 start
    ;;
    *)
 	    echo "Usage: `basename $0` <start|stop|restart|debug)" >&2
        exit 1
    ;;
esac

exit 0