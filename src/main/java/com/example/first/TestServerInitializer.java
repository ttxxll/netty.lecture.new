package com.example.first;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * 客户端和服务端连接建立时会调用initChannel方法：关联请求流程中涉及到的handler
 */
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // netty内置的编解码处理器
        pipeline.addLast("httpServerCodec", new HttpServerCodec());
        // 自定义处理器
        pipeline.addLast("testHttpServerHandler", new TestHttpServerHandler());
    }
}
