package maze.routing;

import maze.Maze;
import maze.Maze.Direction;
import maze.Tile;
import maze.Maze.Coordinate;

import maze.InvalidMazeException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;

import java.util.Stack;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class RouteFinder implements Serializable {
    private Maze maze;
    private Stack<Tile> route;
    private boolean finished;
    public static Tile currentTile;
    public static boolean stackMove;

    public RouteFinder(Maze inputMaze) {
        maze = inputMaze;
        finished = false;
        currentTile = maze.getEntrance();
        route = new Stack<Tile>();
        route.push(maze.getEntrance());
    }

    public Maze getMaze() {
        return maze;
    }

    public Stack<Tile> getStackRoute() {
        return route;
    }

    public boolean isFinished() {
        return finished;
    }
    
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

    public static RouteFinder load(String filePath) throws FileNotFoundException, InvalidMazeException {
        try {
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(filePath)); 
            RouteFinder routeFinder = (RouteFinder) input.readObject();
            input.close();
            System.out.println("RouteFinder loaded successfully");
            // RESET COORDINATE MAP + OTHER STATIC VARIABLES

            stackMove = false;

            return routeFinder;
        } catch (IOException e) {
            e.printStackTrace();
        }  catch (ClassNotFoundException c) {
            c.printStackTrace();
        } 

        return new RouteFinder(Maze.fromTxt("resources/mazes/maze1.txt"));
    }

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
            } else if (route.peek() == maze.getEntrance()) {
                throw new NoRouteFoundException("No Route Found :(");
            } else {
                // pop off stack 
                stackMove = true;
                route.pop();
            }
        }
         
        return finished;
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
            default: 
                break;
        }

        if (y < 0 || y >= maze.getTiles().size() || x < 0 || x >= maze.getTiles().get(0).size()) {
            return false;
        }

        return true;
    }

    public void makeMove(Direction direction, int pastDirectionIndex, int nextDirectionIndex) {
        // Ensures we don't visit tile we came from again - prevents an infinite loop
        currentTile.directionsVisited[pastDirectionIndex] = true;
        currentTile = maze.getAdjacentTile(currentTile, direction);
        currentTile.directionsVisited[nextDirectionIndex] = true;

        route.push(currentTile); 
        stackMove = false;
        
        if (currentTile == maze.getExit()) {
            finished = true;
            System.out.println("MAZE SOLVED!");
        }
    }

    public String toString() {
        return "";
    }

}
