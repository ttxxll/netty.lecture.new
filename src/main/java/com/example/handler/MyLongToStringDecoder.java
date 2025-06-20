package com.example.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @author taoxinglong
 * @description 解码器：将Long类型转换为String类型
 * @date 2025-06-20 18:14
 */
public class MyLongToStringDecoder extends MessageToMessageDecoder<Long> {
    @Override
    protected void decode(ChannelHandlerContext ctx, Long msg, List<Object> out) throws Exception {
        System.out.println("MyLongToStringDecoder decode invoked！" + msg);
        out.add(String.valueOf(msg) + "->" + "Decoder");
    }
}
