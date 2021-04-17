package maze;

public class NoExitException extends InvalidMazeException {
    public NoExitException(String message) {
        super(message);
    }
}
