package maze.routing;

import maze.Maze;
import maze.Maze.Direction;
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
    public static Tile currentTile;
    public static boolean stackMove;
    public static Tile endTile;

    public RouteFinder(Maze inputMaze) {
        maze = inputMaze;
        finished = false;
        currentTile = maze.getEntrance();
        route = new Stack<Tile>();
    }

    public Maze getMaze() {
        return maze;
    }

    public Stack<Tile> getStackRoute() {
        return route;
    }

    // Returns a List of Tiles representing the current route from start to end
    public List<Tile> getRoute() {
        // Once finished: stack of tiles -> list of tiles, i.e. Complete Route
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
        System.out.println("-------------");
        /* 
        If stack reaches end Tile ('x') -> finished = true

        If finished -> Stack remains the same -> return true

        If Stack stuck in circular loop -> NoRouteFoundException | Handle where method called ???
        */
        
        if (possibleMove(Direction.NORTH, 0)) {
            makeMove(Direction.NORTH, 0, 2);
        } else if (possibleMove(Direction.EAST, 1)) {
            makeMove(Direction.EAST, 1, 3);
        } else if (possibleMove(Direction.SOUTH, 2)) {
            makeMove(Direction.SOUTH, 2, 0);
        } else if (possibleMove(Direction.WEST, 3)) {
            makeMove(Direction.WEST, 3, 1);
        } else {
            System.out.println("STACK MOVE!");
            // pop off stack 
            stackMove = true;
            route.pop();
        }
        
        return false;
    }

    public boolean possibleMove(Direction direction, int directionIndex) {
        // Checking if nextTile is within the bounds of the maze
        if (!checkMoveBoundaries(direction)) {
            return false;
        }

        Tile nextTile = maze.getAdjacentTile(currentTile, direction);

        return nextTile.isNavigable() && !currentTile.directionsVisited[directionIndex];
    }

    public boolean checkMoveBoundaries(Direction direction) {
        int x = maze.getTileLocation(currentTile).getX();
        int y = maze.getTiles().size() - maze.getTileLocation(currentTile).getY() - 1;

        switch (direction) {
            case NORTH:
                y--;          
                break;
            case SOUTH:
                y++;     
                break;   
            case EAST:
                x++;
                break;
            case WEST:
                x--;
                break;
            default: {
                break;
            }
        }

        if (y < 0 || y >= maze.getTiles().size() || x < 0 || x >= maze.getTiles().get(0).size()) {
            return false;
        }

        return true;
    }

    public void makeMove(Direction direction, int pastDirectionIndex, int nextDirectionIndex) {
        currentTile.directionsVisited[pastDirectionIndex] = true;
        currentTile = maze.getAdjacentTile(currentTile, direction);
        // Ensures we don't visit tile we came from again - prevents an infinite loop
        currentTile.directionsVisited[nextDirectionIndex] = true;
        route.push(currentTile); 
        stackMove = false;
    }

    public String toString() {
        return "";
    }

}
