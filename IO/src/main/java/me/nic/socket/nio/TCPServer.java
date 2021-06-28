package me.nic.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;

/**
 * NIO server
 */
public class TCPServer {
    public static void main(String[] args) throws Exception {
        LinkedList<SocketChannel> clients = new LinkedList<>();  // 连接的所有客户端
        ServerSocketChannel ss = ServerSocketChannel.open();  // 服务端开启监听，接收客户端
        ss.bind(new InetSocketAddress(9090));
        ss.configureBlocking(false);  // 非阻塞
        while (true) {
            SocketChannel client = ss.accept(); // ①监听不会阻塞，立即返回，没有连接则为null
            if (client == null) {
//                System.out.println("未连接...");
            } else {
                client.configureBlocking(false);
                System.out.println("a client connected, port:" + client.socket().getPort());
                clients.add(client);
            }
            ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
            // 遍历已经连接的客户端能不能读写数据
            for (SocketChannel c : clients) {
                // ②读取也不会阻塞
                if (c.read(buffer) > 0) {
                    buffer.flip();
                    byte[] bytes = new byte[buffer.limit()];
                    buffer.get(bytes);
                    String str = new String(bytes);
                    System.out.println(c.socket().getPort() + ":" + str);
                    buffer.clear();
                }
            }
        }
    }
}


