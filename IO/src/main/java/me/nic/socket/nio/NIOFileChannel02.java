package me.nic.socket.nio;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * FileChannel使用，从文件中读取数据打印到控制台
 */
public class NIOFileChannel02 {
    public static void main(String[] args) throws Exception {
        File file = new File("d://file.txt");
        // 创建文件的输入流
        FileInputStream fis = new FileInputStream(file);
        // 获取输入流对应的channel
        FileChannel channel = fis.getChannel();
        // 创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        // 将通道的数据读入到buffer
        channel.read(byteBuffer);
        // 将buffer的字节数据转string
        System.out.println(new String(byteBuffer.array()));
        fis.close();
    }
}
