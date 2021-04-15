// Version 1.1, Wednesday 10th March 2021 @ 3:35pm
package tests.dev.structural;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExceptionTest {

    // ~~~~~~~~~~ Utility Functions ~~~~~~~~~~

    public Class ensureClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException: " + className);
        }
        return null;
    }

    // ~~~~~~~~~~ Structural tests ~~~~~~~~~~

    @Test
    public void ensureMultipleEntranceExceptionIsInvalidMazeException() {
        assertTrue(ensureClass("maze.InvalidMazeException").isAssignableFrom(
            ensureClass("maze.MultipleEntranceException")
        ));
    }

    @Test
    public void ensureMultipleExitExceptionIsInvalidMazeException() {
        assertTrue(ensureClass("maze.InvalidMazeException").isAssignableFrom(
            ensureClass("maze.MultipleExitException")
        ));
    }

    @Test
    public void ensureNoEntranceExceptionIsInvalidMazeException() {
        assertTrue(ensureClass("maze.InvalidMazeException").isAssignableFrom(
            ensureClass("maze.NoEntranceException")
        ));
    }

    @Test
    public void ensureNoExitExceptionIsInvalidMazeException() {
        assertTrue(ensureClass("maze.InvalidMazeException").isAssignableFrom(
            ensureClass("maze.NoExitException")
        ));
    }

}
