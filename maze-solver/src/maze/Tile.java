package maze;

import java.util.Map;

public class Tile {
    private Type type;
    public static String typeStringRepresentation;
    public static Map<Character, String> typeCharMap = Map.ofEntries(
        Map.entry('.', Type.CORRIDOR.toString()),
        Map.entry('e', Type.ENTRANCE.toString()),
        Map.entry('x', Type.EXIT.toString()),
        Map.entry('#', Type.WALL.toString())
    );

    private Tile(Tile.Type tileType) {
        type = tileType;
    }

    protected static Tile fromChar(char typeCharRepresentation) {
        typeStringRepresentation = Character.toString(typeCharRepresentation);
        
        // Using HashMap to get the Type 
        return new Tile(Type.valueOf(typeCharMap.get(typeCharRepresentation)));
    }

    public Type getType() {
        return type;
    }

    public boolean isNavigable() {
        // All Types except from WALL can be navigated through
        return !(type == Type.WALL);
    }

    public String toString() {
        // HashMap???
        return typeStringRepresentation;    
    }

    public enum Type {
        CORRIDOR,   // .
        ENTRANCE,   // e
        EXIT,       // x
        WALL;       // #
    }
}
