package ReflectionAPI;

import java.io.*;

public class GhiDoc {
    public static void main(String[] args) throws FileNotFoundException {
        {
            FileReader fileReader = new FileReader("src/test-file.txt");
            try (BufferedReader reader = new BufferedReader(fileReader)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}