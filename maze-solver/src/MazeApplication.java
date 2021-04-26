/* 
JavaFX application solves a maze using DFS

./javac.sh src/MazeApplication.java  
./java.sh MazeApplication
*/

// JavaFX Imports 
import javafx.application.Application;
// Containers/Layout
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
// Controls
import javafx.scene.control.ToggleButton;
// Properties
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
// Events
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent; 

import maze.Maze;
import maze.InvalidMazeException;
import maze.Tile;
import maze.Tile.Type;
import maze.routing.RouteFinder;
import maze.routing.NoRouteFoundException;
import maze.visualisation.MazeCell;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;

// Data Structures 
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

import java.util.Iterator;


public class MazeApplication extends Application {
    public static VBox root;
    public static GridPane mazeContainer;
    public static List<List<Tile>> tiles;
    public static List<List<MazeCell>> tileList = new ArrayList<List<MazeCell>>();
    public static Tile currentTile;
    public static Map<String, Color> tileColourMap = Map.ofEntries(
        Map.entry(Type.CORRIDOR.toString(), Color.WHITE),
        Map.entry(Type.ENTRANCE.toString(), Color.GREEN),
        Map.entry(Type.EXIT.toString(), Color.RED),
        Map.entry(Type.WALL.toString(), Color.BLACK)
    );
    public static RouteFinder routeFinder;
    

    @Override
    public void start (Stage stage) {
        // ~~~~~~ BUTTONS ~~~~~~

        // Load/Save buttons
        ToggleButton loadMap = new ToggleButton("Load Map");
        ToggleButton loadRoute = new ToggleButton("Load Route");
        ToggleButton saveRoute = new ToggleButton("Save Route");
        
        // Step button
        ToggleButton stepBtn = new ToggleButton("Step");

        // Event Handlers
        loadMap.addEventFilter(MouseEvent.MOUSE_CLICKED, loadMapHandler);
        loadRoute.addEventFilter(MouseEvent.MOUSE_CLICKED, loadRouteHandler);
        saveRoute.addEventFilter(MouseEvent.MOUSE_CLICKED, saveRouteHandler);
        stepBtn.addEventFilter(MouseEvent.MOUSE_CLICKED, stepHandler);
        
        // Container for load/save buttons
        HBox loadSaveButtonContainer = new HBox(10);
        loadSaveButtonContainer.setBackground(Background.EMPTY);
        loadSaveButtonContainer.setAlignment(Pos.CENTER);
        loadSaveButtonContainer.setSpacing(60);
        loadSaveButtonContainer.getChildren().addAll(loadMap, loadRoute, saveRoute);
        
        VBox buttonContainer = new VBox(10);
        buttonContainer.setBackground(Background.EMPTY);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setSpacing(20);
        buttonContainer.getChildren().addAll(loadSaveButtonContainer, stepBtn);

        // ~~~~~~ MAZE ~~~~~~

        // Container for maze
        mazeContainer = new GridPane();
        mazeContainer.setAlignment(Pos.CENTER);
        mazeContainer.setGridLinesVisible(true);

        root = new VBox(10);
        root.setBackground(Background.EMPTY);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(50);
        root.getChildren().addAll(buttonContainer, mazeContainer);

        // Creating and configuring a new scene
        Scene scene = new Scene(root, 800, 500);
        stage.setScene(scene);
        stage.setTitle("Maze Solver");
        stage.show();
    }

    
    EventHandler<MouseEvent> stepHandler = new EventHandler<MouseEvent>() { 
        @Override 
        public void handle(MouseEvent event) { 
            boolean isFinished = false;
            
            try {
                isFinished = routeFinder.step();
            } catch (NoRouteFoundException e) {
                System.err.println(e.getMessage());
                isFinished = true;
            }
            
            if (isFinished) {
                return;
            } 
            
            MazeCell mazeCell = getMazeCell(RouteFinder.currentTile);

            // No move possible -> tile popped off stack -> update UI
            if (RouteFinder.stackMove) {
                mazeCell.setColour(Color.WHITE);
                RouteFinder.currentTile = routeFinder.getStackRoute().peek();
            } else {
                // Move made -> update UI
                mazeCell.setColour(Color.YELLOW);
            }
        } 
    }; 
    
