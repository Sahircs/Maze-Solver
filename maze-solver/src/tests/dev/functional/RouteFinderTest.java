// Version 1.1, Wed 10th March 2021 @ 4:20pm
package tests.dev.functional;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.EOFException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;
import static org.junit.Assert.*;

import maze.Maze;
import maze.Tile;
import maze.routing.RouteFinder;

public class RouteFinderTest {

    private static Path WORKING_DIR = Paths.get("");
    private static Path TEST_MAZES_DIR = Paths.get(WORKING_DIR.toString(), "resources", "mazes");
    private static Path TEST_ROUTES_DIR = Paths.get(WORKING_DIR.toString(), "resources", "routes");

    // ~~~~~~~~~~ Utility Functions ~~~~~~~~~~
    public static Pair<String> diff(String a, String b) {
         return diffHelper(a, b, new HashMap<>());
    }

    private static Pair<String> diffHelper(
        String a, String b, Map<Long, Pair<String>> lookup
    ) {
        long key = ((long) a.length()) << 32 | b.length();
        if (!lookup.containsKey(key)) {
            Pair<String> value;
            if (a.isEmpty() || b.isEmpty()) {
                value = new Pair<>(a, b);
            } else if (a.charAt(0) == b.charAt(0)) {
                value = diffHelper(a.substring(1), b.substring(1), lookup);
            } else {
                Pair<String> aa = diffHelper(a.substring(1), b, lookup);
                Pair<String> bb = diffHelper(a, b.substring(1), lookup);
                if (aa.first.length() + aa.second.length() < bb.first.length() + bb.second.length()) {
                    value = new Pair<>(a.charAt(0) + aa.first, aa.second);
                } else {
                    value = new Pair<>(bb.first, b.charAt(0) + bb.second);
                }
            }
            lookup.put(key, value);
        }
        return lookup.get(key);
    }

    public static class Pair<T> {
        public final T first, second;
        public Pair(T first, T second) {
            this.first = first;
            this.second = second;
        }
        public String toString() {
            return "(" + this.first + "," + this.second + ")";
        }
    }

    private void ensureRoute(RouteFinder route, int length) {
        assertTrue(route.isFinished());
        List<Tile> tiles = route.getRoute();
        assertSame(tiles.size(), length);
        assertSame(route.getRoute().get(0).getType(), Tile.Type.ENTRANCE);
        assertSame(route.getRoute().get(length-1).getType(), Tile.Type.EXIT);
        for (Tile tile: tiles.subList(1, length-1)) {
            assertSame(tile.getType(), Tile.Type.CORRIDOR);
        }
    }

    private RouteFinder ensureMaze1SaveWritesFile(String filename, int steps) {
        RouteFinder orig = setupForMaze1();

        // Don't use a while here just in case the return value is broken
        // We know this maze is solved in a max of ~22 steps.
        if (steps == -1) steps = 25;

        try {
            for (int i = 0; i<steps; ++i) if (orig.step()) break;
            orig.save(filename);
        } catch (Exception e) {
            fail(e.getClass().getName() + ": " +  e.getMessage());
        }
        File saved = new File(filename);
        assertTrue(saved.exists());
        assertTrue(saved.isFile());
        return orig;
    }

    public void assertRoutesAreEqual(RouteFinder r1, RouteFinder r2) {
        assertEquals(r1.toString(), r2.toString());
        assertSame(r1.isFinished(), r2.isFinished());
        assertMazesAreEqual(r1.getMaze(), r2.getMaze());
        List<Tile> l1 = r1.getRoute();
        List<Tile> l2 = r2.getRoute();
        assertSame(l1.size(), l2.size());
        for (int i = 0; i < l1.size(); i++) {
            assertSame(l1.get(i).getType(), l2.get(i).getType());
        }
    }

    public void assertMazesAreEqual(Maze m1, Maze m2) {
        List<List<Tile>> t1 = m1.getTiles();
        List<List<Tile>> t2 = m2.getTiles();
        assertSame(t1.size(), t2.size());
        for (int i = 0; i < t1.size(); i++) {
            List<Tile> r1 = t1.get(i);
            List<Tile> r2 = t2.get(i);
            assertSame(r1.size(), r2.size());
            for (int j = 0; j < r1.size(); j++) {
                assertSame(r1.get(j).getType(), r2.get(j).getType());
            }
        }
    }
    
