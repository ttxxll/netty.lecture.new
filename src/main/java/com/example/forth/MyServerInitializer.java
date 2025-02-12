package com.example.forth;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * new IdleStateHandler(5, 7, 3, TimeUnit.SECONDS)
 * 读空闲5s触发事件：与客户端建立连接后，5s没有收到客户端发送的消息，触发读空闲
 * 写空闲7s触发事件：与客户端建立连接后，7s没有回复客户端消息，触发写空闲
 * 读或者写3s触发事件：与客户端建立连接后，3s没有读/写触发读写
 *
 * handlerRemoved
 * 有了这个钩子 为什么还需要心跳检测来判断是否连接断开，举个例子客户端和服务端建立连接后，没有退出，之后开启了飞行模式，这时是不会触发这个狗子的，但是连接已断开
 *
 */
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new IdleStateHandler(5, 7, 3, TimeUnit.SECONDS));
        pipeline.addLast(new MyServerHandler());
    }
}
