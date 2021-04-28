package maze;

public class MultipleExitException extends InvalidMazeException {
    /**
     * Exception thrown when multiple exits are detected.
     * @param message error message to display when Exception is thrown.
     */
    public MultipleExitException(String message) {
        super(message);
    }
}
