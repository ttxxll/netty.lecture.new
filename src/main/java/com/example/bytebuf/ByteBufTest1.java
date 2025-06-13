package com.example.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

public class ByteBufTest1 {
    public static void main(String[] args) {
        ByteBuf byteBuf = Unpooled.copiedBuffer("hello world", CharsetUtil.UTF_8);
        if (byteBuf.hasArray()) {

            // UnpooledByteBufAllocator$InstrumentedUnpooledUnsafeHeapByteBuf(ridx: 0, widx: 11, cap: 33)
            System.out.println("具体实现类：" + byteBuf);

            /**
             * 第一个字节的偏移量：0
             * 读指针
             * 写指针
             * 数组容量
             * 可读字节
             */
            System.out.println(byteBuf.arrayOffset());
            System.out.println(byteBuf.readerIndex());
            System.out.println(byteBuf.writerIndex());
            System.out.println(byteBuf.capacity());
            System.out.println(byteBuf.readableBytes());

            // 堆上buffer
            System.out.println("实际内容长度：" + byteBuf.readableBytes());
            System.out.println("实际数组长度：" + byteBuf.array().length);

            byte[] bytes = byteBuf.array();
            // null 字节 \u0000 转成了可见空格
            String s = new String(bytes, CharsetUtil.UTF_8);
            System.out.println(s);

            byte[] contentBytes = new byte[byteBuf.readableBytes()];
            byteBuf.getBytes(byteBuf.readerIndex(), contentBytes);
            System.out.println(new String(contentBytes, CharsetUtil.UTF_8));
        }


        ByteBuf buf = Unpooled.copiedBuffer("我hello world", CharsetUtil.UTF_8);
        if (buf.hasArray()) {
            for (int i = 0; i < buf.readableBytes(); i++) {
                // 前面的3个字节会乱码：因为'我'是由3个字节构成的，而打印时将每个字节分别转成字符
                System.out.println((char) buf.getByte(i));
            }
            System.out.println(new String(buf.array(), CharsetUtil.UTF_8));
            System.out.println(buf.getCharSequence(0, 3, CharsetUtil.UTF_8));
        }
    }
}
