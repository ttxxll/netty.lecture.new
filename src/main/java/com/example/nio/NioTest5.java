package com.example.nio;

import java.nio.ByteBuffer;

/**
 * @author taoxinglong
 * @description TODO
 * @date 2025-03-23 16:43
 */
public class NioTest5 {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        byteBuffer.putInt(15);
        byteBuffer.putLong(500000000L);
        byteBuffer.putDouble(14.123456);
        byteBuffer.putChar('你');
        byteBuffer.putShort((short) 2);
        byteBuffer.putChar('我');

        byteBuffer.flip();

//        while (byteBuffer.hasRemaining()) {
//            // byteBuffer.get() 返回的是一个一个的字节，这样是打印是一个一个的字节
//            System.out.println(byteBuffer.get());
//        }

        // 要这样打印输出
        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getDouble());
        System.out.println(byteBuffer.getChar());
        System.out.println(byteBuffer.getShort());
        System.out.println(byteBuffer.getChar());
    }
}
