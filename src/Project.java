/**
 * Created by Jonathan on 6/22/2017.
 */
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
public class Project {
    public static void main(String[] args) {
        Path path = Paths.get("path/to/file");
        try {
            byte[] data = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
