// Version 1.1, Wednesday 10th March 2021 @ 3:35pm
package tests.dev.structural;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.EOFException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import static org.junit.Assert.*;

public class RouteFinderTest {

    // ~~~~~~~~~~ Utility Functions ~~~~~~~~~~
    public Method ensurePublicMethodStringArg(String methodName) {
        Class cls = setupForClassMembers();
        try {
            return cls.getMethod(methodName, String.class);
        } catch (NoSuchMethodException e) {
          fail(String.format("No public method with signature: %s(String)", methodName));
        }
        return null;
    }

    public Method ensureMethodStringArg(String methodName) {
        Class cls = setupForClassMembers();
        try {
            return cls.getDeclaredMethod(methodName, String.class);
        } catch (NoSuchMethodException e) {
          fail(String.format("No public method with signature: %s(String)", methodName));
        }
        return null;
    }

    public Method ensurePublicMethodNoArgs(String methodName) {
        Class cls = setupForClassMembers();
        try {
            return cls.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            fail(String.format("No public method with signature: %s()", methodName));
        }
        return null;
    }

    public Method ensureMethodNoArgs(String methodName) {
        Class cls = setupForClassMembers();
        try {
            return cls.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            fail(String.format("No method with signature: %s()", methodName));
        }
        return null;
    }

    public Class ensureClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException: " + className);
        }
        return null;
    }

    public Class setupForClassMembers() {
        return ensureClass("maze.routing.RouteFinder");
    }

    // ~~~~~~~~~~ Structural tests ~~~~~~~~~~

    @Test
    public void ensureConstructorArgumentIsMaze() {
        Class cls = setupForClassMembers();
        try {
            Constructor<?> constructor = cls.getDeclaredConstructor(ensureClass("maze.Maze"));
        } catch (NoSuchMethodException e) {
            fail("No constructor with signature: RouteFinder(Maze)");
        }
    }

    @Test
    public void ensurePublicMethodGetMaze() {
        ensurePublicMethodNoArgs("getMaze");
    }

    @Test
    public void ensureGetMazeNoArguments() {
        ensureMethodNoArgs("getMaze");
    }

    @Test
    public void ensureGetMazeReturnsMaze() {
        Method method = ensureMethodNoArgs("getMaze");
        assertSame(method.getReturnType(), ensureClass("maze.Maze"));
    }

    @Test
    public void ensurePublicMethodGetRoute() {
        ensurePublicMethodNoArgs("getRoute");
    }

    @Test
    public void ensureGetRouteNoArguments() {
        ensureMethodNoArgs("getRoute");
    }

    @Test
    public void ensureGetRouteReturnsList() {
        Method method = ensureMethodNoArgs("getRoute");
        assertTrue(method.getReturnType().isAssignableFrom(
            List.class
        ));
    }

    @Test
    public void ensurePublicMethodIsFinished() {
        ensurePublicMethodNoArgs("isFinished");
    }

    @Test
    public void ensureIsFinishedNoArguments() {
        ensureMethodNoArgs("isFinished");
    }

    @Test
    public void ensureIsFinishedReturnsBoolean() {
        Method method = ensureMethodNoArgs("isFinished");
        assertSame(method.getReturnType(), boolean.class);
    }

    @Test
    public void ensurePublicMethodLoad() {
        ensurePublicMethodStringArg("load");
    }

    @Test
    public void ensureLoadArgumentIsString() {
        ensureMethodStringArg("load");
    }

    @Test
    public void ensureLoadReturnsRouteFinder() {
        Class cls = setupForClassMembers();
        Method method = ensureMethodStringArg("load");
        assertSame(method.getReturnType(), cls);
    }

    @Test
    public void ensurePublicMethodSave() {
        ensurePublicMethodStringArg("save");
    }

    @Test
    public void ensureSaveArgumentIsString() {
        ensureMethodStringArg("save");
    }

    @Test
    public void ensureSaveReturnsVoid() {
        Method method = ensureMethodStringArg("save");
        assertSame(method.getReturnType(), Void.TYPE);
    }

    @Test
    public void ensurePublicMethodStep() {
        ensurePublicMethodNoArgs("step");
    }

    @Test
    public void ensureStepNoArguments() {
        ensureMethodNoArgs("step");
    }

    @Test
    public void ensureStepReturnsBoolean() {
        Method method = ensureMethodNoArgs("step");
        assertSame(method.getReturnType(), boolean.class);
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
