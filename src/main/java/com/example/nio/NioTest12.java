package com.example.nio;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author taoxinglong
 * @description NIO组件之Selector
 *  A selectable channel's registration with a selector is represented by a
 *  SelectionKey object.  A selector maintains three sets of selection keys:
 *  可选channel在selector中的注册用SelectionKey对象表示的。一个selector维护着3种selection keys的集合
 *      key set:当前所有注册到selector中的channel
 *      selected-key:
 *      cancelled-key:
 * @date 2025-03-30 13:39
 */
public class NioTest12 {

    public static void main(String[] args) throws Exception{
        int[] ports = new int[5];
        ports[0] = 5000;
        ports[1] = 5001;
        ports[2] = 5002;
        ports[3] = 5003;
        ports[4] = 5004;

        Selector selector = Selector.open();

        for (int i = 0; i < ports.length; i++) {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            // 阻塞模式置为非阻塞
            serverSocketChannel.configureBlocking(false);
            // 获取一个与这个channel关联的serverSocket
            ServerSocket serverSocket = serverSocketChannel.socket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(ports[i]);
            // serverSocket绑定ip端口
            serverSocket.bind(inetSocketAddress);

            // channel注册到selector
            SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("监听端口：" + ports[i]);
        }

        while (true) {
            /**
             * 阻塞方法：获取在selector中注册的且已经准备好IO操作的channel。用于选择已经准备好进行 I/O 操作的通道
             * 没有注册的通道时会返回0，导致阻塞失效：比如建立了一个连接，又关闭了，那么会循环执行这段代码。因为返回0，不阻塞了
             */
            int number = selector.select();
            System.out.println("注册到selector中的channel：" + number);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("selectionKeys：" + selectionKeys);
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                // 已经准备好接收新的socket连接的channel
                if (selectionKey.isAcceptable()) {
                    // 监听事件的channel是ServerSocketChannel：serverSocketChannel用于监听的channel，
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
                    // 建立socket连接的channel是SocketChannel：得到Socket连接的channel真正连接的channel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);

                    // socket连接channel注册到selector
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    // 移除监听连接事件的channel
                    iterator.remove();
                    System.out.println("获得客户端连接：" + socketChannel);
                } else if (selectionKey.isReadable()) {
                    // 建立socket连接的channel
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();
                    // 读到的数据再写会客户端
                    int readBytes = 0;
                    while (true) {
                        // 每次写512到缓存
                        ByteBuffer buffer = ByteBuffer.allocateDirect(512);
                        buffer.clear();
                        int read = socketChannel.read(buffer);
                        if (read <= 0) {
                            break;
                        }
                        buffer.flip();
                        socketChannel.write(buffer);
                        readBytes += read;
                    }
                    System.out.println("读取：" +  readBytes + ",来自：" + socketChannel);
                    // 移除可读的channel
                    iterator.remove();
                }
            }
        }
    }
}
