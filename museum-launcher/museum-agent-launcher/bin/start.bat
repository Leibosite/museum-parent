cd /d %~dp0
set "CURRENT_DIR=%cd%"

cd ..
set "LAUNCHER_HOME=%cd%"
cd "%CURRENT_DIR%"

set "appName=museum-engine"
set "main=com.qingruan.museum.engine.MuseumEngine"

start java -DappName=%appName% -Dmain=%main% -cp . LauncherBootstrap appLauncher &
