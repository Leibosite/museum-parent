set "appName=pcrf-admin"
wmic process where "((CommandLine LIKE '%%appName=%appName%%%') AND NOT(CommandLine LIKE '%_wmic_%' ))" call terminate 

@echo off
echo "%appName% stop success."
set /p DUMMY=Hit ENTER to continue...

