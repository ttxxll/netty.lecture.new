package com.example.second;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.UUID;

public class MyServerHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("from client: " + ctx.channel().remoteAddress() + ", " + msg);
        // 发送的一定要是string
        // 通过Channel写回，流经完整的Handler链
        ctx.channel().writeAndFlush(UUID.randomUUID().toString());
        // 通过handler关联的Context写回，从下一个Handler开始：事件流回更短，可以利用该特性提升性能
        ctx.writeAndFlush(UUID.randomUUID().toString());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }
}
