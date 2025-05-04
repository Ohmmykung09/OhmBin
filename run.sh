cd "$(dirname "$0")"

echo "Compiling..."
javac -d bin src/*.java

echo "Running..."
java -cp bin App