package libs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WebLibrary {

    public static String url, serverAdd;

    public static void readConfig(String fileName){
        if (fileName.isEmpty()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(fileName));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                url = reader.readLine();
                serverAdd = reader.readLine();
                reader.close();

                if (!Files.exists(Paths.get(serverAdd)))

                    System.out.println("server is not there");
            } catch (Exception e) {
                System.out.print(e);
            }
        } else {
            System.out.print("file not present");
        }
    }
}
