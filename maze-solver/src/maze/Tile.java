package maze;

import java.util.Map;
import java.util.UUID;
import java.util.Set;
import java.io.Serializable;
import java.util.HashSet;

public class Tile implements Serializable {
    private Type type;
    public static String typeStringRepresentation;
    public static Map<Character, String> typeCharMap = Map.ofEntries(
        Map.entry('.', Type.CORRIDOR.toString()),
        Map.entry('e', Type.ENTRANCE.toString()),
        Map.entry('x', Type.EXIT.toString()),
        Map.entry('#', Type.WALL.toString())
    );
    public static Map<String, String> typeToStringMap = Map.ofEntries(
        Map.entry(Type.CORRIDOR.toString(), "."),
        Map.entry(Type.ENTRANCE.toString(), "e"),
        Map.entry(Type.EXIT.toString(), "x"),
        Map.entry(Type.WALL.toString(), "#")
    );
    public static String id;
    public static Set<String> idsAlreadyUsed = new HashSet<String>();
    // [North, East, South, West]
    public boolean[] directionsVisited;
    private boolean visitedByStack;

    private Tile(Tile.Type tileType) {
        type = tileType;
        id = generateUniqueId();
        directionsVisited = new boolean[]{false, false, false, false};
        visitedByStack = false;
    }

    protected static Tile fromChar(char charRepresentation) {
        typeStringRepresentation = Character.toString(charRepresentation);

        // Using HashMap to get the Type 
        return new Tile(Type.valueOf(typeCharMap.get(charRepresentation)));
    }

    public Type getType() {
        return type;
    }
    public boolean getVisited() {
        return visitedByStack;
    }
    public void setVisited() {
        visitedByStack = true;
    }


    public boolean isNavigable() {
        // All Types except from WALL can be navigated through
        return !(type == Type.WALL);
    }

    public String toString() {
        return typeToStringMap.get(type.toString());
    }

    public enum Type {
        CORRIDOR,   // .
        ENTRANCE,   // e
        EXIT,       // x
        WALL;       // #
    }

    // Helper Method to Generate a unique id for each Tile 
    // -> so it can be added to a HashMap in Maze
    public static String generateUniqueId() {
        while (true) {
            String possibleId = UUID.randomUUID().toString().substring(0, 8);

            if (idsAlreadyUsed.contains(possibleId)) {
                continue;
            }
            
            return possibleId;
        }
    }
}
