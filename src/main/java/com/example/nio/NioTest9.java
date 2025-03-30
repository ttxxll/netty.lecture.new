package com.example.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author taoxinglong
 * @description
 *  内存映射Buffer：
 *      是一个直接字节缓冲区，其内容是文件在内存上的映射，也是堆外的直接内存。
 *      通过直接修改内存中的数据，进而作用到磁盘上。不需要操作磁盘IO
 *      缓冲区上的更改生效到磁盘上，由操作系统来执行
 * @date 2025-03-29 15:53
 */
public class NioTest9 {
    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("NioTest9.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0, (byte)'a');
        mappedByteBuffer.put(3, (byte)'b');
        randomAccessFile.close();
    }
}
