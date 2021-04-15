// Version 1.1, Wednesday 10th March 2021 @ 3:35pm
package tests.dev.structural;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;
import static org.junit.Assert.*;

public class TileTest {

    // ~~~~~~~~~~ Utility Functions ~~~~~~~~~~
    public Class<?> setupForClassMembers() {
        try {
            return Class.forName("maze.Tile");
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException: maze.Tile");
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

    // ~~~~~~~~~~ Structural tests ~~~~~~~~~~

    @Test
    public void ensureTypeAttributeIsTileType() {
        Class<?> cls = setupForClassMembers();
        try {
    	      Field typeAttribute = cls.getDeclaredField("type");
            assertSame(typeAttribute.getType(), setupForInnerClassMembers());
        } catch (NoSuchFieldException e) {
            fail("NoSuchFieldException: type");
        }
    }

    @Test
    public void ensureConstructorArgumentIsTileType() {
        Class<?> cls = setupForClassMembers();
        try {
    	      Constructor<?> constructor = cls.getDeclaredConstructor(
                setupForInnerClassMembers()
            );
        } catch (NoSuchMethodException e) {
            fail("No constructor with signature: Tile(Type)");
        }
    }

    @Test
    public void ensureFromCharArgumentIsChar() {
        Class<?> cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("fromChar", char.class);
        } catch (NoSuchMethodException e) {
            fail("No method with signature: fromChar(char)");
        }
    }

    @Test
    public void ensureFromCharReturnsTile() {
        Class<?> cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("fromChar", char.class);
            assertSame(method.getReturnType(), cls);
        } catch (NoSuchMethodException e) {
            fail("No method with signature: fromChar(char): Tile");
        }
    }

    @Test
    public void ensurePublicMethodIsNavigable() {
        Class<?> cls = setupForClassMembers();
        try {
            Method method = cls.getMethod("isNavigable");
        } catch (NoSuchMethodException e) {
            fail("No public method with signature: isNavigable()");
        }
    }

    @Test
    public void ensureIsNavigableNoArguments() {
        Class<?> cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("isNavigable");
        } catch (NoSuchMethodException e) {
            fail("No method with signature: isNavigable()");
        }
    }

    @Test
    public void ensureIsNavigableReturnsBoolean() {
        Class<?> cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("isNavigable");
            assertSame(method.getReturnType(), boolean.class);
        } catch (NoSuchMethodException e) {
            fail("No method with signature: isNavigable(): boolean");
        }
    }

    @Test
    public void ensurePublicMethodToString() {
        Class<?> cls = setupForClassMembers();
        try {
            Method method = cls.getMethod("toString");
            assertSame(method.getDeclaringClass(), cls);
        } catch (NoSuchMethodException e) {
            fail("No public method with signature: toString()");
        }
    }

    @Test
    public void ensureToStringNoArguments() {
        Class<?> cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("toString");
            assertSame(method.getDeclaringClass(), cls);
        } catch (NoSuchMethodException e) {
            fail("No method with signature: toString()");
        }
    }

    @Test
    public void ensureToStringReturnsString() {
        Class<?> cls = setupForClassMembers();
        try {
            Method method = cls.getDeclaredMethod("toString");
            assertSame(method.getDeclaringClass(), cls);
            assertSame(method.getReturnType(), String.class);
        } catch (NoSuchMethodException e) {
            fail("No method with signature: toString(): String");
        }
    }

}
