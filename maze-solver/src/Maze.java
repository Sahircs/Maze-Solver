// package maze;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;


public class Maze {
    // attributes
    public static void fromTxt(String filePath) throws FileNotFoundException {
        Scanner scan = new Scanner(new FileReader(filePath));

        while (scan.hasNextLine()) {
            System.out.println(scan.nextLine());
        }
    }
}
