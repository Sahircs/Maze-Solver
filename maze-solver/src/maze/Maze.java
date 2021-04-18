package maze;


import java.io.FileReader;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;


public class Maze {
    private Tile entrance;
    private Tile exit;
    private List<List<Tile>> tiles;
    // Extra variables to help when creating Maze
    private static boolean entranceExists = false;
    private static boolean exitExists = false;
    // HashMap: Tile -> Coordinate
    public static Map<Tile, Coordinate> tileToCoordinateMap = new HashMap<Tile, Coordinate>();

    private Maze() {
        tiles = new ArrayList<List<Tile>>();
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

    // Setters: variables
    private void setEntrance(Tile entranceTile) {
        entrance = entranceTile;
        entranceExists = true;
    }
    private void setExit(Tile exitTile) {
        exit = exitTile;
        exitExists = true;
    }

    // Creating Maze
    public static void fromTxt(String filePath) throws FileNotFoundException, InvalidMazeException {
        Maze mazeInstance = new Maze();

        Scanner scan = new Scanner(new FileReader(filePath));

        int rowNumber = 0;
        
        while (scan.hasNextLine()) {
            String[] mazeRowInput = scan.nextLine().replaceAll("\\s", "").split("");

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
                    throw new RaggedMazeException("Character '" + mazeCell + "' not valid input!");
                }

                rowOfTiles.add(tile);
                tileToCoordinateMap.put(tile, new Coordinate(idx, rowNumber));
            }

            // Add row to tiles list
            mazeInstance.tiles.add(rowOfTiles);
            rowNumber++;
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

        // Reverse: rows start from the bottom 
        Collections.reverse(mazeInstance.tiles);
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

    public Tile getAdjacentTile(Tile tile, Direction direction) throws InvalidMazeException {
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
        if (x < 0 || x >= tiles.get(0).size() || y < 0 || y >= tiles.size()) {
            throw new RaggedMazeException("Current tile has no adjacent tile in that direction!");
        }

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

    public static class Coordinate {
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
