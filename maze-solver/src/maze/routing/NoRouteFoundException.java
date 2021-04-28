package maze.routing;

public class NoRouteFoundException extends Exception {
    /**
     * Exception thrown when no route is found.
     * @param message error message to display when Exception is thrown.
     */
    public NoRouteFoundException(String message) {
        super(message);
    }    
}
