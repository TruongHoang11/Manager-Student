package JavaNetworking;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class SampleUDFClient {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        byte[] sendData = "Xin chao Hoang to".getBytes();

        InetAddress IDAddress = InetAddress.getByName("localhost");
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IDAddress, 7749);
        socket.send(sendPacket);
        socket.close();
    }
}
