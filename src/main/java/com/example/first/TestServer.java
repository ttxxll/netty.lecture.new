package com.example.first;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 启动一个Netty服务器
 */
public class TestServer {

    public static void main(String[] args) {
        // 事件循环组：只用来接收客户端连接，然后指派给worker
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 事件循环组：真正完成请求的工作
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // childHandler针对workerGroup的handler：完成请求
            // handler是针对bossGroup的handler：分发调度
            serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new TestServerInitializer());
            // 服务器启动
            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
