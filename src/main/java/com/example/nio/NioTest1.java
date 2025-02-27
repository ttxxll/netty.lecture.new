package com.example.nio;

import java.nio.IntBuffer;
import java.security.SecureRandom;

/**
 * @author taoxinglong
 * @description TODO
 * @date 2025-02-27 21:57
 */
public class NioTest1 {
    public static void main(String[] args) {
        IntBuffer buffer = IntBuffer.allocate(10);
        for (int i = 0; i < buffer.capacity(); i++) {
            int randomInt = new SecureRandom().nextInt(20);
            buffer.put(randomInt);
        }
        buffer.flip();
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
    }
}
