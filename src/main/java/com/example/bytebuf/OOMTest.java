package com.example.bytebuf;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author taoxinglong
 * @description 堆外内存溢出
 * @date 2025-05-29 11:11
 */
public class OOMTest {
    public static void main(String[] args) {
        List<ByteBuffer> buffers = new ArrayList<>();
        while (true) {
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 1024);
            buffers.add(buffer);
        }
    }
}
