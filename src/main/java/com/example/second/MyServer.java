package com.example.second;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 基于TCP连接的Netty服务端和客户端例子
 */
public class MyServer {
    public static void main(String[] args) {
        /**
         * 异步事件循环组：死循环，监听连接.
         * EventLoopGroup（事件循环组）是一种特殊的EventExecutorGroup（事件执行器组），
         * 在事件的循环期间（等待事件的发生，比如连接建立，输入数据的到来等），
         * 允许注册channel到selector，且这些channel可以在之后的selection进行处理
         * 注册channel是异步的，会返回一个ChannelFuture，一旦注册动作完成，ChannelFuture会收到通知
         *
         * 无参构造时，默认会取系统属性io.netty.eventLoopThreads，系统属性也没有会取当前Runtime实例的逻辑核心数*2作为事件循环线程数。
         * 但是一般这里传1较合适，因为parentGroup只用来接收连接，不做处理，将连接分发给childGroup做真正的处理
         *
         * parentGroup：用来处理ServerChannel所有的事件和IO
         * childGroup：用来处理Channel的所有事件和IO（即ClientChannel）
         */
        EventLoopGroup parentGroup = new NioEventLoopGroup();
        EventLoopGroup childGroup = new NioEventLoopGroup();
        try {
            /**
             * ServerBootstrap：用来方便的启动一个ServerChannel。ServerChannel是用来接收accept一个即将到来的连接，accept接收成功后会创建一个子Channel
             * 比如：SocketChannel socketChannel = serverSocketChannel.accept();
             */
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            /**
             * NioServerSocketChannel:是一个ServerSocketChannel的实现，并且是基于NIO Selector的来实现接收新的连接。
             * childHandler：用来服务与Channel的请求
             */
            // childHandler针对SocketChannel的handler：完成请求
            // handler是针对ServerSocketChannel的handler：分发调度
            serverBootstrap.group(parentGroup, childGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    // ChannelInitializer是一种特殊的InboundHandler，在Channel注册到EventLoop时，向ChannelPipeline中添加我们指定的业务Handler
                    // Channel注册完成后，ChannelInitializer这个InboundHandler就会被移除掉
                    .childHandler(new MyServerInitializer());
            /**
             * bind：根据上面channel方法指定的class，channelFactory会创建一个NioServerSocketChannel实例，并将创建的ServerChannel绑定到端口上，准备接收客户端连接
             * 会向ChannelPipeline中添加我们在handler指定的new LoggingHandler(LogLevel.INFO)，
             * 还会添加一个ServerBootstrapAcceptor，用于接收客户端连接
             * Future：代表一个异步计算的结果
             */
            // sync()：必须等待bind()结束才能开始后续监听等步骤
            ChannelFuture channelFuture = serverBootstrap.bind(8899).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 优雅关闭两个事件循环组:释放服务器资源
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }

    }
}
