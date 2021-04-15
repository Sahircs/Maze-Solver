// Version 1.1, Wed 10th March 2021 @ 3:35pm
package tests.dev.functional;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;
import static org.junit.Assert.*;

import maze.Tile;
import maze.InvalidMazeException;
import maze.NoEntranceException;
import maze.NoExitException;
import maze.MultipleEntranceException;
import maze.MultipleExitException;
import maze.RaggedMazeException;

public class TileTest {

    // ~~~~~~~~~~ Utility Functions ~~~~~~~~~~

    public Object constructTypeObject(String typeValue) {
        Class<?> cls = setupForClassMembers();
        try {
    	      Constructor<?> constructor = cls.getDeclaredConstructor(
                setupForInnerClassMembers()
            );
            constructor.setAccessible(true);
            return constructor.newInstance(Tile.Type.valueOf(typeValue));
        } catch (
            NoSuchMethodException | InstantiationException |
            IllegalAccessException | InvocationTargetException e
        ) {
            fail(e.getClass().getName() + ": Tile(Type)");
        }
        return null;
    }

    public Tile fromChar(char c) throws InvalidMazeException {
        try {
            Method method = setupForFromChar();
            return (Tile)method.invoke(null, c);
        } catch (IllegalAccessException e) {
            fail("IllegalAccessException: Tile.fromChar");
        } catch (InvocationTargetException e) {
            if (e.getCause().getClass().equals(IllegalArgumentException.class)) {
                throw (IllegalArgumentException)e.getCause();
            } else if (InvalidMazeException.class.isAssignableFrom(e.getCause().getClass())) {
                if (
                    !e.getCause().getClass().equals(InvalidMazeException.class) &&
                    !e.getCause().getClass().equals(MultipleEntranceException.class) &&
                    !e.getCause().getClass().equals(MultipleExitException.class) &&
                    !e.getCause().getClass().equals(NoEntranceException.class) &&
                    !e.getCause().getClass().equals(NoExitException.class) &&
                    !e.getCause().getClass().equals(RaggedMazeException.class)
                ) {
                    throw (InvalidMazeException)e.getCause();
                }
            }
            fail("InvocationTargetException caused by " + e.getCause().getClass() + ": Tile.fromChar");
        }
        return null;
    }

    public Class<?> setupForClassMembers() {
        try {
            return Class.forName("maze.Tile");
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException: maze.Tile");
        }
        return null;
    }

    public Method setupForFromChar() {
        Class<?> cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("fromChar", char.class);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException: Tile.fromChar");
        }
        return null;
    }

    public Class<?> setupForInnerClassMembers() {
        Class<?> cls = setupForClassMembers();
        for (Class<?> innerClass: cls.getDeclaredClasses()) {
            if (innerClass.getName().equals("maze.Tile$Type")) {
                return innerClass;
            }
        }
        fail("ClassNotFoundException: maze.Tile$Type");
        return null;
    }


    // ~~~~~~~~~~ Functionality tests ~~~~~~~~~~

    @Test
    public void ensureConstructorEntrance() {
        constructTypeObject("ENTRANCE");
    }

    @Test
    public void ensureConstructorWall() {
        constructTypeObject("WALL");
    }

    @Test
    public void ensureFromCharCorridorType() {
        try {
            Tile tile = fromChar('.');
            assertEquals(tile.getType(), Tile.Type.valueOf("CORRIDOR"));
        } catch (InvalidMazeException e) { fail(); }
    }

    @Test
    public void ensureFromCharEntranceType() {
        try {
            Tile tile = fromChar('e');
            assertEquals(tile.getType(), Tile.Type.valueOf("ENTRANCE"));
        } catch (InvalidMazeException e) { fail(); }
    }

    @Test
    public void ensureFromCharExitType() {
        try {
            Tile tile = fromChar('x');
            assertEquals(tile.getType(), Tile.Type.valueOf("EXIT"));
        } catch (InvalidMazeException e) { fail(); }
    }

    @Test
    public void ensureFromCharWallType() {
        try {
            Tile tile = fromChar('#');
            assertEquals(tile.getType(), Tile.Type.valueOf("WALL"));
        } catch (InvalidMazeException e) { fail(); }
    }

    @Test
    public void ensureGetTypeReturnsTypeVariable() {
        try {
            Tile tile = fromChar('#');
            Class<?> cls = setupForClassMembers();
    	      Field typeAttribute = cls.getDeclaredField("type");
            typeAttribute.setAccessible(true);
            assertSame(typeAttribute.get(tile), tile.getType());
        } catch (NoSuchFieldException | IllegalAccessException | InvalidMazeException e) {
            fail(e.getClass().getName() + ": type");
        }
    }

    @Test
    public void ensureCorridorNavigable() {
        try {
            Tile tile = fromChar('.');
            assertTrue(tile.isNavigable());
        } catch (InvalidMazeException e) { fail(); }
    }

    @Test
    public void ensureEntranceNavigable() {
        try {
            Tile tile = fromChar('e');
            assertTrue(tile.isNavigable());
        } catch (InvalidMazeException e) { fail(); }
    }

    @Test
    public void ensureExitNavigable() {
        try {
            Tile tile = fromChar('x');
            assertTrue(tile.isNavigable());
        } catch (InvalidMazeException e) { fail(); }
    }

    @Test
    public void ensureWallNotNavigable() {
        try {
            Tile tile = fromChar('#');
            assertFalse(tile.isNavigable());
        } catch (InvalidMazeException e) { fail(); }
    }

}
