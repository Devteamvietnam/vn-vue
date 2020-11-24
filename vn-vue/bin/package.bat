@echo off
echo.
echo [Devteam] install node_modules packages
echo.

%~d0
cd %~dp0

cd ..
npm install --registry=https://registry.npmjs.org

pause