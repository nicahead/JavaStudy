package me.nic.socket.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 使用一个Buffer完成文件的复制
 */
public class NIOFileChannel03 {
    public static void main(String[] args) throws Exception {
        // source
        FileInputStream fis = new FileInputStream("d://file.txt");
        FileChannel inChannel = fis.getChannel();
        // target
        FileOutputStream fos = new FileOutputStream("d://output.txt");
        FileChannel outChannel = fos.getChannel();
        // 创建缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        while (true) {
            // 清空buffer，重置position的位置
            // 由于循环的最后执行了write操作，会将position移动到limit位置
            // 如果没有重置position，那么上次读取后，position和limit位置一样，读取后read的值永远为0
            byteBuffer.clear();
            // 大于0的情况，就是正常的读取数据的长度
            int read = inChannel.read(byteBuffer);
            System.out.println("read=" + read);
            // 表示读完
            if (read == -1) break;
            // 将buffer中的数据写入到outChannel
            byteBuffer.flip();
            outChannel.write(byteBuffer);
        }
        fis.close();
        fos.close();
    }
}
