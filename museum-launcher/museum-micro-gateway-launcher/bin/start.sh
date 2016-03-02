#!/bin/sh

#export PATH=$PATH:/usr/local/lib
#export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/lib

appName="museum-micro-gateway"
main="com.qingruan.museum.gateway.MuseumMicroGateway"

java -DappName=$appName -Dmain=$main -cp . LauncherBootstrap appLauncher &
