package me.nic.socket.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SelectorServer {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel channel = ServerSocketChannel.open();
        Selector selector = Selector.open();
        // 绑定一个端口
        channel.socket().bind(new InetSocketAddress(6666));
        // 设置非阻塞
        channel.configureBlocking(false);
        // 把channel注册到selector，关心事件为OP_ACCEPT（有新的客户端连接）
        SelectionKey register = channel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            // 等待1秒，如果没有事件发生就返回
            if (selector.select(1000) == 0) {
                System.out.println("服务器等待了1秒，无连接");
                continue;
            }
            // 返回的>0，表示已经获取到了关注的事件
            // 获取到相关的selectionKey集合，反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            // 使用迭代器遍历selectionKeys
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                // 根据key对应的通道发生的事件，做相应处理
                // 有新客户端连接
                if (key.isAcceptable()) {
                    // 该客户端生成一个socketChannel
                    SocketChannel socketChannel = channel.accept();
                    System.out.println("客户端连接成功，生成一个socketChannel" + socketChannel.hashCode());
                    // 将socketChannel设置为非阻塞
                    socketChannel.configureBlocking(false);
                    // 将socketChannel注册到selector，关注事件为OP_READ，同时给socketChannel关联一个Buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if (key.isReadable()) {
                    // 通过key反向获取到对应的Channel
                    SocketChannel keyChannel = (SocketChannel) key.channel();
                    // 获取到该channel关联的Buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    keyChannel.read(buffer);
                    System.out.println("from客户端：" + new String(buffer.array()));
                }
                // 手动从集合中移除当前的SelectionKey，防止重复操作
                keyIterator.remove();
            }
        }
    }
}
