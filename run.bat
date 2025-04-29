@echo off
REM Move to project root
cd /d %~dp0

echo Compiling...
javac -d bin src\*.java

echo Running...
java -cp bin App

pause
