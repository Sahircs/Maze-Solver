// Version 1.1, Wednesday 10th March 2021 @ 4:40pm
package tests.dev.structural;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.DirectoryStream;

import org.junit.Test;
import static org.junit.Assert.*;

public class VisualisationTest {

    private static Path WORKING_DIR = Paths.get("");
    private static Path SRC_ROOT = Paths.get(WORKING_DIR.toString(), "src");

    // ~~~~~~~~~~ Utility Functions ~~~~~~~~~~
    private static Path findVisualisationDir() {
        String[] spellingVariants = {
            "visualisation", "vizualisation", "visualization", "vizualization",
            "visualisations", "vizualisations", "visualizations", "vizualizations"
        };
        Path mazePackageRoot = Paths.get(SRC_ROOT.toString(), "maze");
        for (int i = 0; i < spellingVariants.length; ++i) {
            Path dir = Paths.get(
                mazePackageRoot.toString(), spellingVariants[i]
            ).toAbsolutePath();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                return dir;
            } catch (IOException e){}
        }
        return null;
    }

    // ~~~~~~~~~~ Structural tests ~~~~~~~~~~

    @Test
    public void ensureVisualisationPackage() {
        assertNotEquals(findVisualisationDir(), null);
    }

}
