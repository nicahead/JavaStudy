package me.nic.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SelectorClient {
    public static void main(String[] args) throws Exception {
        //得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //提供服务器端的IP和端口
        InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 6666);
        //连接服务器
        if (!socketChannel.connect(socketAddress)) { //如果不成功
            while (!socketChannel.finishConnect()) {
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他工作。。。");
            }
        }

        //如果连接成功，就发送数据
        String str = "hello, world";
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
        //发送数据,实际上就是将buffer数据写入到channel
        socketChannel.write(byteBuffer);
        System.in.read();
    }
}
