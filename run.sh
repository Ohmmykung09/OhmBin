#!/bin/bash

# Move to the project root
cd "$(dirname "$0")"

# Ensure bin directory exists
if [ ! -d "bin" ]; then
  echo "Creating bin directory..."
  mkdir bin
fi

# Compile the Java files
echo "Compiling..."
javac -d bin -sourcepath src src/ui/App.java
if [ $? -ne 0 ]; then
  echo "Compilation failed. Exiting..."
  exit 1
fi

# Copy assets folder to bin
echo "Copying assets..."
cp -r assets bin/

# Package into a .jar file
echo "Packaging into JAR..."
jar cfe OhmBin.jar ui.App -C bin .
if [ $? -ne 0 ]; then
  echo "JAR packaging failed. Exiting..."
  exit 1
fi

# Run the application
echo "Running JAR..."
java -jar OhmBin.jar