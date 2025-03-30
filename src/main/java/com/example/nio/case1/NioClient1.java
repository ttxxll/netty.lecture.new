package com.example.nio.case1;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author taoxinglong
 * @description TODO
 * @date 2025-03-30 19:37
 */
public class NioClient1 {
    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8899));

        while (true) {
            // 监听是否有就绪的channel
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            for (SelectionKey selectionKey : selectionKeys) {
                if (selectionKey.isConnectable()) {
                    SocketChannel client = (SocketChannel)selectionKey.channel();
                    // 完成连接建立
                    if (client.isConnectionPending()) {
                        client.finishConnect();
                        ByteBuffer buffer = ByteBuffer.allocate(512);
                        buffer.put((LocalDateTime.now() + " 连接成功").getBytes(StandardCharsets.UTF_8));
                        buffer.flip();
                        client.write(buffer);

                        // 新启一个线程去处理该连接键盘输入等交互
                        ExecutorService threadPool = Executors.newFixedThreadPool(16);
                        threadPool.submit(() -> {
                            while (true) {
                                ByteBuffer directBuf = ByteBuffer.allocate(1024);
                                InputStreamReader inputStreamReader = new InputStreamReader(System.in);
                                BufferedReader reader = new BufferedReader(inputStreamReader);
                                String sendMsg = reader.readLine();
                                directBuf.put(sendMsg.getBytes(StandardCharsets.UTF_8));
                                directBuf.flip();
                                client.write(directBuf);
                            }
                        });

                        // 注册read事件，监听server的返回
                        client.register(selector, SelectionKey.OP_READ);
                    }
                } else if (selectionKey.isReadable()) {
                    SocketChannel client = (SocketChannel)selectionKey.channel();
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    int count = client.read(readBuffer);
                    if (count > 0) {
                        String receiveMsg = new String(readBuffer.array(), 0, count);
                        System.out.println(receiveMsg);
                    } else if (count == -1) {
                        // 连接断开 注销事件，关闭channel
                        System.out.println("连接断开");
                        selectionKey.cancel();
                        socketChannel.close();
                    }
                }
            }
            // 清楚已处理的事件
            selectionKeys.clear();
        }
    }
}
