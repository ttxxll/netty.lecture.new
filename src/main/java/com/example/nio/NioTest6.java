package com.example.nio;

import java.nio.ByteBuffer;

/**
 * @author taoxinglong
 * @description Slice Buffer
 * @date 2025-03-23 16:51
 */
public class NioTest6 {

    public static void main(String[] args) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        for (int i = 0; i < buffer.capacity(); i++) {
            buffer.put((byte)i);
        }
        buffer.position(2);
        buffer.limit(6);

        /**
         * sliceBuffer是从buffer的position~limit的一个快照
         * sliceBuffer和buffer底层公用一个数组，但是position和limit等属性独立
         */
        ByteBuffer sliceBuffer = buffer.slice();

        for (int i = 0; i < sliceBuffer.capacity(); i++) {
            byte b = sliceBuffer.get(i);
            b *= 2;
            sliceBuffer.put(i, b);
        }
        buffer.position(0);
        buffer.limit(buffer.capacity());

        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
    }
}
