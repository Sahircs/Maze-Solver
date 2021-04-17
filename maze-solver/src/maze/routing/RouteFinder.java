package maze.routing;

import java.util.Stack;

public class RouteFinder {
    private Maze maze;
    private Stack<Tile> route;
    private boolean finished;

    public RouteFinder(Maze inputMaze) {
        maze = inputMaze;
    }

    // Getters
    public Maze getMaze() {
        return maze;
    }
    public Stack<Tile> getRoute() {
        return route;
    }
    public boolean isFinished() {
        return finished;
    }

    // public static RouteFinder load(String someStr) {

    // }

    public void save(String mazeState) {

    }

    public boolean step() {
        return false;
    }

    public String toString() {
        return "";
    }

}
