@echo "Removing class files..."
@del /s /q *.class
@javac -d ./bin --source-path ./src  ./src/maze/Maze.java
@javac -d ./bin src/tests/ModifierChecker.java

@rem @set isStatic=(java -cp bin tests/ModifierChecker)
for /f %%i in ('java -cp bin tests/ModifierChecker') do set isStatic=%%i
@del /s /q *.class

@rem Compile structural tests
@echo "Compiling structural tests..."
@for %%i in (CoordinateTest, DirectionTest, ExceptionTest, MazeTest, RouteFinderTest, TileTest, VisualisationTest) do (
    javac -d ./bin -cp .:junit-platform-console-standalone.jar --source-path ./src ./src/tests/*/structural/%%i.java
	)

@rem Compile functional tests (these won't compile without code)
@echo "Compiling functional tests..."
@for %%i in (MazeTest, RouteFinderTest, TileTest) do (
    javac -d ./bin -cp .:junit-platform-console-standalone.jar --source-path ./src ./src/tests/*/functional/%%i.java
	)

echo %isStatic%
if %isStatic% == true (
	javac -d ./bin -cp .;junit-platform-console-standalone.jar --source-path ./src src/tests/dev/MazeCoordinateStaticTest.java
	)

if %isStatic% == false (
	javac -d ./bin -cp .;junit-platform-console-standalone.jar --source-path ./src src/tests/dev/MazeCoordinateNotStaticTest.java
	)

if NOT %isStatic% == true (
	if NOT %isStatic% == false
    	javac -d ./bin -cp .;junit-platform-console-standalone.jar --source-path ./src src/tests/dev/MazeCoordinateErrorTest.java
	    )
	)

@rem All tests compiled, run whatever compiled OK
@echo "Executing all compiled tests..."
java -jar junit-platform-console-standalone.jar --class-path ./bin --scan-class-path --fail-if-no-tests
