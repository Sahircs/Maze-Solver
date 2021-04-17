package maze;

public class MultipleExitException extends InvalidMazeException {
    public MultipleExitException(String message) {
        super(message);
    }
}
