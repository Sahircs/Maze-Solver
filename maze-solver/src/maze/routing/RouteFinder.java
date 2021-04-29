package maze.routing;

import maze.Maze;
import maze.Maze.Direction;
import maze.Tile;

import maze.InvalidMazeException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;


public class RouteFinder implements Serializable {
    private Maze maze;
    private Stack<Tile> route;
    private boolean finished;
    /**
     * Tile variable representing the current tile within the route-solving process.
     */
    public static Tile currentTile;
    /**
     * Boolean variable representing whether or not a tile has been added to the Stack each move/step.
     */
    public static boolean stackMove;

    /**
     * Constructor that creates a RouteFinder object and initialises all attributes required.
     * @param inputMaze maze to be solved.
     */
    public RouteFinder(Maze inputMaze) {
        maze = inputMaze;
        finished = false;
        currentTile = maze.getEntrance();
        route = new Stack<Tile>();
        route.push(maze.getEntrance());
    }
    /**
     * Getter method - to get the maze that is being solved.
     * @return Returns a Maze object.
     */
    public Maze getMaze() {
        return maze;
    }
    /**
     * Getter method - to get the current Stack, {@link #route}, 
     * which contains Tile objects in the current route.
     * @return Returns a Stack of Tile objects.
     */
    public Stack<Tile> getStackRoute() {
        return route;
    }
    /**
     * Getter method - to find whether or not the route has been solved.
     * @return Returns {@link #finished}.
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Converts the Stack, {@link #route}, into a List of Tile objects 
     * to represent the current route from the entrance to exit Tile.
     * @return Returns a List of Tiles representing the current route from start to end.
     */
    @SuppressWarnings("unchecked")
    public List<Tile> getRoute() {
        List<Tile> routeList = new ArrayList<Tile>();
        Stack<Tile> tempRouteStack = (Stack<Tile>) route.clone();

        while (!tempRouteStack.isEmpty()) {
            routeList.add(tempRouteStack.pop());
        }

        Collections.reverse(routeList);
        
        return routeList; 
    }

    /**
     * Reads in a RouteFinder object from a file using 
     * @param filePath representing the path to the file containing a RouteFinder object.
     * @return Returns a RouteFinder object loaded from a file.
     * @throws FileNotFoundException if file is not found.
     * @throws InvalidMazeException if .txt file stores an invalid maze.
     * @see java.io.ObjectInputStream
     * @see java.io.FileInputStream
     */
    public static RouteFinder load(String filePath) throws FileNotFoundException, InvalidMazeException {
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(filePath)); 
            RouteFinder routeFinder = (RouteFinder) input.readObject();
            input.close();
            System.out.println("RouteFinder loaded successfully");
            stackMove = false;