    public Class setupForClassMembers() {
        String className = "maze.routing.RouteFinder";
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            fail("ClassNotFoundException: " + className);
        }
        return null;
    }

    public RouteFinder setupForMaze(Maze maze) {
        return new RouteFinder(maze);
    }

    public RouteFinder setupForMaze(String filename) {
        Maze maze = null;
        try {
            maze = Maze.fromTxt(filename);
        } catch (Exception e) {
            fail(e.getClass().getName() + ": " +  e.getMessage());
        }
        return setupForMaze(maze);
    }

    public RouteFinder setupForMaze1() {
        Path filePath = Paths.get(TEST_MAZES_DIR.toString(), "maze1.txt");
        return setupForMaze(filePath.toString());
    }

    public RouteFinder setupForMaze2() {
        Path filePath = Paths.get(TEST_MAZES_DIR.toString(), "maze2.txt");
        return setupForMaze(filePath.toString());
    }

    // ~~~~~~~~~~ Functional tests ~~~~~~~~~~

    @Test
    public void ensureGetMaze() {
        Class cls = setupForClassMembers();
        RouteFinder routeFinder = setupForMaze1();
        try {
            Field maze = cls.getDeclaredField("maze");
            maze.setAccessible(true);
            assertSame(routeFinder.getMaze(), maze.get(routeFinder));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            fail(e.getClass().getName() + ": routeFinder.maze");
        }
    }

    @Test
    public void ensureIsFinished() {
        Class cls = setupForClassMembers();
        RouteFinder routeFinder = setupForMaze1();
        try {
            Field field = cls.getDeclaredField("finished");
            field.setAccessible(true);
            assertSame(routeFinder.isFinished(), field.get(routeFinder));
            field.set(routeFinder, true);
            assertSame(routeFinder.isFinished(), field.get(routeFinder));
        } catch (IllegalAccessException | NoSuchFieldException  e) {
            fail(e.getClass().getName() + ": routeFinder.finishd");
        }
    }

    @Test
    public void ensureToStringMeetsMinimumDimensions() {
        RouteFinder routeFinder = setupForMaze1();
        try {
            routeFinder.step(); routeFinder.step();
        } catch (Exception e) {
            fail(e.getClass().getName() + ": " + e.getMessage());
        }
        String[] lines = routeFinder.toString().split("\r\n|\r|\n");
        Set<Integer> set = new HashSet<Integer>();
        assertTrue(lines.length >= 6);
        for (int i=0; i < lines.length; i++) {
            if (lines[i].length() != 0) set.add(lines[i].length());
        }
        List<Integer> lst = set.stream().collect(Collectors.toList());
        assertTrue(set.size() <= 2);
        assertTrue(lst.get(0).intValue() >= 6);
    }

    @Test
    public void ensureLoadRestoresStartState() {
        Path filePath = Paths.get(TEST_ROUTES_DIR.toString(), "route1_initialState.route");
        String filename = filePath.toString();
        RouteFinder orig = ensureMaze1SaveWritesFile(filename, 0);
        RouteFinder loaded = null;
        try {
            loaded = RouteFinder.load(filename);
        } catch (Exception e) {
            fail(e.getClass().getName() + ": " + e.getMessage());
        }
        assertRoutesAreEqual(orig, loaded);
    }

    @Test
    public void ensureLoadRestoresEndState() {
        Path filePath = Paths.get(TEST_ROUTES_DIR.toString(), "route1_endState.route");
        String filename = filePath.toString();
        RouteFinder orig = ensureMaze1SaveWritesFile(filename, -1);
        RouteFinder loaded = null;
        try {
            loaded = RouteFinder.load(filename);
        } catch (Exception e) {
            fail(e.getClass().getName() + ": " + e.getMessage());
        }
        assertRoutesAreEqual(orig, loaded);
    }

    @Test
    public void ensureLoadRestoresMidState() {
        Path filePath = Paths.get(TEST_ROUTES_DIR.toString(), "route1_endState.route");
        String filename = filePath.toString();
        RouteFinder orig = ensureMaze1SaveWritesFile(filename, 7);
        RouteFinder loaded = null;
        try {
            loaded = RouteFinder.load(filename);
        } catch (Exception e) {
            fail(e.getClass().getName() + ": " + e.getMessage());
        }
        assertRoutesAreEqual(orig, loaded);
    }

    @Test
    public void ensureLoadErrorsWithFileNotFound() {
        Path filePath = Paths.get(TEST_ROUTES_DIR.toString(), "thisisnotaroute.route");
        String filename = filePath.toString();
        RouteFinder loaded = null;
        try {
            loaded = RouteFinder.load(filename);
        } catch (Exception e) {
            if (!e.getClass().equals(FileNotFoundException.class)) {
                fail(e.getClass().getName() + ": " + e.getMessage());
            }
        }
        assertNull(loaded);
    }

    @Test
    public void ensureLoadErrorsWithEmptyFile() {
        Path filePath = Paths.get(TEST_ROUTES_DIR.toString(), "invalid", "empty.route");
        String filename = filePath.toString();
        RouteFinder loaded = null;
        try {
            loaded = RouteFinder.load(filename);
        } catch (Exception e) {
          if (!e.getClass().equals(EOFException.class)) {
              fail(e.getClass().getName() + ": " + e.getMessage());
          }
        }
        assertNull(loaded);
    }

    @Test
    public void ensureSaveWritesFile() {
        Path filePath = Paths.get(TEST_ROUTES_DIR.toString(), "ensureSaveWritesFile.route");
        ensureMaze1SaveWritesFile(filePath.toString(), 3);
    }

    @Test
    public void ensureSavedFileCanBeLoaded() {
        Path filePath = Paths.get(TEST_ROUTES_DIR.toString(), "ensureSavedFileCanBeLoaded.route");
        String filename = filePath.toString();
        ensureMaze1SaveWritesFile(filename, 3);
        RouteFinder loaded = null;
        try {
            loaded = RouteFinder.load(filename);
        } catch (Exception e) {
            fail(e.getClass().getName() + ": " + e.getMessage());
        }
        assertTrue(loaded != null);
    }

    @Test
    public void ensureStepOnlyTakesOneAction() {
        RouteFinder route = setupForMaze1();
        try {
            for (int i = 0; i<3; ++i) route.step();
            String s = route.toString();
            int routeSize = route.getRoute().size();
            route.step();
            assertSame(1, Math.abs(routeSize - route.getRoute().size()));
            Pair p = diff(s, route.toString());
            assertSame(1, ((String)p.first).length());
            assertSame(1, ((String)p.second).length());
        } catch (Exception e) {
            fail(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Test
    public void ensureStepDetectsSetsFinished() {
        RouteFinder route = setupForMaze1();

        // This maze should easily be solved in <25 steps
        try {
            for (int i = 0; i<25; ++i) if (route.step()) break;
        } catch (Exception e) {
            fail(e.getClass().getName() + ": " + e.getMessage());
        }

        assertTrue(route.isFinished());
    }

    @Test
    public void ensureStepCanReturnTrue() {
        RouteFinder route = setupForMaze1();

        // This maze should easily be solved in <25 steps
        try {
            for (int i = 0; i<25; ++i) if (route.step()) return;
        } catch (Exception e) {
            fail(e.getClass().getName() + ": " + e.getMessage());
        }

        fail("step() through maze 1 returns false after 25 steps.");
    }

    @Test
    public void ensureStepCanReturnFalse() {
        RouteFinder route = setupForMaze1();
        try {
            assertFalse(route.step());
        } catch (Exception e) {
            fail(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    @Test
    public void ensureStepOnceFinishedDoesNotAlterRoute() {
        RouteFinder route = setupForMaze1();

        // This maze should easily be solved in <25 steps
        try {
            for (int i = 0; i<25; ++i) if (route.step()) break;
        } catch (Exception e) {
            fail(e.getClass().getName() + ": " + e.getMessage());
        }
        String s = route.toString();
        int routeSize = route.getRoute().size();

        try {
            route.step();
        } catch (Exception e) {
            fail(e.getClass().getName() + ": " + e.getMessage());
        }
        assertSame(routeSize, route.getRoute().size());
        Pair p = diff(s, route.toString());
        assertSame(0, ((String)p.first).length());
        assertSame(0, ((String)p.second).length());
    }

    @Test
    public void ensureStepSolvesMaze1() {
        RouteFinder route = setupForMaze1();

        // This maze should easily be solved in <25 steps
        try {
            for (int i = 0; i<25; ++i) if (route.step()) break;
        } catch (Exception e) {
            fail(e.getClass().getName() + ": " + e.getMessage());
        }

        ensureRoute(route, 12);
    }

    @Test
    public void ensureStepSolvesMaze2() {
        RouteFinder route = setupForMaze2();

        // This maze should easily be solved in <85 steps
        try {
            for (int i = 0; i<85; ++i) if (route.step()) break;
        } catch (Exception e) {
            fail(e.getClass().getName() + ": " + e.getMessage());
        }

        assertTrue(route.isFinished());
        List<Tile> tiles = route.getRoute();
        assertTrue(tiles.size() == 19 || tiles.size() == 23);
        assertSame(route.getRoute().get(0).getType(), Tile.Type.ENTRANCE);
        assertSame(route.getRoute().get(tiles.size()-1).getType(), Tile.Type.EXIT);
        for (Tile tile: tiles.subList(1, tiles.size()-1)) {
            assertSame(tile.getType(), Tile.Type.CORRIDOR);
        }
    }

}
