package com.example.fifth;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 与客户端初次建立连接时，是http请求，请求中会携带websocket信息，server读到该信息后会升级成websocket。
        pipeline.addLast(new HttpServerCodec());
        // 以块的形式响应
        pipeline.addLast(new ChunkedWriteHandler());
        // Netty会对请求和响应做分块的处理 这里通过HttpObjectAggregator将一块一块的请求/响应 聚合成一个完整的http请求，http响应FullHttpRequest/FullHttpResponse。避免了多次处理HttpContent
        pipeline.addLast(new HttpObjectAggregator(8192));
        // /ws是websocket协议请求路径content_path： ws://p:port/content_path
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        pipeline.addLast(new WebSocketTextFrameHandler());
    }
}
