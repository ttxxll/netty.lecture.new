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
        for (int i = 0; i < 10; i++) {
            buffer.writeByte(i);
        }
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.getByte(i));
        }
    }
}
