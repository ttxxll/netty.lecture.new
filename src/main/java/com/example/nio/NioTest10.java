package com.example.nio;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * @author taoxinglong
 * @description
 *  文件锁：共享锁/排他锁
 * @date 2025-03-29 15:53
 */
public class NioTest10 {
    public static void main(String[] args) throws Exception {
        RandomAccessFile randomAccessFile = new RandomAccessFile("NioTest10.txt", "rw");
        FileChannel fileChannel = randomAccessFile.getChannel();

        /**
         * 从3位置开始锁，锁6个长度，true表示共享锁
         * 共享锁：多个程序都可以对锁定的部分read/write
         * 排他锁：只有一个程序能对锁定的部分write
         */
        FileLock lock = fileChannel.lock(3, 6, true);

        System.out.println("valid：" + lock.isValid());
        System.out.println("lock type: " + lock.isShared());
        randomAccessFile.close();
    }
}
