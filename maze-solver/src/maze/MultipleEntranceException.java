package maze;

public class MultipleEntranceException extends InvalidMazeException {
    /**
     * Exception thrown when multiple entrances are detected.
     * @param message error message to display when Exception is thrown.
     */
    public MultipleEntranceException(String message) {
        super(message);
    }
}
