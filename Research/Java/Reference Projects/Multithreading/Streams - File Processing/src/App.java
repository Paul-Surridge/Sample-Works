import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class App {

    public static void main(String[] args) throws IOException
    {
        
        String sPath = "C:\\Users\\Paul Surridge\\Documents\\Software Development\\...Streams - File Processing\\src\\Names.txt";
        
        Stream<String> objStreamNames = Files.lines(Paths.get(sPath));
        
        objStreamNames.forEach(x->System.out.println(x));
    }
}
