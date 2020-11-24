@echo off
echo.
echo [Devteam] Package build Webwar/jar
echo.

%~d0
cd %~dp0

cd ..
call mvn clean package -Dmaven.test.skip=true

pause