            return routeFinder;
        } catch (IOException e) {
            e.printStackTrace();
        }  catch (ClassNotFoundException c) {
            c.printStackTrace();
        }

        return new RouteFinder(Maze.fromTxt("resources/mazes/maze1.txt"));
    }

    /**
     * Saves a RouteFinder object to a file.
     * @param filePath representing the path where the file will be saved to.
     */
    public void save(String filePath) {
        try {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filePath));
            output.writeObject(this);
            output.close();
            System.out.println("RouteFinder saved to File successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Updates Stack by making 1 move
    /**
     * Makes a move in the maze using the {@link #currentTile} and by updating the Stack. 
     * If {@link #finished} is true, nothing happens. 
     * It evaluates each direction and mimics the Depth-First Search Algorithm using the following steps.
     * 1. Checks if a move in that direction is possible using {@link #possibleMove(Direction, int)}
     * 2. If it's a possible move, then it makes the move using {@link #makeMove(Direction, int, int)}
     * @return Returns a boolean of whether or not the route has been solved.
     * @throws NoRouteFoundException if there exists no route.
     */
    public boolean step() throws NoRouteFoundException {
        if (!finished) {
            if (possibleMove(Direction.NORTH, 0)) {
                makeMove(Direction.NORTH, 0, 2);
            } else if (possibleMove(Direction.EAST, 1)) {
                makeMove(Direction.EAST, 1, 3);
            } else if (possibleMove(Direction.SOUTH, 2)) {
                makeMove(Direction.SOUTH, 2, 0);
            } else if (possibleMove(Direction.WEST, 3)) {
                makeMove(Direction.WEST, 3, 1);
            } else if (route.peek() == maze.getEntrance() && route.size() == 1) {
                throw new NoRouteFoundException("No Route Found :(");
            } else {
                // pop off stack 
                stackMove = true;
                route.pop();
            }
        }
         
        return finished;
    }

    /**
     * Checks if a move with the {@link #currentTile} and given direction is valid
     * by firstly checking boundaries using {@link #checkMoveBoundaries(Direction)},
     * then checking the directions visited so no moves are repeated.
     * @param direction - represents direction being evaluated.
     * @param directionIndex - index of direction within the directionVisited array.
     * @return Returns a boolean of whether or not the move is possibly valid.
     */
    public boolean possibleMove(Direction direction, int directionIndex) {
        // Checking if nextTile is within the bounds of the maze
        if (!checkMoveBoundaries(direction)) {
            return false;
        }

        Tile nextTile = maze.getAdjacentTile(currentTile, direction);

        return nextTile.isNavigable() && !currentTile.directionsVisited[directionIndex];
    }

    /**
     * Checks if the move in the given direction is within bounds of the maze. 
     * @param direction - represents direction being evaluated.
     * @return Returns a boolean of whether or not the boundaries of the move are valid.
     */
    public boolean checkMoveBoundaries(Direction direction) {
        int x = maze.getTileLocation(currentTile).getX();
        int y = maze.getTileLocation(currentTile).getY();

        switch (direction) {
            case NORTH:
                y++;          
                break;
            case SOUTH:
                y--;     
                break;   
            case EAST:
                x++;
                break;
            case WEST:
                x--;
                break;
            default: 
                break;
        }

        // Coordinate -> List format
        y = maze.getTiles().size() - y - 1;

        if (y < 0 || y >= maze.getTiles().size() || x < 0 || x >= maze.getTiles().get(0).size()) {
            return false;
        }

        return true;
    }

    /**
     * Makes a move by adding the new tile to the {@link #route} Stack which holds the route-finding state.
     * Updates the directionsVisited array of the old and new {@link #currentTile}
     * @param direction - represents direction in which the move is being made.
     * @param pastDirectionIndex - index with which to update the 'directionsVisited' of the old {@link #currentTile}.
     * @param nextDirectionIndex - index with which to update the 'directionsVisited' of the new {@link #currentTile}.
     */
    public void makeMove(Direction direction, int pastDirectionIndex, int nextDirectionIndex) {
        // Ensures we don't visit tile we came from again - prevents an infinite loop
        currentTile.directionsVisited[pastDirectionIndex] = true;
        currentTile = maze.getAdjacentTile(currentTile, direction);
        currentTile.directionsVisited[nextDirectionIndex] = true;

        route.push(currentTile); 
        stackMove = false;
        currentTile.setVisited();
        
        if (currentTile == maze.getExit()) {
            finished = true;
            System.out.println("MAZE SOLVED!");
        }
    }

    /**
     * Generated a String representation of the {@link #maze} including the route-solving state 
     * (i.e. tiles in the current route differentiated to tiles that used to be).
     * @return Returns a string that visualises the entire {@link #maze} and route-solving state
     */
    public String toString() {
        String mazeVisualised = "";
        List<List<Tile>> tiles = new ArrayList<List<Tile>>(maze.getTiles());
        Set<Tile> tileSet = new HashSet<Tile>();

        int colSize = tiles.size();

        Iterator stack = route.iterator();

        while (stack.hasNext()) {
            tileSet.add((Tile)stack.next());
        }

        for (int y = 0; y < colSize; y++) {
            mazeVisualised += (colSize - y - 1) + "\t";
            for (int x = 0; x < tiles.get(y).size(); x++) {
                Tile tile = tiles.get(y).get(x);
                if (!tile.getVisited()) {
                    mazeVisualised += tile.toString() + " ";
                } else if (tileSet.contains(tile)) {
                    mazeVisualised += "* ";
                } else {
                    mazeVisualised += "- ";
                }
            }
            mazeVisualised += "\n";
        }

        mazeVisualised += "\n\n\t";

        for (int i = 0; i < tiles.get(0).size(); i++) {
            mazeVisualised += i + " ";
        }

        return mazeVisualised;
    }

}
