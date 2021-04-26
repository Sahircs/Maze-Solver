package maze;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.Scanner;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
// javadoc -d ./html-docs -sourcepath ./Maze.java -subpackages maze
// javadoc -d ./html-docs src/Maze.java


public class Maze implements Serializable {
    /**	
	*	Class for the Maze which uses the Tile class to create the contents of the Maze
	*	@author	Sahir Ali
	*	@version	1.1,	25th	April	2021
	*	@see	java.nio.file.Path
	*/	
    private Tile entrance;
    private Tile exit;
    private List<List<Tile>> tiles;
    // Extra variables to help when creating Maze
    public static boolean entranceExists = false;
    public static boolean exitExists = false;
    // HashMap: Tile -> Coordinate
    private Map<Tile, Coordinate> tileToCoordinateMap;

    private Maze() {
        tiles = new ArrayList<List<Tile>>();
        tileToCoordinateMap = new HashMap<Tile, Coordinate>();
    }

    // Getters: variables
    public List<List<Tile>> getTiles() {
        return tiles;
    }
    public Tile getEntrance() {
        return entrance;
    }
    public Tile getExit() {
        return exit;
    }
    public Map<Tile, Coordinate> getMap() {
        return tileToCoordinateMap;
    }

    // Setters: variables
    private void setEntrance(Tile entranceTile) throws InvalidMazeException {
        if (entranceExists) {
            throw new MultipleEntranceException("Multiple Entrances");
        }

        entrance = entranceTile;
        entranceExists = true;
    }
    private void setExit(Tile exitTile) throws InvalidMazeException {
        if (exitExists) {
            throw new MultipleExitException("Multiple Exits");
        }

        exit = exitTile;
        exitExists = true;
    }

    // Creating Maze
    public static Maze fromTxt(String filename) throws FileNotFoundException, InvalidMazeException {
        entranceExists = false;
        exitExists = false;

        int expectedRowSize = 0;
        boolean rowSet = false;

        Maze mazeInstance = new Maze();

        Scanner scan = new Scanner(new FileReader(filename));
        
        while (scan.hasNextLine()) {
            String[] mazeRowInput = scan.nextLine().replaceAll("\\s", "").split("");

            if (!rowSet) {
                expectedRowSize = mazeRowInput.length;
                rowSet = true;
            } else if (rowSet && mazeRowInput.length != expectedRowSize) {
                throw new RaggedMazeException("Sizes of rows are not equal!");
            } 

            List<Tile> rowOfTiles = new ArrayList<Tile>();
            
            // Iterate through row and check if valid Tile
            for (int idx = 0; idx < mazeRowInput.length; idx++) {
                String mazeCell = mazeRowInput[idx];
                Tile tile;
                if (mazeCell.equals("e")) {         // Entrance check
                    if (!entranceExists) {
                        tile = Tile.fromChar('e');
                        mazeInstance.setEntrance(tile);
                    } else {
                        // Multiple entrances
                        throw new MultipleEntranceException("Multiple Entrances detected in file!");
                    }
                } else if (mazeCell.equals("x")) {  // Exit check
                    if (!exitExists) {
                        tile = Tile.fromChar('x');
                        mazeInstance.setExit(tile);
                    } else {
                        // Multiple exits
                        throw new MultipleExitException("Multiple Exits detected in file!");
                    }
                } else if (mazeCell.equals(".")) {  // Corridor check
                    tile = Tile.fromChar('.');
                } else if (mazeCell.equals("#")) {  // Wall check
                    tile = Tile.fromChar('#');
                } else {
                    // Not a valid cell
                    throw new InvalidMazeException("Invalid character");
                }

                rowOfTiles.add(tile);
            }

            // Add row to tiles list
            mazeInstance.tiles.add(rowOfTiles);
        }

        scan.close();

        // No entrances
        if (!entranceExists) {
            throw new NoEntranceException("No Entrance detected in file!");
        }
        // No exits
        if (!exitExists) {
            throw new NoExitException("No Exit detected in file!");
        }

        // Setting coordinates for all Tiles 
        for (int y = 0; y < mazeInstance.tiles.size(); y++) {
            for (int x = 0; x < mazeInstance.tiles.get(y).size(); x++) {
                int yCoord = mazeInstance.tiles.size() - 1 - y;
                mazeInstance.tileToCoordinateMap.put(mazeInstance.tiles.get(y).get(x), new Coordinate(x, yCoord));
            }
        }

        mazeInstance.tiles = updateDirectionsVisited(mazeInstance.tiles);

        return mazeInstance;
    }

