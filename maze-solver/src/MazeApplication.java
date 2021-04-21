/* 
JavaFX application that allows users to specify a text file containing a maze representation

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
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Polygon;
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

import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class MazeApplication extends Application {
    public static VBox root;
    public static GridPane mazeContainer;
    public static String filePath = "resources/mazes/maze1.txt";
    public static List<List<Tile>> tiles;
    public static List<List<Rectangle>> tileList = new ArrayList<List<Rectangle>>();
    // public static Rectangle currentRectangle = new Rectangle(0, 0, 0, 0);
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

        // ~~~ TRY WITH ARROW FUNC -> UPDATE SCENE FOR NEW MAZE
        // loadMap.addEventFilter(MouseEvent.MOUSE_CLICKED, loadMapHandler);
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
        // mazeContainer.setVgap(2); 
        // mazeContainer.setHgap(2);
        mazeContainer.setGridLinesVisible(true);

        for (int y = 0; y < tiles.size(); y++) {
            List<Rectangle> tempTileList = new ArrayList<Rectangle>();
            for (int x = 0; x < tiles.get(y).size(); x++) {
                Rectangle mazeCell = new Rectangle(0, 0, 30, 30); 
                String tileType = tiles.get(y).get(x).getType().toString();
                mazeCell.setFill(tileColourMap.get(tileType));
                mazeCell.setStroke(Color.BLACK);

                mazeContainer.add(mazeCell, x, y);
                tempTileList.add(mazeCell);

                if (tileType == Type.ENTRANCE.toString()) {
                    // currentRectangle = mazeCell;
                    // currentTile = tiles.get(y).get(x);
                    // currentTile.setFill(Color.YELLOW);
                }
            }
            tileList.add(tempTileList);
        }

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

    // Method: eventhandler -> read in filename for loading/storing
    // EventHandler<MouseEvent> loadMapHandler = new EventHandler<MouseEvent>() { 
    //     @Override 
    //     public void handle(MouseEvent e) { 
    //         root.getChildren().remove(1);
    //         root.getChildren().add(new ToggleButton("Testing"));
    //     } 
    // }; 

    EventHandler<MouseEvent> stepHandler = new EventHandler<MouseEvent>() { 
        @Override 
        public void handle(MouseEvent e) { 
            boolean isFinished = routeFinder.step();

            if (isFinished) {
                // do something..
                return;
            } 

            int x = routeFinder.getMaze().getTileLocation(routeFinder.currentTile).getX();
            int y = tileList.size() - routeFinder.getMaze().getTileLocation(routeFinder.currentTile).getY();
            Rectangle tile = tileList.get(y - 1).get(x);
            
            // No move possible -> tile popped off stack -> update UI
            if (routeFinder.stackMove) {
                tile.setFill(Color.WHITE);
                routeFinder.currentTile = routeFinder.getStackRoute().peek();
            } else {
                // Move made -> update UI
                tile.setFill(Color.YELLOW);
            }
        } 
    }; 

    public static void main(String args[]) throws FileNotFoundException, InvalidMazeException {
        try {  
            // tiles = Maze.fromTxt("resources/mazes/maze1.txt").getTiles();
            routeFinder = RouteFinder.load("resources/mazes/maze1.txt");
            tiles = routeFinder.getMaze().getTiles();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } 

        launch(args);
    }
}
