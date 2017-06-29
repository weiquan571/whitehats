import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * Created by Jonathan on 6/22/2017.
 */
public class Histo {
    public static void main(String[] args) {
        Path path = Paths.get("C:\\\\Users\\\\Jonathan\\\\Downloads\\\\Day2\\\\ShoppingCartEx1\\\\Design1\\\\wh\\\\src\\\\LionInGrass_868x592_24.bmp");
        try {
            byte[] temp1 = Files.readAllBytes(path);
            HashMap<Byte,Integer> histogram = new HashMap<>();
            for ( Byte b : temp1){
                if (histogram.get(b)!=null){
                    int count = histogram.get(b);
                    histogram.put(b,++count);
                } else {
                    histogram.put(b,1);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
