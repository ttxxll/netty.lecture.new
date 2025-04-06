package com.example.zerocopy;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author taoxinglong
 * @description TODO
 * @date 2025-04-06 20:11
 */
public class NewIOServer {
    public static void main(String[] args) throws Exception {
        InetSocketAddress address = new InetSocketAddress(8899);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 返回一个与该channel关联的serverSocket
        ServerSocket serverSocket = serverSocketChannel.socket();
        /**
         * 当一个TCP连接被关闭后，连接可能仍会处于timeout状态一段时间。通常称为TIME_WAIT状态或者2MSL等待状态
         * 当应用继续去连接这个ip:port时，可能会因为这个状态，导致无法绑定socket连接到这个地址上
         *
         * serverSocket.setReuseAddress(true); 允许socket绑定到这个address，即使之前的连接仍然处于timeout状态
         */
        serverSocket.setReuseAddress(true);
        serverSocket.bind(address);

        ByteBuffer buffer = ByteBuffer.allocate(4096);
        while (true) {
            // 这里没必要置为非阻塞，因为没有用到Selector
            serverSocketChannel.configureBlocking(true);
            /**
             * 获取一个连接到这个channel的serverSocket上的socket
             * 该方法默认是阻塞的，直到有可用到连接
             * 也可以置为非阻塞的，会直接返回
             *
             * 返回的socket是阻塞的无论channel是不是阻塞模式
             */
            SocketChannel socketChannel = serverSocketChannel.accept();
            int readCount = 0;
            int total = 0;
            while ( -1 != readCount) {
                try {
                    /**
                     * 将socket中数据读到程序的buffer中
                     * 、
                     * 这里就不用allocateDirect方法了，因为是比较客户端的传统和ZeroCopy的IO效率
                     */
                    readCount = socketChannel.read(buffer);
                    total += readCount;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 重置position 重新读到buffer中
                buffer.rewind();
            }
            System.out.println("读取字节数：" + total);
            total = 0;
        }
    }
}
