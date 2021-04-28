package maze;

public class NoExitException extends InvalidMazeException {
    /**
     * Exception thrown when no exits are detected.
     * @param message error message to display when Exception is thrown.
     */
    public NoExitException(String message) {
        super(message);
    }
}
