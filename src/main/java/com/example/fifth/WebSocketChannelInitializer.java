package com.example.fifth;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * websocket协议是基于html5规范所提出来的一个新的协议，它不是一个全新的协议，是在http基础上诞生的协议。
 * 要想建立server和client之间的websocket长连接，首先需要客户端向服务端发起一个http连接，并且该http连接要携带一个特定的header头
 * server检测到有这个头之后，会进行一个update的动作，将http连接升级位websocket长连接。
 * 升级完成后，服务端和客户端就成功的建立了一个全双工，双向的websocket连接。
 *
 * WebSocketServerProtocolHandler：Netty对websocket协议支持了内置的处理器实现
 */
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
        // /ws是websocket协议请求路径content_path： ws://ip:port/content_path
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        pipeline.addLast(new WebSocketTextFrameHandler());
    }
}
