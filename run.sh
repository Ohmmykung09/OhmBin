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

# Run the application
echo "Running..."
java -cp bin ui.App