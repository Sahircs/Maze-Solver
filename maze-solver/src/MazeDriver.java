import java.io.FileNotFoundException;

/*
Optional Java Application used in dev process.
Use to Print Maze/RouteFinder 
*/

import maze.Maze;

public class MazeDriver {
    public static void main(String args[]) throws FileNotFoundException {
        try {
            Maze.fromTxt("resources/mazes/maze1.txt");    
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
        
    }
}
