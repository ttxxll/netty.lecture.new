package com.example.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 将从客户端读取的字节数据转换为Long类型：入站处理器，解码器
        pipeline.addLast(new MyLongToByteEncoder());
        // 返回时将Long类型数据转换为字节数据：出站处理器，编码器
        pipeline.addLast(new MyByteToLongDecoder());
        pipeline.addLast(new MyServerHandler());
    }
}
