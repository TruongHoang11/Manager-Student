package JavaNetworking;

import java.io.*;
import java.net.Socket;

public class SampleClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 4953); // ket noi den server o cong 1234
        // gui du lieu den server
        OutputStream output = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(output, true);
        writer.println("Xin chao tu client!");

        // nhan phan hoi tu server
        InputStream input = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String response = reader.readLine();
        System.out.println("Phan hoi tu server: " + response);
        socket.close();
    }
}
