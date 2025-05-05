@echo off
REM Move to project root
cd /d %~dp0

REM Ensure bin directory exists
if not exist bin mkdir bin

echo Compiling...
javac -d bin -sourcepath src src\ui\App.java
if errorlevel 1 (
    echo Compilation failed. Exiting...
    pause
    exit /b
)

echo Running...
java -cp bin ui.App

pause