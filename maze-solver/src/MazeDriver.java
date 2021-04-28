import java.io.FileNotFoundException;

/*
Optional Java Application used during development process.
Used for testing purposes.

./javac.sh src/MazeDriver.java      - compile
./java.sh MazeDriver                - run
*/

import maze.Maze;
import maze.InvalidMazeException;

public class MazeDriver {
    public static void main(String args[]) throws FileNotFoundException, InvalidMazeException {
        try {  
            System.out.println(Maze.fromTxt("resources/mazes/maze1.txt"));
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } 
    }
}
