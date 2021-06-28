package me.nic.socket.bio;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 客户端
 */
public class TCPClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 6666);
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        Scanner sc = new Scanner(System.in);
        while (true) {
            dos.writeUTF(sc.nextLine());
        }
    }
}
