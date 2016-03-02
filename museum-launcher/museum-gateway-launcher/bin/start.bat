cd /d %~dp0
set "CURRENT_DIR=%cd%"

cd ..
set "LAUNCHER_HOME=%cd%"
cd "%CURRENT_DIR%"

set "PATH=%PATH%;%LAUNCHER_HOME%\etc\zeromq-4.0.3-x64"

set "appName=pcrf-engine"
set "main=com.baoyun.pcrf.engine.PcrfEngine"

start java -DappName=%appName% -Dmain=%main% -cp . LauncherBootstrap appLauncher &