    // Method: eventhandler -> read in filename for loading/storing
    EventHandler<MouseEvent> loadMapHandler = new EventHandler<MouseEvent>() { 
        @Override 
        public void handle(MouseEvent event) { 
            String filePath = validateFileName("resources/mazes/", true);

            Maze.entranceExists = false;
            Maze.exitExists = false;
            Tile.idsAlreadyUsed.clear();

            try {
                routeFinder = new RouteFinder(Maze.fromTxt(filePath));
                System.out.println(routeFinder);
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
                System.out.println("File not found!");
            } catch (InvalidMazeException em) {
                System.out.println(em.getMessage());
            }

            RouteFinder.currentTile = routeFinder.getStackRoute().peek();
            currentTile = routeFinder.getMaze().getEntrance();
            tiles = routeFinder.getMaze().getTiles();
            tileList.clear();

            mazeContainer.getChildren().clear();

            initialiseMazeUI();
        } 
    }; 

    EventHandler<MouseEvent> loadRouteHandler = new EventHandler<MouseEvent>() { 
        @Override 
        public void handle(MouseEvent event) { 
            String filePath = validateFileName("resources/routes/", true);

            Maze.entranceExists = false;
            Maze.exitExists = false;
            Tile.idsAlreadyUsed.clear();

            try {
                routeFinder = RouteFinder.load(filePath);
                System.out.println(routeFinder);
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            } catch (InvalidMazeException e2) {
                System.out.println(e2.getMessage());
            }
            
            RouteFinder.currentTile = routeFinder.getStackRoute().peek();
            currentTile = routeFinder.getMaze().getEntrance();
            tiles = routeFinder.getMaze().getTiles();
            tileList.clear();

            mazeContainer.getChildren().clear();

            initialiseMazeUI();
            
            // Update UI with updated route
            updateRouteUI(routeFinder.getStackRoute(), Color.YELLOW);
        } 
    }; 

    EventHandler<MouseEvent> saveRouteHandler = new EventHandler<MouseEvent>() { 
        @Override 
        public void handle(MouseEvent event) { 
            String filePath = validateFileName("resources/routes/", false);
            routeFinder.save(filePath);
        } 
    }; 

    public static String validateFileName(String filePath, boolean loading) {
        boolean validName = false;
        Scanner scan = new Scanner(System.in);

        while (!validName) {
            if (loading) {
                System.out.print("Enter a filename to load from: \n>> ");
            } else {
                System.out.print("Enter a filename to save to: \n>> ");
            }

            String filename = scan.nextLine().strip().replaceAll("\\s+","");

            File possibleFile = new File(filePath + filename);
            
            if (loading && possibleFile.exists() || !loading && !possibleFile.exists()) {
                return filePath + filename;
            } else if (loading && !possibleFile.exists()) {
                System.out.println("File does not exist!");
            } else if (!loading && possibleFile.exists()) {
                System.out.println("File Already exists!");
            }
        }

        scan.close();
        
        return "";
    }

    public void updateRouteUI(Stack routeStack, Color colour) {
        Iterator stack = routeStack.iterator();

        while (stack.hasNext()) {
            MazeCell mazeCell = getMazeCell((Tile)stack.next());
            mazeCell.setColour(colour);
        }
        getMazeCell(routeFinder.getMaze().getEntrance()).setColour(Color.GREEN);
    }

    public MazeCell getMazeCell(Tile tile) {
        int x = routeFinder.getMaze().getTileLocation(tile).getX();
        int y = tileList.size() - routeFinder.getMaze().getTileLocation(tile).getY() - 1;

        return tileList.get(y).get(x);
    }

    public void initialiseMazeUI() {
        for (int y = 0; y < tiles.size(); y++) {
            List<MazeCell> tempTileList = new ArrayList<MazeCell>();
            for (int x = 0; x < tiles.get(y).size(); x++) {
                String tileType = tiles.get(y).get(x).getType().toString();
                MazeCell mazeCell = new MazeCell(tileColourMap.get(tileType)); 

                mazeContainer.add(mazeCell.getRectangle(), x, y);
                tempTileList.add(mazeCell);
            }
            tileList.add(tempTileList);
        }
    }

    public static void main(String args[]) throws FileNotFoundException, InvalidMazeException {
        launch(args);
    }
}
