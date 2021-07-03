package me.nic.socket.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class NIOFileChannel04 {
    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream("d://file.txt");
        FileChannel inChannel = fis.getChannel();
        FileOutputStream fos = new FileOutputStream("d://copy.txt");
        FileChannel outChannel = fos.getChannel();
        // 使用transferFrom完成拷贝
        outChannel.transferFrom(inChannel, 0, inChannel.size());
        outChannel.close();
        inChannel.close();
        fos.close();
        fis.close();
    }
}
