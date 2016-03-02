#/bin/bash
#this script is using for stop webapp

appName="museum-web"
pid=`ps -ef | grep "appName=$appName" | grep -v "grep" | awk '{print $2}'`
if [ "$pid" = "" ]
then 
echo "$appName is not running"
else
kill -9 $pid
echo "kill pid:$pid"
echo "$appName stop success"
fi
