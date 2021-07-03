package me.nic.socket.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * FileChannel使用，字符串写入文件
 */
public class NIOFileChannel01 {
    public static void main(String[] args) throws Exception {
        String str = "hello，尚硅谷";
        // 创建一个输出流
        FileOutputStream fos = new FileOutputStream("d://file.txt");
        // 通过输出流获取对应的FileChannel
        FileChannel channel = fos.getChannel();
        // 创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        // 将数据写入缓冲区
        byteBuffer.put(str.getBytes());
        // 缓冲区由写状态改为读状态
        byteBuffer.flip();
        // 将缓冲区的数据写入channel
        channel.write(byteBuffer);
        channel.close();
        fos.close();
    }
}
