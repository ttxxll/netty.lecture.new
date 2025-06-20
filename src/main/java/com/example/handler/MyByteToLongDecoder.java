package com.example.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author taoxinglong
 * @description TODO
 * @date 2025-06-19 19:58
 */
public class MyByteToLongDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyByteToLongDecode decode invoked! 可读字节数：" + in.readableBytes());
        if (in.readableBytes() >= 8) {
            long l = in.readLong();
            System.out.println("读取的long：" + l);
            out.add(l);
        }
    }
}
