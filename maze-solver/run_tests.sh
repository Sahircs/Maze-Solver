find . -name "*.class" -type f -delete

javac -d ./bin --source-path ./src ./src/maze/Maze.java
javac -d ./bin ./src/tests/ModifierChecker.java
isStatic=$(java -cp bin tests/ModifierChecker)

find . -name "*.class" -type f -delete

# Compile the structural tests (these should compile and run even with no code written)
echo "Compiling structural tests..."
javac -d ./bin -cp .:junit-platform-console-standalone.jar ./src/tests/*/structural/CoordinateTest.java
javac -d ./bin -cp .:junit-platform-console-standalone.jar ./src/tests/*/structural/DirectionTest.java
javac -d ./bin -cp .:junit-platform-console-standalone.jar ./src/tests/*/structural/ExceptionTest.java
javac -d ./bin -cp .:junit-platform-console-standalone.jar ./src/tests/*/structural/MazeTest.java
javac -d ./bin -cp .:junit-platform-console-standalone.jar ./src/tests/*/structural/RouteFinderTest.java
javac -d ./bin -cp .:junit-platform-console-standalone.jar ./src/tests/*/structural/TileTest.java
javac -d ./bin -cp .:junit-platform-console-standalone.jar ./src/tests/*/structural/VisualisationTest.java

# Compile the functional tests (these won't compile without code)
echo "Compiling functional tests..."
javac -d ./bin -cp .:junit-platform-console-standalone.jar --source-path ./src ./src/tests/*/functional/MazeTest.java
javac -d ./bin -cp .:junit-platform-console-standalone.jar --source-path ./src ./src/tests/*/functional/RouteFinderTest.java
javac -d ./bin -cp .:junit-platform-console-standalone.jar --source-path ./src ./src/tests/*/functional/TileTest.java

# These functional tests will allow either a static or non-static Coordinate class
echo "isStatic is $isStatic"
if [ "$isStatic" == "true" ]
then
    javac -d ./bin -cp .:junit-platform-console-standalone.jar --source-path ./src ./src/tests/*/functional/MazeCoordinateStaticTest.java
elif [ "$isStatic" == "false" ]
then
    javac -d ./bin -cp .:junit-platform-console-standalone.jar --source-path ./src ./src/tests/*/functional/MazeCoordinateNotStaticTest.java
else
    javac -d ./bin -cp .:junit-platform-console-standalone.jar --source-path ./src ./src/tests/*/functional/MazeCoordinateErrorTest.java
fi

# All tests compiled, run whatever compiled OK
echo "Executing all compiled tests..."
java -jar junit-platform-console-standalone.jar --class-path ./bin --scan-class-path --fail-if-no-tests 