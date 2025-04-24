package com.example.reactor;

import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author taoxinglong
 * @description TODO
 * @date 2025-04-24 16:25
 */
public class Acceptor implements Runnable {

    private ServerSocketChannel serverSocketChannel;

    private Selector selector;

    Acceptor(ServerSocketChannel serverSocketChannel, Selector selector) {
        this.serverSocketChannel = serverSocketChannel;
        this.selector = selector;
    }

    @Override
    public void run() {
        try {
            SocketChannel socket = serverSocketChannel.accept();
            if (socket != null) {
                new Handler(selector, socket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
