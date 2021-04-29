package maze;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.util.Scanner;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Maze implements Serializable {
    /**	
	*	Class for the Maze which uses the Tile class to create the contents of the Maze
	*	@author	Sahir Ali
	*	@see	java.nio.file.Path 
	*/	
    private Tile entrance;
    private Tile exit;
    private List<List<Tile>> tiles;
    /**
    * Boolean variable representing whether or not the entrance exists.
    */
    public static boolean entranceExists = false;
    /**
    * Boolean variable representing whether or the exit exists.
    */
    public static boolean exitExists = false;
    /**
    * HashMap that maps a particular Tile object to its Coordinate object.
    */
    private Map<Tile, Coordinate> tileToCoordinateMap;

    private Maze() {
        tiles = new ArrayList<List<Tile>>();
        tileToCoordinateMap = new HashMap<Tile, Coordinate>();
    }

    /**
    * Getter method - to get the list of tiles within the corresponding maze.
    * @return Returns a 2D ArrayList of Tiles.
    */
    public List<List<Tile>> getTiles() {
        return tiles;
    }
    /** 
    * Getter method - to get the entrance Tile set.
    * @return Returns the {@link #entrance} tile.
    */
    public Tile getEntrance() {
        return entrance;
    }
    /** 
    * Getter method - to get the exit Tile set.
    * @return Returns the {@link #exit} tile.
    */
    public Tile getExit() {
        return exit;
    }
    /** 
    * Getter method - to get the HashMap ({@link #tileToCoordinateMap}) set.
    * @return Returns a HashMap that maps a Tile to its Coordinate object.
    */
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

    /**
     * Creates the Maze using the .txt file (retrieved using the file path) for the maze contents. 
     * Contents are instantiated as Tile objects which is then added to a 2D list of Tiles {@link #tiles} representing the maze. 
     * Method also sets the {@link #entrance} and {@link #exit} Tile 
     * and creates a Map full of Tiles that map onto their Coordinates {@link #tileToCoordinateMap}.
     * @param filepath the path to the .txt file which contains the contents of the maze.
     * @return Returns a Maze instance with all of its attributes set.
     * @throws RaggedMazeException If sizes of rows are not equal.
     * @throws MultipleEntranceException If multiple entrances are detected.
     * @throws MultipleExitException If multiple exits are detected.
     * @throws FileNotFoundException If the file is not found - incorrect filepath parameter.
     * @throws InvalidMazeException If the .txt file contains characters which are not valid.
     */
    public static Maze fromTxt(String filepath) throws FileNotFoundException, InvalidMazeException {
        entranceExists = false;
        exitExists = false;

        int expectedRowSize = 0;
        boolean rowSet = false;

        Maze mazeInstance = new Maze();

        Scanner scan = new Scanner(new FileReader(filepath));
        
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

    /**
     * Updates the 'directionsVisited' attribute of each Tile object 
     * so we can automatically rule out certain moves as invalid/already visited
     * @param tileList {@link #tiles} attribute of a Maze instance 
     * @return Returns updated {@link #tiles}
     */
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
    
    /**
     * Gets the tile next to the 'tile' input in a specified Direction using the enum, Maze.Direction. 
     * @param tile a Tile object which is contained within the maze.
     * @param direction the direction to look for the adjacent Tile.
     * @return Returns the tile next to a specified tile in a given Direction.
     */
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

    /**
     * Retrieves the tile using a given Coordinate object
     * @param coord the Coordinate object that should correspond to a Tile object
     * @return Returns the tile at given location
     */
    public Tile getTileAtLocation(Coordinate coord) {
        int x = coord.getX();
        int y = tiles.size() - coord.getY() - 1;

        return tiles.get(y).get(x);
    }

    /**
     * Gets the location of a Tile object using the HashMap {@link #tileToCoordinateMap}.
     * @param tile the Tile object 
     * @return Returns location of the tile
     */
    public Coordinate getTileLocation(Tile tile) {
        return tileToCoordinateMap.get(tile);
    }

    /**
     * An Enum containing all the possible directions you can move within the maze.
     */
    public enum Direction {
        NORTH,
        SOUTH,
        EAST,
        WEST;
    }

    public static class Coordinate implements Serializable {
        private int x;
        private int y;

        /**
         * Constructor which requires the x and y coordinate to create a Coordinate object.
         * @param xVal x-coordinate
         * @param yVal y-coordinate 
         */
        public Coordinate(int xVal, int yVal) {
            x = xVal;
            y = yVal;
        }

        /**
         * Getter method - to get the {@link x} coordinate. 
         * @return Returns the {@link x} attribute.
         */
        public int getX() {
            return x;
        }

        /**
         * Getter method - to get the {@link y} coordinate. 
         * @return Returns the {@link y} attribute.
         */
        public int getY() {
            return y;
        }

        /**
         * Creates a string representation of the coordinates in the from (x, y).
         * @return Returns a string representing the coordinates.
         */
        public String toString() {
            return "(" + x + ", " + y + ")" ;
        }
    }

    /**
     * Iterates over the {@link #tiles} attribute of the Maze and 
     * converts the contents into a String representation of the whole maze with coordinates.
     * @return Returns a string that visualises the entire maze.
     */
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
}
