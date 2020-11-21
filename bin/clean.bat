@echo off
echo.
echo [Devteam] Starting mvn clean
echo.

%~d0
cd %~dp0

cd ..
call mvn clean

pause