// Version 1.1, Wed 10th March 2021 @ 4:30pm
package tests.dev.functional;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.junit.Test;
import static org.junit.Assert.*;

import maze.Maze;
import maze.InvalidMazeException;
import maze.MultipleEntranceException;
import maze.MultipleExitException;
import maze.NoEntranceException;
import maze.NoExitException;
import maze.RaggedMazeException;
import maze.Tile;

public class MazeTest {

    private static Path WORKING_DIR = Paths.get("");
    private static Path TEST_MAZES_DIR = Paths.get(WORKING_DIR.toString(), "resources", "mazes");

    // ~~~~~~~~~~ Utility Functions ~~~~~~~~~~

    public boolean checkIfSetEntranceThrowsMultipleEntranceExceptionIfEntranceNotEmpty(Maze maze) {
        Tile newEntrance = fromChar('e');
        Class cls = setupForClassMembers();

        // Modify tiles to contain another entrance tile
        try {
            Field tilesAttribute = cls.getDeclaredField("tiles");
            tilesAttribute.setAccessible(true);
            List<List<Tile>> tiles = (List<List<Tile>>)tilesAttribute.get(maze);
            tiles.get(1).set(1, newEntrance);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": tiles");
        }

        // Try setting the new entrance tile as the entrance
        try {
            Method method = cls.getDeclaredMethod("setEntrance", Tile.class);
            method.setAccessible(true);
            method.invoke(maze, newEntrance);
        } catch (InvocationTargetException e) {
            assertSame(e.getCause().getClass(), MultipleEntranceException.class);
            return true; // return true only if MultipleEntranceException thrown
        } catch (NoSuchMethodException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": setEntrance");
        }
        return false; // no MultipleEntranceException thrown
    }

    public boolean checkIfSetExitThrowsMultipleExitExceptionIfExitNotEmpty(Maze maze) {
        Tile newExit = fromChar('x');
        Class cls = setupForClassMembers();

        // Modify tiles to contain another exit tile
        try {
            Field tilesAttribute = cls.getDeclaredField("tiles");
            tilesAttribute.setAccessible(true);
            List<List<Tile>> tiles = (List<List<Tile>>)tilesAttribute.get(maze);
            tiles.get(1).set(1, newExit);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": tiles");
        }

        // Try setting the new exit tile as the exit
        try {
            Method method = cls.getDeclaredMethod("setExit", Tile.class);
            method.setAccessible(true);
            method.invoke(maze, newExit);
        } catch (InvocationTargetException e) {
            assertSame(e.getCause().getClass(), MultipleExitException.class);
            return true; // return true only if MultipleExitException thrown
        } catch (NoSuchMethodException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": setExit");
        }
        return false; // no MultipleExitException thrown
    }

    public Tile fromChar(char c) {
        try {
            Class cls = Class.forName("maze.Tile");
            Method method = cls.getDeclaredMethod("fromChar", char.class);
            method.setAccessible(true);
            return (Tile)method.invoke(null, c);
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException: maze.Tile");
        } catch (NoSuchMethodException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": Tile.fromChar");
        } catch (InvocationTargetException e) {
          fail(
              e.getClass().getName() + ": Tile.fromChar\nCause: " +
              e.getCause().getClass() + ": " + e.getCause().getMessage()
          );
        }
        return null;
    }

    public Class setupForClassMembers() {
        try {
            return Class.forName("maze.Maze");
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException: maze.Maze");
        }
        return null;
    }

    public Maze setupForMaze1() {
        Maze rtn = null;
        Path filePath = Paths.get(TEST_MAZES_DIR.toString(), "maze1.txt");
        try {
            rtn = Maze.fromTxt(filePath.toString());
        } catch (Exception e) { fail(); }
        return rtn;
    }

    // ~~~~~~~~~~ Functionality tests ~~~~~~~~~~

    @Test
    public void ensureFromTxt() {
        setupForMaze1();
    }

    @Test
    public void ensureFromTxtWithInvalidCharThrowsInvalidMazeException() {
        Path filePath = Paths.get(TEST_MAZES_DIR.toString(), "invalid", "invalidChar.txt");
        try {
            Maze.fromTxt(filePath.toString());
        } catch (Exception e) {
            if (InvalidMazeException.class.isAssignableFrom(e.getClass())) {
                if (
                    e.getClass().equals(MultipleEntranceException.class) ||
                    e.getClass().equals(MultipleExitException.class) ||
                    e.getClass().equals(NoEntranceException.class) ||
                    e.getClass().equals(NoExitException.class) ||
                    e.getClass().equals(RaggedMazeException.class)
                ) {
                    fail(e.getClass().getName() + ": " +  e.getMessage());
                }
            } else {
                fail(e.getClass().getName() + ": " +  e.getMessage());
            }
        }
    }

    @Test
    public void ensureFromTxtWithRaggedMazeThrowsRaggedMazeException() {
        Path filePath = Paths.get(TEST_MAZES_DIR.toString(), "invalid", "ragged.txt");
        Exception exception = assertThrows(RaggedMazeException.class, () -> {
            Maze.fromTxt(filePath.toString());
        });
    }

    @Test
    public void ensureFromTxtWithNoEntranceThrowsNoEntranceException() {
        Path filePath = Paths.get(TEST_MAZES_DIR.toString(), "invalid", "noEntrance.txt");
        Exception exception = assertThrows(NoEntranceException.class, () -> {
            Maze.fromTxt(filePath.toString());
        });
    }

    @Test
    public void ensureFromTxtWithNoExitThrowsNoExitException() {
        Path filePath = Paths.get(TEST_MAZES_DIR.toString(), "invalid", "noExit.txt");
        Exception exception = assertThrows(NoExitException.class, () -> {
            Maze.fromTxt(filePath.toString());
        });
    }

    @Test
    public void ensureGetEntranceIsEntranceType() {
        Maze maze = setupForMaze1();
        try {
            assertSame(maze.getEntrance().getType(), Tile.Type.ENTRANCE);
        } catch (Exception e) {
            fail(e.getClass().getName() + ": " +  e.getMessage());
        }
    }

    @Test
    public void ensureGetExitIsExitType() {
        Maze maze = setupForMaze1();
        try {
            assertSame(maze.getExit().getType(), Tile.Type.EXIT);
        } catch (Exception e) {
            fail(e.getClass().getName() + ": " +  e.getMessage());
        }
    }

    @Test
    public void ensureGetTilesReturns2DList() {
        Maze maze = setupForMaze1();
        List<List<Tile>> tiles = maze.getTiles();
    }

    @Test
    public void ensureGetTilesReturnsCorrectDimensions() {
        Maze maze = setupForMaze1();
        List<List<Tile>> tiles = maze.getTiles();
        assertSame(tiles.size(), 6);
        for (List<Tile> row : tiles) { assertSame(row.size(), 6); }
    }

    @Test
    public void ensureSetEntranceOK() {
        Maze maze = setupForMaze1();
        Tile newEntrance = fromChar('e');
        Class cls = setupForClassMembers();
        Field entranceAttribute = null;

        // Modify entrance to be null
        try {
            entranceAttribute = cls.getDeclaredField("entrance");
            entranceAttribute.setAccessible(true);
            entranceAttribute.set(maze, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": entrance");
        }

        // Modify tiles to contain another entrance tile
        try {
            Field tilesAttribute = cls.getDeclaredField("tiles");
            tilesAttribute.setAccessible(true);
            List<List<Tile>> tiles = (List<List<Tile>>)tilesAttribute.get(maze);
            tiles.get(1).set(1, newEntrance);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": tiles");
        }

        // Try setting the new entrance tile as the entrance
        try {
            Method method = cls.getDeclaredMethod("setEntrance", Tile.class);
            method.setAccessible(true);
            method.invoke(maze, newEntrance);
        } catch (
            NoSuchMethodException | IllegalAccessException e
        ) {
            fail(e.getClass().getName() + ": setEntrance");
        } catch (InvocationTargetException e) {
            fail(
                e.getClass().getName() + ": setEntrance\nCause: " +
                e.getCause().getClass() + ": " + e.getCause().getMessage()
            );
        }

        // Check the new value of entrance
        try {
            assertSame((Tile)entranceAttribute.get(maze), newEntrance);
        } catch (IllegalAccessException e) {
            fail(e.getClass().getName() + ": entrance");
        }
    }

    @Test
    public void ensureSetEntranceFailsIfEntranceNotEmpty() {
        Maze maze = setupForMaze1();
        Class cls = setupForClassMembers();
        Field entranceAttribute = null;
        Tile origEntrance = null;

        // Save original entrance state
        try {
            entranceAttribute = cls.getDeclaredField("entrance");
            entranceAttribute.setAccessible(true);
            origEntrance = (Tile)entranceAttribute.get(maze);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": entrance");
        }

        checkIfSetEntranceThrowsMultipleEntranceExceptionIfEntranceNotEmpty(maze);

        // Compare the old and new values of entrance
        try {
            assertEquals((Tile)entranceAttribute.get(maze), origEntrance);
        } catch (IllegalAccessException e) {
            fail(e.getClass().getName() + ": entrance");
        }

    }

    @Test
    public void ensureSetEntranceThrowsMultipleEntranceExceptionIfEntranceNotEmpty() {
        Maze maze = setupForMaze1();
        assertTrue(checkIfSetEntranceThrowsMultipleEntranceExceptionIfEntranceNotEmpty(maze));
    }

    @Test
    public void ensureSetEntranceFailsIfTileNotInMaze() {
        Maze maze = setupForMaze1();
        Tile newEntrance = fromChar('e');
        Class cls = setupForClassMembers();
        Field entranceAttribute = null;

        // Modify entrance to be null
        try {
            entranceAttribute = cls.getDeclaredField("entrance");
            entranceAttribute.setAccessible(true);
            entranceAttribute.set(maze, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": entrance");
        }

        // Try setting the new entrance tile as the entrance
        try {
            Method method = cls.getDeclaredMethod("setEntrance", Tile.class);
            method.setAccessible(true);
            method.invoke(maze, newEntrance);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": setEntrance");

        // If an exception is thrown in the setEntrance method, it must be an
        // IllegalArgumentException (or something that extends IllegalArgumentException).
        } catch (InvocationTargetException e) {
            assertTrue(
                IllegalArgumentException.class.isAssignableFrom(e.getCause().getClass())
            );
        }

        // Compare the new value of entrance, make sure that setEntrance()
        // has had no effect.
        try {
            assertNotSame((Tile)entranceAttribute.get(maze), newEntrance);
        } catch (IllegalAccessException e) {
            fail(e.getClass().getName() + ": entrance");
        }
    }

    @Test
    public void ensureSetExitOK() {
        Maze maze = setupForMaze1();
        Tile newExit = fromChar('x');
        Class cls = setupForClassMembers();
        Field exitAttribute = null;

        // Modify exit to be null
        try {
            exitAttribute = cls.getDeclaredField("exit");
            exitAttribute.setAccessible(true);
            exitAttribute.set(maze, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": exit");
        }

        // Modify tiles to contain another exit tile
        try {
            Field tilesAttribute = cls.getDeclaredField("tiles");
            tilesAttribute.setAccessible(true);
            List<List<Tile>> tiles = (List<List<Tile>>)tilesAttribute.get(maze);
            tiles.get(1).set(1, newExit);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": tiles");
        }

        // Try setting the new exit tile as the exit
        try {
            Method method = cls.getDeclaredMethod("setExit", Tile.class);
            method.setAccessible(true);
            method.invoke(maze, newExit);
        } catch (
            NoSuchMethodException | IllegalAccessException e
        ) {
            fail(e.getClass().getName() + ": setExit");
        } catch (InvocationTargetException e) {
            fail(
                e.getClass().getName() + ": setExit\nCause: " +
                e.getCause().getClass() + ": " + e.getCause().getMessage()
            );
        }

        // Check the new value of exit
        try {
            assertSame((Tile)exitAttribute.get(maze), newExit);
        } catch (IllegalAccessException e) {
            fail(e.getClass().getName() + ": exit");
        }
    }

    @Test
    public void ensureSetExitFailsIfExitNotEmpty() {
        Maze maze = setupForMaze1();
        Class cls = setupForClassMembers();
        Field exitAttribute = null;
        Tile origExit = null;

        // Save original exit state
        try {
            exitAttribute = cls.getDeclaredField("exit");
            exitAttribute.setAccessible(true);
            origExit = (Tile)exitAttribute.get(maze);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": exit");
        }

        checkIfSetExitThrowsMultipleExitExceptionIfExitNotEmpty(maze);

        // Compare the old and new values of exit
        try {
            assertEquals((Tile)exitAttribute.get(maze), origExit);
        } catch (IllegalAccessException e) {
            fail(e.getClass().getName() + ": exit");
        }

    }

    @Test
    public void ensureSetExitThrowsMultipleExitExceptionIfExitNotEmpty() {
        Maze maze = setupForMaze1();
        assertTrue(checkIfSetExitThrowsMultipleExitExceptionIfExitNotEmpty(maze));
    }

    @Test
    public void ensureSetExitFailsIfTileNotInMaze() {
        Maze maze = setupForMaze1();
        Tile newExit = fromChar('x');
        Class cls = setupForClassMembers();
        Field exitAttribute = null;

        // Modify exit to be null
        try {
            exitAttribute = cls.getDeclaredField("exit");
            exitAttribute.setAccessible(true);
            exitAttribute.set(maze, null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": exit");
        }

        // Try setting the new exit tile as the exit
        try {
            Method method = cls.getDeclaredMethod("setExit", Tile.class);
            method.setAccessible(true);
            method.invoke(maze, newExit);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            fail(e.getClass().getName() + ": setExit");

        // If an exception is thrown in the setExit method, it must be an
        // IllegalArgumentException (or something that extends IllegalArgumentException).
        } catch (InvocationTargetException e) {
            assertTrue(
                IllegalArgumentException.class.isAssignableFrom(e.getCause().getClass())
            );
        }

        // Compare the new value of exit, make sure that setExit()
        // has had no effect.
        try {
            assertNotSame((Tile)exitAttribute.get(maze), newExit);
        } catch (IllegalAccessException e) {
            fail(e.getClass().getName() + ": exit");
        }
    }

    @Test
    public void ensureToStringMeetsMinimumDimensions() {
        Maze maze = setupForMaze1();
        String[] lines = maze.toString().split("\r\n|\r|\n");
        Set<Integer> set = new HashSet<Integer>();
        assertTrue(lines.length >= 6);
        for (int i=0; i < lines.length; i++) {
            if (lines[i].length() != 0) set.add(lines[i].length());
        }
        List<Integer> lst = set.stream().collect(Collectors.toList());
        assertTrue(set.size() <= 2);
        assertTrue(lst.get(0).intValue() >= 6);
    }

}
