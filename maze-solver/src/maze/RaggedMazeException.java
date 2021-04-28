package maze;

public class RaggedMazeException extends InvalidMazeException {
    /**
     * Exception thrown when its detected that the row sizes 
     * of a maze are not all equal.
     * @param message error message to display when Exception is thrown.
     */
    public RaggedMazeException(String message) {
        super(message);
    }
}
