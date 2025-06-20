package com.example.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * @author taoxinglong
 * @description 相比于自己实现ByteToMessageDecoder，ReplayingDecoder用起来更简单
 *
 *   public class IntegerHeaderFrameDecoder extends ByteToMessageDecoder {
 *
 *     @Override
 *     protected void decode(ChannelHandlerContext ctx,
 *                             ByteBuf buf, List<Object> out) throws Exception {
 *       // 先读取消息的长度：前4个字节是消息的长度
 *       if (buf.readableBytes() < 4) {
 *          return;
 *       }
 *
 *       // 标记一下当前的readIndex的位置：设置回退点（保存当前读取位置）
 *       buf.markReaderIndex();
 *       int length = buf.readInt();
 *
 *       if (buf.readableBytes() < length) {
 *          // 如果可读长度不够消息的长度，不读，重置readIndex到之前的标记处，不影响后面的读
 *          buf.resetReaderIndex();
 *          return;
 *       }
 *
 *       // 读取消息体放到ByteBuf中
 *       out.add(buf.readBytes(length));
 *     }
 *   }
 * @date 2025-06-20 15:18
 */
public class MyByteToLongDecoder2 extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("ReplayingDecoder decode invoked！");
        // 不需要判断字节是否够读，ReplayingDecoder会帮我们做这个事情
        out.add(in.readLong());
    }
}
