package maze.routing;

import maze.Maze;
import maze.Tile;

import maze.InvalidMazeException;
import java.io.FileNotFoundException;

import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class RouteFinder {
    private Maze maze;
    private Stack<Tile> route;
    private boolean finished;

    public RouteFinder(Maze inputMaze) {
        maze = inputMaze;
        finished = false;
        route = new Stack<Tile>();
    }

    // Getters
    public Maze getMaze() {
        return maze;
    }

    // Returns a List of Tiles representing the current route from start to end
    public List<Tile> getRoute() {
        return new ArrayList<Tile>();
    }

    public boolean isFinished() {
        return finished;
    }

    // Reads a maze with its current route from a file
    public static RouteFinder load(String filePath) throws FileNotFoundException, InvalidMazeException {
        // try {  
        //     Maze mazeInstance = Maze.fromTxt(filePath);
        // } catch (FileNotFoundException e) {
        //     System.err.println(e.getMessage());
        // } 

        return new RouteFinder(Maze.fromTxt(filePath));
    }

    // Writes a maze with its current route to a file
    public void save(String mazeState) {

    }

    // Updates Stack by making 1 move
    public boolean step() {

        /* 
        If stack reaches end Tile ('x') -> finished = true

        If finished -> Stack remains the same -> return true

        If Stack stuck in circular loop -> NoRouteFoundException | Handle where method called ???
        
        
        */
        


        return false;
    }

    public String toString() {
        return "";
    }

}
