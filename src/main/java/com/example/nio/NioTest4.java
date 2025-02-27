package com.example.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author taoxinglong
 * @description TODO
 * @date 2025-02-27 21:24
 */
public class NioTest4 {
    public static void main(String[] args) throws Exception{
        FileInputStream inputStream = new FileInputStream("input.txt");
        FileOutputStream outputStream = new FileOutputStream("output.txt");

        FileChannel inputChannel = inputStream.getChannel();
        FileChannel outputChannel = outputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(512);
        while (true) {
            /**
             * 重置状态：position = 0; limit = capacity;
             * 如果没有这行：
             * 第一次循环
             *  inputChannel第一次读完后内容read=57，position = 57，limit = 512
             *  buffer.flip()反转：position = 0，limit = 57
             *  outputChannel：写到outputChannel 57字节，position = 57，limit = 57
             * 第二次循环
             *  没有buffer.clear();
             *  inputChannel继续读，但是position = 57，limit = 57，所以没法再将内容读到buffer中，返回0
             *  0!=-1没有退出循环
             *  buffer.flip()反转：position = 0，limit = 57
             *  outputChannel又可以写57字节了
             * ....
             *  如果有buffer.clear();
             *  buffer.clear()：position = 0; limit = capacity;
             *  inputChannel继续读，但是inputChannel已经读完了，没有内容了，返回-1，结束循环。
             *
             * 所以原因就是：
             *  inputChannel将内容read到buffer中时：inputChannel.read(buffer)
             *      如果position == limit：返回0
             *      如果inputChannel内容已经读完了，返回-1
             */
            buffer.clear();
            // 从inputChannel读到buffer
            int read = inputChannel.read(buffer);
            System.out.println("read: " + read);
            if (-1 == read) {
                break;
            }
            // limit置为position， position置为0
            buffer.flip();
            // 从buffer写到outputChannel
            outputChannel.write(buffer);
        }
        inputChannel.close();
        outputChannel.close();
    }
}
