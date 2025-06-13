package com.example.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author taoxinglong
 * @description
 *  ByteBuf相较于ByteBuffer提供了两个指针，一个读指针，一个写指针
 *  每写一次，写指针就往后移动
 *  每读一次，读指针就往后移动
 *  readIndex到writeIndex之间就是可读的数据，即数据实际存储的区域
 *  通过判断writeIndex是否大于readIndex的值来判断是否可读
 *  相较于ByteBuffer，ByteBuf在读写切换时不需要调用flip()来切换
 *
 *  ByteBuf三种实现方式：
 *      堆上
 *      堆外
 *      复合：包含任意个堆上和堆外
 * @date 2025-05-21 18:30
 */
public class ByteBufTest0 {
    public static void main(String[] args) {
        ByteBuf buffer = Unpooled.buffer(10);
        /**
         * writeByte(i)：i整数虽然是4字节，但是writeByte只会写入低位的一个字节，高位的3个字节会被丢弃
         */
        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }

        // 通过索引来访问ByteBuf不会改变读写索引
//        for (int i = 0; i < buffer.capacity(); i++) {
//            System.out.println(buffer.getByte(i));
//        }

        /**
         * readByte()读取一个字节
         * readByte()读取ByteBuf会改变读写索引
         */
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.readByte());
        }
        System.out.println("===================================");

        buffer.clear();
        /**
         * 512 = 0b00000010_00000000
         * 同理，writeByte(i)只会写入低位的一个字节，即
         * 00000000
         * 00000001
         * 00000002
         * 00000003
         * ....
         */
        for (int i = 0; i < 10; i++) {
            buffer.writeByte(512+i);
        }
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.readByte());
        }

    }
}
