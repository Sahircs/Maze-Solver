/* 
JavaFX application that solves a maze using DFS

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
// maze package
import maze.Maze;
import maze.InvalidMazeException;
import maze.Tile;
import maze.Tile.Type;
import maze.routing.RouteFinder;
import maze.routing.NoRouteFoundException;
import maze.visualisation.MazeCell;
// IO
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
// Data Structures 
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;
// Others
import java.util.Iterator;


public class MazeApplication extends Application {
    /**
     * GridPane variable which is the container for the visualized maze. 
     */
    public static GridPane mazeContainer;
    /**
     * 2D List of Tile objects
     */
    public static List<List<Tile>> tiles;
    /**
     * 2D List of MazeCell objects used in the actual maze visualization
     */
    public static List<List<MazeCell>> tileList = new ArrayList<List<MazeCell>>();
    /**
     * Tile variable representing the currentTile at a particular time.
     */
    public static Tile currentTile;
    /**
     * HashMap that maps a Type to its corresponding colour in the maze.
     */
    public static Map<String, Color> tileColourMap = Map.ofEntries(
        Map.entry(Type.CORRIDOR.toString(), Color.WHITE),
        Map.entry(Type.ENTRANCE.toString(), Color.GREEN),
        Map.entry(Type.EXIT.toString(), Color.RED),
        Map.entry(Type.WALL.toString(), Color.BLACK)
    );
    /**
     * RouteFinder object used in creating/manipulating the maze.
     */
    public static RouteFinder routeFinder;
    

    /**
     * Required method in JavaFX Applications. Contains the whole structure of the Application, 
     * with the top-half containing buttons to add/load mazes/routes and to 
     * step through the algorithm used to solve the maze. The bottom-half contains the actual maze 
     * as a GridPane of Rectangle objects. 
     * Each button also has a corresponding EventHandler to provide its required functionality.
     * @param stage required in JavaFX Applications
     */
    @Override
    public void start(Stage stage) {
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

        VBox root = new VBox(10);
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

    /**
     * EventHandler for the 'Step' button. It calls the 'step' method using the {@link #routeFinder} 
     * which executes the algorithm to make 1 move. This is then handled here to update the Application
     * so that it displays the changes made.
     */
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
    /**
     * EventHandler for the 'Load Map' button. Reads in filename (of a maze .txt file) 
     * via the Terminal using the {@link #validateFileName(String, boolean)} method.
     * It then creates a new RouteFinder object and a maze using the filePath provided.
     * The Application is then updated so it displays the new maze.
     */
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

    /**
     * EventHandler for the 'Load Route' button. Reads in filename (of a route .txt file) 
     * via the Terminal using the {@link #validateFileName(String, boolean)} method.
     * The Application is then updated so it displays the new maze with its route-solving state.
     */
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

    /**
     * EventHandler for the 'Save Route' button. Reads in filename (of a new route .txt file) 
     * via the Terminal using the {@link #validateFileName(String, boolean)} method.
     * If filename is not already taken - a new file is created which stores the current 
     * routeFinder object, with its maze and route-solving state.
     */
    EventHandler<MouseEvent> saveRouteHandler = new EventHandler<MouseEvent>() { 
        @Override 
        public void handle(MouseEvent event) { 
            String filePath = validateFileName("resources/routes/", false);
            routeFinder.save(filePath);
        } 
    }; 

    /**
     * Helper method to validate a filename and ensure a valid one is chosen whilst 
     * providing a nice and easy way for the user to choose one.
     * @param filePath contains the rest of the file path which is to be returned once filename validated.
     * @param loading boolean variable for whether its loading/saving so an appropriate message is sent.
     * @return Returns a String representing the filePath that will be used to load/save in the EventHandlers.  
     */
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

    /**
     * Helper method which updates strictly the route based on whether you're adding/removing the route.
     * @param routeStack a Stack which contains all the Tiles currently in the route.
     * @param colour a Color object to update the route tiles in.
     */
    public void updateRouteUI(Stack routeStack, Color colour) {
        Iterator stack = routeStack.iterator();

        while (stack.hasNext()) {
            MazeCell mazeCell = getMazeCell((Tile)stack.next());
            mazeCell.setColour(colour);
        }
        getMazeCell(routeFinder.getMaze().getEntrance()).setColour(Color.GREEN);
    }

    /**
     * Helper method to get the MazeCell using its corresponding tile.
     * @param tile a Tile object within the maze.
     * @return Returns a MazeCell object.
     */
    public MazeCell getMazeCell(Tile tile) {
        int x = routeFinder.getMaze().getTileLocation(tile).getX();
        int y = tileList.size() - routeFinder.getMaze().getTileLocation(tile).getY() - 1;

        return tileList.get(y).get(x);
    }

    /**
     * Creates the maze and updates the tileList with the new MazeCell objects.
     */
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

    /**
     * Required method - launches the JavaFX Application
     */
    public static void main(String args[]) throws FileNotFoundException, InvalidMazeException {
        launch(args);
    }
}
