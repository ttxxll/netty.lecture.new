package com.example.nio.case1;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author taoxinglong
 * @description NIO服务端
 * @date 2025-03-30 16:04
 */
public class NioServer {

    private static final HashMap<String, SocketChannel> CLIENT_MAP = new HashMap<>();

    public static void main(String[] args) throws Exception {
        // selector
        Selector selector = Selector.open();
        // serverSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        // 服务端的监听socket
        ServerSocket socket = serverSocketChannel.socket();
        socket.bind(new InetSocketAddress(8899));
        // channel注册到selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            // 阻塞方法
            int number = selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (SelectionKey selectionKey : selectionKeys) {
                try {
                    // 是监听事件
                    if (selectionKey.isAcceptable()) {
                        System.out.println("==============================acceptable event!");
                        ServerSocketChannel server = (ServerSocketChannel)selectionKey.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);
                        String key = "【" + UUID.randomUUID() + "]";
                        CLIENT_MAP.put(key, client);
                    } else if (selectionKey.isReadable()) {
                        System.out.println("==============================readable event!");
                        SocketChannel client = (SocketChannel)selectionKey.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int read = client.read(buffer);
                        if (read > 0) {
                            buffer.flip();
                            Charset charset = StandardCharsets.UTF_8;
                            String receiveMsg = String.valueOf(charset.decode(buffer).array());
                            System.out.println(client + ": " + receiveMsg);
                            // 获取clientKey
                            String clientKey = "";
                            for (Map.Entry<String, SocketChannel> entry : CLIENT_MAP.entrySet()) {
                                if (entry.getValue() == client) {
                                    clientKey = entry.getKey();
                                    break;
                                }
                            }
                            // 发给所有的连接的客户端
                            for (Map.Entry<String, SocketChannel> entry : CLIENT_MAP.entrySet()) {
                                ByteBuffer directBuf = ByteBuffer.allocateDirect(1024);
                                directBuf.put((clientKey + ": " +receiveMsg).getBytes(StandardCharsets.UTF_8));
                                directBuf.flip();
                                entry.getValue().write(directBuf);
                            }

                        } else if (read == -1) {
                            // 客户端连接关闭
                            System.out.println("客户端连接关闭！");
                            // 取消注册
                            selectionKey.cancel();
                            // 关闭通道
                            client.close();
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 取消channel在selector中的注册
                    // selectionKey.cancel();
                    // 这里应该是移除处理完成的SelectionKey，但是没有直接的remove，所以一般是通过迭代器的remove，或者在循环结束置空set
                }
            }
            // 置空set，移除处理过的selectionKey事件
            selectionKeys.clear();
        }

    }
}