    public static List<List<Tile>> updateDirectionsVisited(List<List<Tile>> tileList) {
        int rowEnd = tileList.size() - 1;
        int columnEnd = tileList.get(0).size() - 1;

        for (int y = 0; y < tileList.size(); y++) {
            for (int x = 0; x < tileList.get(y).size(); x++) {
                Tile tile = tileList.get(y).get(x);

                // UP
                if (y == 0 || !tileList.get(y - 1).get(x).isNavigable()) {
                    tile.directionsVisited[0] = true;
                } 
                // Right
                if (x == columnEnd || !tileList.get(y).get(x + 1).isNavigable()) {
                    tile.directionsVisited[1] = true;
                } 
                // Bottom
                if (y == rowEnd || !tileList.get(y + 1).get(x).isNavigable()) {
                    tile.directionsVisited[2] = true;
                }
                // Left 
                if (x == 0 || !tileList.get(y).get(x - 1).isNavigable()) {
                    tile.directionsVisited[3] = true;
                }
            }
        }
        return tileList;
    }

    public String toString() {
        String mazeVisualised = "";

        int rowSize = tiles.size();
        int colSize = tiles.get(0).size();

        for (int row = 0; row < rowSize; row++) {
            mazeVisualised += ((rowSize - row - 1) + "\t");
            for (int col = 0; col < colSize; col++) {
                // For rows >= 10  ->  they need some extra space
                if (Integer.toString(col).length() > 1) {
                    mazeVisualised += " ";
                }
                mazeVisualised += (tiles.get(row).get(col).toString() + "  ");
            }
            mazeVisualised += "\n\n";
        }

        mazeVisualised += "\n\n\t";

        // display columns at the bottom
        for (int col = 0; col < colSize; col++) {
            mazeVisualised += (col + "  ");
        }
        mazeVisualised += "\n";

        return mazeVisualised;
    }

    public Tile getAdjacentTile(Tile tile, Direction direction) {
        Coordinate tileCoords = getTileLocation(tile);

        int x = tileCoords.getX();
        int y = tileCoords.getY();

        if (direction == Direction.NORTH) {
            y++;
        } else if (direction == Direction.SOUTH) {
            y--;
        } else if (direction == Direction.EAST) {
            x++;
        } else if (direction == Direction.WEST) {
            x--;
        } 

        // Boundary check
        // if (x < 0 || x >= tiles.get(0).size() || y < 0 || y >= tiles.size()) {
        //     throw new RaggedMazeException("Current tile has no adjacent tile in that direction!");
        // }

        return tiles.get(tiles.size() - y - 1).get(x);
    }

    public Tile getTileAtLocation(Coordinate coord) {
        int x = coord.getX();
        int y = tiles.size() - coord.getY() - 1;

        return tiles.get(y).get(x);
    }

    public Coordinate getTileLocation(Tile tile) {
        // Using HashMap
        return tileToCoordinateMap.get(tile);
    }

    public enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST;
    }

    public static class Coordinate implements Serializable {
        private int x;
        private int y;

        public Coordinate(int xVal, int yVal) {
            x = xVal;
            y = yVal;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public String toString() {
            return "(" + x + ", " + y + ")" ;
        }
    }
}
