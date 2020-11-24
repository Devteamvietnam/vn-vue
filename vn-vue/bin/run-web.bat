@echo off
echo.
echo [Devteam] Run dev
echo.

%~d0
cd %~dp0

cd ..
npm run dev

pause