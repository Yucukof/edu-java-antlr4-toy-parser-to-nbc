@echo off
echo Setting all files to lowercase
cd %~dp0
for /f "Tokens=*" %f in ('dir /l/b/a-d/s') do (move "%f" "%f")