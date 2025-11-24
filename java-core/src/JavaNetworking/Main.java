package JavaNetworking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
       BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        String line = reader.readLine();
//        System.out.println("Ban da nhap: " + line);
        char[] buffer = new char[20];
        int num = reader.read(buffer, 0, 10); // đọc 10 ký tự
        System.out.println("Da doc: " + new String(buffer, 0, num));

    }
}
