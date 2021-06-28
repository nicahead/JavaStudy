package me.nic.socket.bio;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务端
 */
public class TCPServer {
    public static void main(String[] args) throws Exception {
        ServerSocket server = new ServerSocket(6666);
        // 服务端，保持启动
        while (true) {
            Socket socket = server.accept();  // 阻塞1，等待客户端建立连接请求
            System.out.println("a client connected, port:" + socket.getPort());
            // 需要开启一个线程读写数据，否则主线程阻塞，其他client无法连接server
            // BIO 每个连接占用一个线程
            new Thread(() -> {
                DataInputStream dis = null;
                try {
                    dis = new DataInputStream(socket.getInputStream());
                    String str = null;
                    while (true) {
                        // 阻塞2，等待客户端发送消息
                        if ((str = dis.readUTF()) != null) {
                            System.out.println("port #" + socket.getPort() + ":" + str);
                        } else {
                            dis.close();
                            socket.close();
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
