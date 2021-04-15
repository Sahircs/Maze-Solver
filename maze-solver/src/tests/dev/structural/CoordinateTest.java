// Version 1.1, Wednesday 10th March 2021 @ 3:35pm
package tests.dev.structural;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.Test;
import static org.junit.Assert.*;

public class CoordinateTest {

    // ~~~~~~~~~~ Utility Functions ~~~~~~~~~~

    public Class<?> setupForClassMembers() {
        try {
            Class<?> cls = Class.forName("maze.Maze");
            for (Class<?> innerClass: cls.getDeclaredClasses()) {
                if (innerClass.getName().equals("maze.Maze$Coordinate")) {
                    return innerClass;
                }
            }
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException: maze.Maze");
        }

        fail("ClassNotFoundException: maze.Maze$Coordinate");
        return null;
    }

    // ~~~~~~~~~~ Structural tests ~~~~~~~~~~

    @Test
    public void ensureIntAttributeX() {
        Class<?> cls = setupForClassMembers();
        try {
    	      Field typeAttribute = cls.getDeclaredField("x");
            assertSame(typeAttribute.getType(), int.class);
        } catch (NoSuchFieldException e) {
            fail("NoSuchFieldException: x");
        }
    }

    @Test
    public void ensureIntAttributeY() {
        Class<?> cls = setupForClassMembers();
        try {
    	      Field typeAttribute = cls.getDeclaredField("y");
            assertSame(typeAttribute.getType(), int.class);
        } catch (NoSuchFieldException e) {
            fail("NoSuchFieldException: y");
        }
    }

    @Test
    public void ensurePublicMethodGetX() {
        Class<?> cls = setupForClassMembers();
        try {
            Method method = cls.getMethod("getX");
        } catch (NoSuchMethodException e) {
            fail("No public method with signature: getX()");
        }
    }

    @Test
    public void ensureGetXNoArguments() {
        Class<?> cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("getX");
        } catch (NoSuchMethodException e) {
            fail("No method with signature: getX()");
        }
    }

    @Test
    public void ensureGetXReturnsInt() {
        Class<?> cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("getX");
            assertSame(method.getReturnType(), int.class);
        } catch (NoSuchMethodException e) {
            fail("No method with signature: getX(): int");
        }
    }

    @Test
    public void ensurePublicMethodGetY() {
        Class<?> cls = setupForClassMembers();
        try {
            Method method = cls.getMethod("getY");
        } catch (NoSuchMethodException e) {
            fail("No public method with signature: getY()");
        }
    }

    @Test
    public void ensureGetYNoArguments() {
        Class<?> cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("getY");
        } catch (NoSuchMethodException e) {
            fail("No method with signature: getY()");
        }
    }

    @Test
    public void ensureGetYReturnsInt() {
        Class<?> cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("getY");
            assertSame(method.getReturnType(), int.class);
        } catch (NoSuchMethodException e) {
            fail("No method with signature: getY(): int");
        }
    }

}
