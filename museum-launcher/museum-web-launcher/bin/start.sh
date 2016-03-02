#!/bin/sh

#export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/lib

appName="museum-web"
main="com.qingruan.framework.webserver.TomcatServer"

java -DappName=$appName -Dmain=$main -cp . LauncherBootstrap appLauncher &
