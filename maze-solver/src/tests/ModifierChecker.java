// Version 1.1, Wed 10th March 2021 @ 4:07pm
package tests;

import java.lang.reflect.Modifier;

public class ModifierChecker {

    // ~~~~~~~~~~ Utility Functions ~~~~~~~~~~

    public static void main(String args[]) {
        try {
            Class cls = Class.forName("maze.Maze");
            for (Class innerClass: cls.getDeclaredClasses()) {
                if (innerClass.getName().equals("maze.Maze$Coordinate")) {
                    System.out.println(Modifier.isStatic(
                        innerClass.getModifiers()
                    ));
                    return;
                }
            }
            System.out.println("error");
        } catch (ClassNotFoundException e) {
            System.out.println("error");
        }
    }

}
