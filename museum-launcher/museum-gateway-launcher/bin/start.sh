#!/bin/sh

#export PATH=$PATH:/usr/local/lib
#export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/lib

appName="museum-gateway"
main="com.qingruan.museum.gateway.MuseumGateway"

java -DappName=$appName -Dmain=$main -cp . LauncherBootstrap appLauncher &
