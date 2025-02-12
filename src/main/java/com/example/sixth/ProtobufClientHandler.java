package com.example.sixth;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Random;

/**
 * @author taoxinglong
 * @description TODO
 * @date 2025-02-05 21:33
 */
public class ProtobufClientHandler extends SimpleChannelInboundHandler<MultiMessage.Message> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MultiMessage.Message msg) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        while (true) {
            Thread.sleep(3000);
            int random = new Random().nextInt(3);
            MultiMessage.Message message = null;
            if (0 == random) {
                message = MultiMessage.Message.newBuilder().
                        setMessageType(MultiMessage.Message.MessageType.Person).
                        setPerson(MultiMessage.Person.newBuilder().setName("张四").setAge(22).setAddress("北京").build()).
                        build();
            } else if (1 == random) {
                message = MultiMessage.Message.newBuilder().
                        setMessageType(MultiMessage.Message.MessageType.Dog).
                        setDog(MultiMessage.Dog.newBuilder().setName("小狗").setAge(100).build()).
                        build();
            } else {
                message = MultiMessage.Message.newBuilder().
                        setMessageType(MultiMessage.Message.MessageType.Cat).
                        setCat(MultiMessage.Cat.newBuilder().setName("小猫").setCity("上海").build()).
                        build();
            }
            ctx.channel().writeAndFlush(message);
        }

    }
}
