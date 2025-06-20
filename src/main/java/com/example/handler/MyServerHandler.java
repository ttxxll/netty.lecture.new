package com.example.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class MyServerHandler extends SimpleChannelInboundHandler<Long> {

    /**
     * MyByteToLongDecoder每次解码出一个Long消息放入ByteBuf中，就会触发一次 channelRead0()
     * 服务端每收到一个Long消息都会触发一次 channelRead0()
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {
        System.out.println("from client: " + ctx.channel().remoteAddress() + ", msg = " + msg);
//        ctx.writeAndFlush(9876543210L);
//        ctx.writeAndFlush(9L);
//        ctx.writeAndFlush(8L);
//        ctx.writeAndFlush(7L);
//        ctx.writeAndFlush(6L);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }
}
