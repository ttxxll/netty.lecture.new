package com.example.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author taoxinglong
 * @description TODO
 * @date 2025-03-23 21:23
 */
public class NioTest8 {
    public static void main(String[] args) throws Exception{
        FileInputStream inputStream = new FileInputStream("input2.txt");
        FileOutputStream outputStream = new FileOutputStream("output2.txt");
        FileChannel inputStreamChannel = inputStream.getChannel();
        FileChannel outputStreamChannel = outputStream.getChannel();

        /**
         * HeapByteBuffer -> ByteBuffer 堆上的对象，并且其中的字节数组也是在堆上的，即JVM进程内的
         * 那么在进行IO操作时，需要和操作系统打交道，首先需要将字节数组中的内从拷贝到操作系统内部开辟的一个空间（内核态？）
         * 然后操作系统在拿着这部分数据和IO设备打交道
         * 多了一次拷贝操作：将JVM堆上的字节数组内容拷贝到堆外的操作系统的某块内存区域中，再与IO设备进行交互
         *
         * 为什么操作系统不直接访问操作堆上的内存（字节数组）：
         *  如果靠操作系统来直接操作这块内存区域，那么应该也是需要JNI native方法（OS原生API）来操作IO设备等
         *  那么字节数组的地址一定要是固定的，但是Java存在GC，
         *  当JNI正在访问这块区域时，发生了GC，这块区域的地址很可能就会变化（标记，压缩，清除等动作就会导致数据的地址发生变化）。
         *  所以才拷贝到堆外，拷贝的成本相比和IO设备交互来说小很多，这点成本相较于操作系统直接操作堆内存的风向而言可以忽略
         *  而且拷贝时也是不允许GC，这点由JVM来保证，比如...安全点？
         */
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        /**
         * DirectByteBuffer -> MappedByteBuffer -> ByteBuffer -> Buffer DirectByteBuffer是堆上的对象
         * unsafe.allocateMemory(size) 申请堆外内存
         * DirectByteBuffer怎么访问到堆外内存：Buffer中的long address属性来指向堆外内存，堆外内存直接与IO设备交互
         * 少了一次拷贝过程
         */
        ByteBuffer buffer = ByteBuffer.allocateDirect(512);
        while (true) {
            buffer.clear();
            int read = inputStreamChannel.read(buffer);
            System.out.println("read: " + read);
            if (-1 == read) {
                break;
            }
            buffer.flip();
            outputStreamChannel.write(buffer);
        }
        inputStreamChannel.close();
        outputStreamChannel.close();
    }
}
