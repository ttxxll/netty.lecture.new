package com.example.second;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * 主要用于初始化通道，向ChannelPipeline添加处理器，它本身也是一个ChannelHandler，添加完成后，会被自动移除
 *
 * 入站：[LengthFieldBasedFrameDecoder] → [StringDecoder] → [MyServerHandler]
 * 出站：[MyServerHandler] ← [StringEncoder] ← [LengthFieldPrepender]
 * 入站和出战是两条独立链路，两条链路上的handler执行顺序不影响
 */
public class MyServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        // 解码器（入栈）：将接收到的字节流（在上一层已经按帧分好）转换为字符串对象。
        /**
         * 解码器（入站）
         *    将接收到的字节流（在上一层已经按帧分好）转换为字符串对象。
         *    int maxFrameLength,
         *    int lengthFieldOffset,
         *    int lengthFieldLength,
         *    int lengthAdjustment,
         *    int initialBytesToStrip
         */
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,
                0, 4, 0, 4));

        /**
         * 编码器（出站）：与LengthFieldBasedFrameDecoder配对使用，用于在发送数据时自动在消息前加上4字节长度前缀。
         * 将帧长度字段添加到帧的前面，这样就可以通过帧长度字段来区分帧的开始和结束
         * 这样接收端就知道每条消息的长度，可以正确解码。
         */
        pipeline.addLast(new LengthFieldPrepender(4));
        // 将接收到的字节流（在上一层已经按帧分好）转换为字符串对象。
        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
        // 将要发送的字符串对象编码为 UTF-8 格式的字节流，供底层网络传输。
        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
        /**
         * 经过上面的处理器，可以保证收到的字节流数据经过解码后得到字符串并且是以长度为前缀的字符串
         * 然后在MyServerHandler中，可以直接处理String类型数据
         */
        pipeline.addLast(new MyServerHandler());
    }
}
