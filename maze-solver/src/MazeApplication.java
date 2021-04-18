/* 
JavaFX application that allows users to specify a text file containing a maze representation

./javac.sh src/MazeApplication.java  
./java.sh MazeApplication
*/

import javafx.application.Application;
// Containers/Layout
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;

// Controls
// import javafx.scene.control.Button;
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

import java.util.List;
import java.util.ArrayList;

public class MazeApplication extends Application {
    public static String filePath = "resources/mazes/maze1.txt";
    public static List<List<Rectangle>> tileList = new ArrayList<List<Rectangle>>();
    // Walls: Black    
    // Tiles -> not-visited: blue  ||  visited: green
    // Current-Tile: yellow

    @Override
    public void start (Stage stage) {
        // ~~~~~~ BUTTONS ~~~~~~

        // Load/Save buttons
        ToggleButton loadMap = new ToggleButton("Load Map");
        ToggleButton loadRoute = new ToggleButton("Load Route");
        ToggleButton saveRoute = new ToggleButton("Save Route");
        
        // Step button
        ToggleButton stepBtn = new ToggleButton("Step");
        // loadMap.addEventFilter(MouseEvent.MOUSE_CLICKED, eventHandler);
        
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
        GridPane mazeContainer = new GridPane();
        mazeContainer.setAlignment(Pos.CENTER);
        mazeContainer.setVgap(2); 
        mazeContainer.setHgap(2);

        for (int y = 0; y < 10; y++) {
            for (int x = 0; x < 16; x++) {
                Rectangle mazeCell = new Rectangle(0, 0, 30, 30); 
                mazeCell.setFill((x % 2 == 0) ? Color.BLACK : Color.PURPLE);
                mazeContainer.add(mazeCell, x, y);
            }
        }

        VBox root = new VBox(10);
        root.setBackground(Background.EMPTY);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);
        root.getChildren().addAll(buttonContainer, mazeContainer);

        // Creating and configuring a new scene
        Scene scene = new Scene(root, 800, 500);
        stage.setScene(scene);
        stage.setTitle("Maze Solver");
        stage.show();
    }

    // Method: eventhandler -> read in filename for loading/storing
    EventHandler<MouseEvent> eventHandler = new EventHandler<MouseEvent>() { 
        @Override 
        public void handle(MouseEvent e) { 
           System.out.println("btn clicked!"); 
        } 
    }; 

    public static void main(String args[]) {
        launch(args);
    }
}
