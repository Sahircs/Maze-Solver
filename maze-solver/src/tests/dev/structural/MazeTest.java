// Version 1.1, Wednesday 10th March 2021 @ 3:35pm
package tests.dev.structural;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

import org.junit.Test;
import static org.junit.Assert.*;

public class MazeTest {

    // ~~~~~~~~~~ Utility Functions ~~~~~~~~~~

    public Class setupForClassMembers() {
        return ensureClass("maze.Maze");
    }

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
    public void ensureEntranceAttributeIsTileType() {
        Class cls = setupForClassMembers();
        try {
    	      Field entranceAttribute = cls.getDeclaredField("entrance");
            assertSame(entranceAttribute.getType(), Class.forName("maze.Tile"));
        } catch (NoSuchFieldException e) {
            fail("NoSuchFieldException: entrance");
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException: maze.Tile");
        }
    }

    @Test
    public void ensureExitAttributeIsTileType() {
        Class cls = setupForClassMembers();
        try {
    	      Field exitAttribute = cls.getDeclaredField("exit");
            assertSame(exitAttribute.getType(), Class.forName("maze.Tile"));
        } catch (NoSuchFieldException e) {
            fail("NoSuchFieldException: exit");
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException: maze.Tile");
        }
    }

    @Test
    public void ensureTilesAttributeIsListType() {
        Class cls = setupForClassMembers();
        try {
    	      Field tilesAttribute = cls.getDeclaredField("tiles");
            assertSame(tilesAttribute.getType(), List.class);
        } catch (NoSuchFieldException e) {
            fail("NoSuchFieldException: tiles");
        }
    }

    @Test
    public void ensureOnlyPrivateConstructors() {
        Class cls = setupForClassMembers();
        Constructor[] allConstructors = cls.getDeclaredConstructors();
        for (Constructor constructor : allConstructors) {
            assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        }
        assertEquals(cls.getConstructors().length, 0);
    }

    @Test
    public void ensurePrivateConstructorNoArguments() {
        Class cls = setupForClassMembers();
        try {
    	      Constructor constructor = cls.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            fail("No constructor with signature: Maze()");
        }
    }

    @Test
    public void ensureFromTxtReturnsMaze() {
        boolean declared = false;
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("fromTxt", String.class);
            assertSame(method.getReturnType(), cls);
        } catch (NoSuchMethodException e) {
            fail("No method with signature: fromTxt(String)");
        }
    }

    @Test
    public void ensurePublicMethodGetEntrance() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getMethod("getEntrance");
        } catch (NoSuchMethodException e) {
            fail("No public method with signature: getEntrance()");
        }
    }

    @Test
    public void ensureGetEntranceNoArguments() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("getEntrance");
        } catch (NoSuchMethodException e) {
            fail("No method with signature: getEntrance()");
        }
    }

    @Test
    public void ensureGetEntranceReturnsTile() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("getEntrance");
            assertSame(method.getReturnType(), ensureClass("maze.Tile"));
        } catch (NoSuchMethodException e) {
            fail("No method with signature: getEntrance()");
        }
    }

    @Test
    public void ensurePublicMethodGetExit() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getMethod("getExit");
        } catch (NoSuchMethodException e) {
            fail("No public method with signature: getExit()");
        }
    }

    @Test
    public void ensureGetExitNoArguments() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("getExit");
        } catch (NoSuchMethodException e) {
            fail("No method with signature: getExit()");
        }
    }

    @Test
    public void ensureGetExitReturnsTile() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("getExit");
            assertSame(method.getReturnType(), ensureClass("maze.Tile"));
        } catch (NoSuchMethodException e) {
            fail("No method with signature: getExit()");
        }
    }

    @Test
    public void ensurePublicMethodGetTiles() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getMethod("getTiles");
        } catch (NoSuchMethodException e) {
            fail("No public method with signature: getTiles()");
        }
    }

    @Test
    public void ensureGetTilesNoArguments() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("getTiles");
        } catch (NoSuchMethodException e) {
            fail("No method with signature: getTiles()");
        }
    }

    @Test
    public void ensurePrivateMethodSetEntrance() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("setEntrance", ensureClass("maze.Tile"));
            assertTrue(Modifier.isPrivate(method.getModifiers()));
        } catch (NoSuchMethodException e) {
            fail("No method with signature: setEntrance()");
        }
    }

    @Test
    public void ensureSetEntranceReturnsVoid() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("setEntrance", ensureClass("maze.Tile"));
            assertSame(method.getReturnType(), Void.TYPE);
        } catch (NoSuchMethodException e) {
            fail("No method with signature: setEntrance()");
        }
    }

    @Test
    public void ensurePrivateMethodSetExit() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("setExit", ensureClass("maze.Tile"));
            assertTrue(Modifier.isPrivate(method.getModifiers()));
        } catch (NoSuchMethodException e) {
            fail("No method with signature: setExit()");
        }
    }

    @Test
    public void ensureSetExitReturnsVoid() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("setExit", ensureClass("maze.Tile"));
            assertSame(method.getReturnType(), Void.TYPE);
        } catch (NoSuchMethodException e) {
            fail("No method with signature: setExit()");
        }
    }

    @Test
    public void ensurePublicMethodToString() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getMethod("toString");
            assertSame(method.getDeclaringClass(), cls);
        } catch (NoSuchMethodException e) {
            fail("No public method with signature: toString()");
        }
    }

    @Test
    public void ensureToStringNoArguments() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("toString");
            assertSame(method.getDeclaringClass(), cls);
        } catch (NoSuchMethodException e) {
            fail("No method with signature: toString()");
        }
    }

    @Test
    public void ensureToStringReturnsString() {
        Class cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("toString");
            assertSame(method.getDeclaringClass(), cls);
            assertSame(method.getReturnType(), String.class);
        } catch (NoSuchMethodException e) {
            fail("No method with signature: toString()");
        }
    }

}
