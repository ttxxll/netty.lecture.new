package com.example.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.time.LocalDateTime;

/**
 * 处理服务端向客户端响应数据
 */
public class MyClientHandler extends SimpleChannelInboundHandler<Long> {

    /**
     * 客户端每收到一条Long消息，会触发一次 channelRead0()
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("from server: " + ctx.channel().remoteAddress() + ", msg = " + msg);
        ctx.writeAndFlush(LocalDateTime.now().toString());
    }

    /**
     * 客户端连接建立时，发送5个Long消息
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(1234567890L);

        // 发送字符串肯定是发不出去的，因为客户端只添加了LongDecoder
        // ctx.writeAndFlush("hello world");

        // 通过ByteBuf绕过类型检查
        // ctx.writeAndFlush(Unpooled.copiedBuffer("helloworldhelloworld", CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }
}
