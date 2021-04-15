// Version 1.1, Wednesday 10th March 2021 @ 3:35pm
package tests.dev.structural;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.Test;
import static org.junit.Assert.*;

public class DirectionTest {


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
    public void ensureEnum() {
        for (Class<?> cls: ensureClass("maze.Maze").getDeclaredClasses()) {
            if (cls.getName().equals("maze.Maze$Direction")) {
                assertTrue(cls.isEnum());
                return;
            }
        }
        fail();
    }

}
