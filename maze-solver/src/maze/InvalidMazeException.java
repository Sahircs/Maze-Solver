package maze;

public class InvalidMazeException extends Exception {
    /**
     * Root Exception for all maze Exceptions created 
     * which all extend this InvalidMazeException.
     * @param message error message to display when Exception is thrown.
     */
    public InvalidMazeException(String message) {
        super(message);
    }
}
