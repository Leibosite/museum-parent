#!/bin/sh

#export PATH=$PATH:/usr/local/lib
#export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/lib

appName="museum-agent"
main="com.qingruan.museum.agent.MuseumAgent"

java -DappName=$appName -Dmain=$main -cp . LauncherBootstrap appLauncher &
