package com.example.nio;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author taoxinglong
 * @description TODO
 * @date 2025-02-27 22:00
 */
public class NioTest2 {
    public static void main(String[] args) throws Exception{
        FileInputStream inputStream = new FileInputStream("NioTest2.txt");
        FileChannel fileChannel = inputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        int read = fileChannel.read(byteBuffer);
        System.out.println("read: " + read);

        byteBuffer.flip();
        while (byteBuffer.remaining() > 0) {
            byte b = byteBuffer.get();
            System.out.println((char) b);
        }
        fileChannel.close();
    }
}
