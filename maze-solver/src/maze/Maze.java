package maze;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;


public class Maze {
    private Tile entrance;
    private Tile exit;
    private List<List<Tile>> tiles;

    // Extra variables to help when creating Maze
    private static boolean entranceExists = false;
    private static boolean exitExists = false;


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

        while (scan.hasNextLine()) {
            String[] mazeRowInput = scan.nextLine().replaceAll("\\s", "").split("");

            // Iterate through row and check if valid Tile
            List<Tile> rowOfTiles = new ArrayList<Tile>();
            for (String mazeCell : mazeRowInput) {
                if (mazeCell.equals("e")) {         // Entrance check
                    if (!entranceExists) {
                        Tile entranceTile = Tile.fromChar('e');
                        rowOfTiles.add(entranceTile);
                        mazeInstance.setEntrance(entranceTile);
                        continue;
                    } else {
                        // Multiple entrances
                        throw new MultipleEntranceException("Multiple Entrances detected in file!");
                    }
                } else if (mazeCell.equals("x")) {  // Exit check
                    if (!exitExists) {
                        Tile exitTile = Tile.fromChar('x');
                        rowOfTiles.add(exitTile);
                        mazeInstance.setExit(exitTile);
                        continue;
                    } else {
                        // Multiple exits
                        throw new MultipleExitException("Multiple Exits detected in file!");
                    }
                } else if (mazeCell.equals(".")) {  // Corridor check
                    rowOfTiles.add(Tile.fromChar('.'));
                    continue;
                } else if (mazeCell.equals("#")) {  // Wall check
                    rowOfTiles.add(Tile.fromChar('#'));
                    continue;
                } else {
                    // Not a valid cell
                    throw new RaggedMazeException("Character '" + mazeCell + "' not valid input!");
                }
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
    }

    public String toString() {
        String mazeVisualised = "";

        int rowSize = tiles.size();
        int colSize = tiles.get(0).size();

        for (int row = 0; row < rowSize; row++) {
            mazeVisualised += ((rowSize - row - 1) + "\t");
            for (int col = 0; col < colSize; col++) {
                // ~~~ Uncomment below statement for columns >= 10 ~~~
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
        return Tile.fromChar('e');
    }

    public Tile getTileAtLocation(Coordinate coord) {
        return tiles.get(coord.getY()).get(coord.getX());
    }

    public Coordinate getTileLocation(Tile tile) {
        // uuid -> Map<Id, Tile>
        return new Coordinate(1, 1);
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
