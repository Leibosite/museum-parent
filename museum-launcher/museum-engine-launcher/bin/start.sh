#!/bin/sh

#export PATH=$PATH:/usr/local/lib
#export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/lib

appName="museum-engine"
main="com.qingruan.museum.engine.MuseumEngine"

java -DappName=$appName -Dmain=$main -cp . LauncherBootstrap appLauncher &
