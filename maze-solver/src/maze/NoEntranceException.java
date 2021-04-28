package maze;

public class NoEntranceException extends InvalidMazeException {
    /**
     * Exception thrown when no entrances are detected.
     * @param message error message to display when Exception is thrown.
     */
    public NoEntranceException(String message) {
        super(message);
    }
}
