package ReflectionAPI;

import java.io.*;

public class DocGhi {
    public static void main(String[] args) throws IOException {
        

        try (BufferedWriter writer = new BufferedWriter(new
                FileWriter("src/test-file.txt"))) {
            writer.write("Đây là nội dung được ghi từ App");
            writer.newLine();
            writer.write("Hà nội mùa thu 2024");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
