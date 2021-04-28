package maze;

import java.util.Map;
import java.util.UUID;
import java.util.Set;
import java.io.Serializable;
import java.util.HashSet;


public class Tile implements Serializable {
    private Type type;
    /**
     * String representing the tile content.
     */
    public static String typeStringRepresentation;
    /**
     * HashMap that maps a valid tile character to its corresponding 
     * Type using the Enum, Type.
     */
    public static Map<Character, String> typeCharMap = Map.ofEntries(
        Map.entry('.', Type.CORRIDOR.toString()),
        Map.entry('e', Type.ENTRANCE.toString()),
        Map.entry('x', Type.EXIT.toString()),
        Map.entry('#', Type.WALL.toString())
    );
    /**
     * HashMap that maps a Type to its string representation.
     */
    public static Map<String, String> typeToStringMap = Map.ofEntries(
        Map.entry(Type.CORRIDOR.toString(), "."),
        Map.entry(Type.ENTRANCE.toString(), "e"),
        Map.entry(Type.EXIT.toString(), "x"),
        Map.entry(Type.WALL.toString(), "#")
    );
    /**
     * Unique String id of a Tile
     */
    public static String id;
    /**
     * HashSet of all ids already used
     */
    public static Set<String> idsAlreadyUsed = new HashSet<String>();
    /**
     * Boolean array representing the directions already visited for a particular tile,
     * in the following order, [North, East, South, West].
     */
    public boolean[] directionsVisited;
    /**
     * Boolean variable representing whether or not this Tile has been visited by the Stack
     * (i.e. in the RouteFinder class)
     */
    private boolean visitedByStack;

    private Tile(Tile.Type tileType) {
        type = tileType;
        id = generateUniqueId();
        directionsVisited = new boolean[]{false, false, false, false};
        visitedByStack = false;
    }

    /**
     * Method used to instantiate a Tile utilising the HashMap, {@link #typeCharMap}, 
     * to get the corresponding tile Type.
     * @param tileChar character representing the tile content.
     * @return Returns a Tile object.
     */
    protected static Tile fromChar(char tileChar) {
        typeStringRepresentation = Character.toString(tileChar);

        return new Tile(Type.valueOf(typeCharMap.get(tileChar)));
    }
    /**
     * Getter method - to get the type of the Tile object
     * @return Returns the type of a Tile object
     */
    public Type getType() {
        return type;
    }
    /**
     * Getter method - to find out whether the tile has been visited by the Stack 
     * (i.e. in the RouteFinder class).
     * @return Returns {@link #visitedByStack}
     */
    public boolean getVisited() {
        return visitedByStack;
    }
    /**
     * Setter method - to set {@link #visitedByStack} to be true, 
     * once the tile has been first added onto the Stack (i.e. in the RouteFinder class).
     */
    public void setVisited() {
        visitedByStack = true;
    }
    /**
     * All Types except from Type.WALL can be navigated through.
     * @return Returns whether or not you can visit a particular tile.
     */
    public boolean isNavigable() {
        return !(type == Type.WALL);
    }
    /**
     * Retrieves the String representation of the tile content using the HashMap, {@link #typeToStringMap}.
     * @return Returns a String of the tiles content.
     */
    public String toString() {
        return typeToStringMap.get(type.toString());
    }
    /**
     * An Enum containing all the possible tile types.
     */
    public enum Type {
        CORRIDOR,   // .
        ENTRANCE,   // e
        EXIT,       // x
        WALL;       // #
    }

    /**
     * Generated a unique Id using java.util.UUID and a 
     * HashSet to check if the Id is already in use.
     * @return Returns a unique Id.
     */
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
