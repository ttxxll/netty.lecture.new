package com.example.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * @author taoxinglong
 * @description TODO
 * @date 2025-02-27 22:04
 */
public class NioTest3 {
    public static void main(String[] args) throws Exception{
        FileOutputStream fileOutputStream = new FileOutputStream("NioTest3.txt");
        FileChannel fileChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        byte[] msg = "hello world welcome, nihao".getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < msg.length; i++) {
            byteBuffer.put(msg[i]);
        }
        byteBuffer.flip();
        fileChannel.write(byteBuffer);
        fileOutputStream.close();
    }
}
