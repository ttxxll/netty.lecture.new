package com.example.nio;

import java.nio.ByteBuffer;

/**
 * @author taoxinglong
 * @description 只读Buffer
 * @date 2025-03-23 21:19
 */
public class NioTest7 {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        System.out.println(byteBuffer.getClass());

        for (int i = 0; i < byteBuffer.capacity(); i++) {
            byteBuffer.put((byte) i);
        }
        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();
        System.out.println(readOnlyBuffer.getClass());

        readOnlyBuffer.position(0);
        // java.nio.ReadOnlyBufferException
        readOnlyBuffer.put(0, (byte) 1);
    }
}
