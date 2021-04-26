package maze.visualisation;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;


public class MazeCell {
    private Rectangle content;
    private Color colour;

    public MazeCell(Color cellColour) {
        content = new Rectangle(0, 0, 30, 30);
        content.setFill(cellColour);
        content.setStroke(Color.BLACK);
        colour = cellColour;
    }
    
    public void setColour(Color newCellColour) {
        content.setFill(newCellColour);
        colour = newCellColour;
    }
    
    public Rectangle getRectangle() {
        return content;
    }
    
    public Color getColour() {
        return colour;
    }
}
