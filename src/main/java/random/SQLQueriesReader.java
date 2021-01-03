package random;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SQLQueriesReader {

    public static String read(String path) {
        try {
            Path fileName = Path.of(path);
            String query = Files.readString(fileName);
            return query;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
