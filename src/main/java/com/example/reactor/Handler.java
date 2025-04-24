package com.example.reactor;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author taoxinglong
 * @description TODO
 * @date 2025-04-24 16:27
 */
public class Handler implements Runnable{

    private SocketChannel socketChannel;

    private Selector selector;

    private SelectionKey sk;

    ByteBuffer input = ByteBuffer.allocate(64);

    ByteBuffer output = ByteBuffer.allocate(64);

    static final int READING = 0;

    static final int SENDING = 1;

    int state = READING;

    public Handler(Selector selector, SocketChannel socketChannel) throws Exception {
        this.selector = selector;
        this.socketChannel = socketChannel;
        socketChannel.configureBlocking(false);
        sk = socketChannel.register(selector, 0);
        sk.attach(this);
        sk.interestOps(SelectionKey.OP_READ);
        // 使尚未返回的上一个选择操作立即返回
        selector.wakeup();
    }

    boolean inputIsComplete() {
        return false;
    }

    boolean outputIsComplete() {
        return false;
    }

    void process() {}

    @Override
    public void run() {
        try {
            if (state == READING) {
                read();
            } else if (state == SENDING) {
                send();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void read() throws Exception {

    }

    void send() throws Exception {

    }
}
