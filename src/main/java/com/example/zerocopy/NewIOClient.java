package com.example.zerocopy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @author taoxinglong
 * @description 50ms
 * @date 2025-04-06 20:54
 */
public class NewIOClient {

    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(8899));
        socketChannel.configureBlocking(true);

        String fileName = "E:\\安装包\\安装包\\inteliJIDEA\\ideaIU-2021.1.3.exe";
        FileInputStream fileInputStream = new FileInputStream(fileName);
        FileChannel fileChannel = fileInputStream.getChannel();

        long start = System.currentTimeMillis();
        /**
         * 将与这个channel关联的文件内容，传输到一个可写的channel中，0~size范围
         * 如果channel有position，会从position位置开始写
         * 很多系统可以从文件系统缓存中将字节直接传输到目标channel，不会实际拷贝它们。
         * 所以这个方法会比传统的方式，循环从源channel读再写到目标channel高效非常多
         */
        long transferCount = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        System.out.println("发送字节数：" + transferCount + "耗时：" + (System.currentTimeMillis() - start));
        fileChannel.close();
    }
}
