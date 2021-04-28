package maze.visualisation;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;


public class MazeCell {
    private Rectangle content;
    /**
     * Constructor that creates a MazeCell + and initialises all attributes required.
     * @param cellColour - colour to fill the Rectangle with.
     */
    public MazeCell(Color cellColour) {
        content = new Rectangle(0, 0, 30, 30);
        content.setFill(cellColour);
        content.setStroke(Color.BLACK);
    }
    /**
     * Updates the colour of the Rectangle to visualise a change in the the route-solving state.
     * @param newCellColour - updated colour to fill the Rectangle with.
     */
    public void setColour(Color newCellColour) {
        content.setFill(newCellColour);
    }
    /**
     * Getter method - to get the Rectangle object of the MazeCell instance.
     * @return Returns a Rectangle object
     */
    public Rectangle getRectangle() {
        return content;
    }
}
