import java.io.FileNotFoundException;

/*
Optional Java Application used in dev process.
Use to Print Maze/RouteFinder 

./javac.sh src/MazeDriver.java  
./java.sh MazeDriver
*/

import maze.Maze;
import maze.InvalidMazeException;

public class MazeDriver {
    public static void main(String args[]) throws FileNotFoundException, InvalidMazeException {
        try {  
            Maze.fromTxt("resources/mazes/maze1.txt");
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } 

        // try {
        //     // String[] temp = new String[1];
        //     // System.out.println(temp[10]);
        //     throw new InvalidMazeException("CUSTOM Error");
        // } catch (InvalidMazeException e) {
        //     // throw new InvalidMazeException("LETS-GO");
        //     throw new RaggedMazeException("Ragged MAZE");
        // }

        // for (int i = 0; i < 10; i++) {
        //     if (i == 5) {
        //         throw new RaggedMazeException("Ragged MAZE");
        //     }
        //     System.out.println();
        // }
        
    }
}
