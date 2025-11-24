package JavaNetworking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SampleServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(4953); // mo cong 4953
        System.out.println("Server dang cho ket noi ......");
        Socket socket = serverSocket.accept(); // cho client ket noi
        System.out.println("Client da ket noi");
        // nhan du lieu tu client
        InputStream input = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String message = reader.readLine();
        System.out.println("Message tu client: " + message);

        // gui phan hoi den client
        OutputStream output = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(output, true);
        writer.println("Xin chao tu Server!");
        socket.close();
        serverSocket.close();
    }
}
