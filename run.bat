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

REM Copy assets folder to bin
echo Copying assets...
xcopy /E /I assets bin\assets

echo Packaging into JAR...
jar cfe OhmBin.jar ui.App -C bin .
if errorlevel 1 (
    echo JAR packaging failed. Exiting...
    pause
    exit /b
)

echo Running JAR...
java -jar OhmBin.jar

pause