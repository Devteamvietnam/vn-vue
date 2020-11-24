@echo off
echo.
echo [Devteam]  Build
echo.

%~d0
cd %~dp0

cd ..
npm run build:prod

pause