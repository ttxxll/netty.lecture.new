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
 *  channel在selector中的注册用SelectionKey对象表示的。一个selector维护着3种selection keys的集合
 *        key set:当前所有注册到selector中的channel
 *        selected-key:
 *        cancelled-key:
 *  注意：一个SelectionKey代表着一个注册到selector上的channel的感兴趣的事件发生，可以是accept，read，write，connect，
 *      SelectionKey不是channel，只是发生这些事件的载体是channel
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

            // channel注册到selector，并且感兴趣的事件是accept
            SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("监听端口：" + ports[i]);
        }

        while (true) {
            /**
             * 阻塞方法：等待在selector中注册的channel至少有一个已经准备好IO操作了，返回准备好IO操作的channel数量
             *
             * 分析一个客户端异常关闭的情况：
             *  比如我用终端窗口结合telnet命令进行连接测试。当我直接叉掉窗口时，此时意味着客户端发生异常关闭。
             *  操作系统会像服务端发送一个TCP连接关闭的通知，这个通知通常表现为一个FIN信号包（TCP协议中用于表示关闭连接到标志）。
             *  当服务端接收到FIN包时，SocketChannel会认为有数据可读，即触发了read事件，channel就进入IO就绪的状态。
             *  于是selector就会检测到这个read事件，select()方法不在阻塞，返回了就绪的channel数量。
             *  同时selectedKeys方法返回了这个read事件对应的SelectionKey的集合。
             * 正确的处理：
             *  收到了这个FIN包后，我们要正确的处理。
             *  此时int read = socketChannel.read(buffer);返回的是-1，根据返回的结果，我们cancel掉channel注册的read事件
             */
            int number = selector.select();
            System.out.println("注册到selector中的channel：" + number);
            /**
             * 返回已经准备好I/O操作的channel所关心的事件selectionKey集合：
             *  比如serverSocketChannel在注册到selector中时关注的是accept事件，当accept事件发生时，即那么当有客户端连接进来时，serverSocketChannel就准备好IO操作了，
             *  selectedKeys方法会返回serverSocketChannel关注的accept事件
             *  每一个事件处理完成后，需要移除，避免重复处理。
             */
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            System.out.println("selectionKeys：" + selectionKeys.size());
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                // 是accept事件
                if (selectionKey.isAcceptable()) {
                    // 监听事件的channel是ServerSocketChannel：serverSocketChannel用于监听的channel，
                    ServerSocketChannel serverSocketChannel = (ServerSocketChannel)selectionKey.channel();
                    // 服务器与客户端建立socket连接的channel对象是SocketChannel：得到Socket连接的channel真正连接的channel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);

                    // socket连接channel注册到selector，并且感兴趣的事件是read
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    // 监听连接事件处理结束了，移除该事件
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
                        // 客户端连接关闭
                        if (read == -1) {
                            System.out.println("客户端连接关闭！");
                            // 取消注册
                            selectionKey.cancel();
                            // 关闭通道
                            socketChannel.close();
                            break;
                        }
                        buffer.flip();
                        socketChannel.write(buffer);
                        readBytes += read;
                    }
                    System.out.println("一共读取：" +  readBytes + ",来自：" + socketChannel);
                    // read事件处理结束了，移除该事件
                    iterator.remove();
                }
            }
        }
    }
}
