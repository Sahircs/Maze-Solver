package maze;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

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

    // attributes
    public static void fromTxt(String filePath) throws FileNotFoundException {
        Maze mazeInstance = new Maze();
        // Tile tileInstance = new Tile(Tile.Type.WALL);

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
                        // MultipleEntranceException
                    }
                } else if (mazeCell.equals("x")) {  // Exit check
                    if (!exitExists) {
                        Tile exitTile = Tile.fromChar('e');
                        rowOfTiles.add(exitTile);
                        mazeInstance.setExit(exitTile);
                        continue;
                    } else {
                        // MultipleExitException
                    }
                } else if (mazeCell.equals(".")) {  // Corridor check
                    rowOfTiles.add(Tile.fromChar('.'));
                    continue;
                } else if (mazeCell.equals("#")) {  // Wall check
                    rowOfTiles.add(Tile.fromChar('#'));
                    continue;
                } else {                            // Not a valid cell
                    continue;
                }
            }

            // Add row to tiles list
            mazeInstance.tiles.add(rowOfTiles);
        }
        scan.close();

        
    }

    // Getters
    public List<List<Tile>> getTiles() {
        return tiles;
    }
    public Tile getEntrance() {
        return entrance;
    }
    public Tile getExit() {
        return exit;
    }

    // Setters
    private void setEntrance(Tile entranceTile) {
        entrance = entranceTile;
        entranceExists = true;
    }
    private void setExit(Tile exitTile) {
        exit = exitTile;
        exitExists = true;
    }
}
