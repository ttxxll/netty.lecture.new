package com.example.nio;

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author taoxinglong
 * @description
 *  Buffer的Scattering和Gathering：分散和汇集
 *      Scattering：将来自一个channel的的数据read到多个buffer当中
 *      Gathering：将多个buffer的内容write到channel
 * @date 2025-03-29 15:53
 */
public class NioTest11 {
    public static void main(String[] args) throws Exception {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress(8899);
        serverSocketChannel.socket().bind(address);

        int messageLength = 2 + 3 + 4;

        ByteBuffer[] byteBuffers = new ByteBuffer[3];
        byteBuffers[0] = ByteBuffer.allocate(2);
        byteBuffers[1] = ByteBuffer.allocate(3);
        byteBuffers[2] = ByteBuffer.allocate(4);

        // 阻塞，开始监听
        SocketChannel socketChannel = serverSocketChannel.accept();
        while (true) {

            // 从channel读数据到buffers
            int bytesRead = 0;
            while (bytesRead < messageLength) {
                long r = socketChannel.read(byteBuffers);
                bytesRead += r;
                System.out.println("bytesRead: " + bytesRead);

                Arrays.asList(byteBuffers).stream()
                        .map(byteBuffer -> "position: " + byteBuffer.position() + ", limit: " + byteBuffer.limit())
                        .forEach(System.out::println);
            }

            Arrays.asList(byteBuffers).forEach(Buffer::flip);

            // 将buffers写到channel
            long bytesWritten = 0;
            while (bytesWritten < messageLength) {
                long w = socketChannel.write(byteBuffers);
                bytesWritten += w;
            }

            // 清空复原
            Arrays.asList(byteBuffers).forEach(Buffer::clear);
            System.out.println("bytesRead: " + bytesRead + ", bytesWritten: " + bytesWritten + ", messageLength: " + messageLength);
        }
    }
}
