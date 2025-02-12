package com.example.sixth;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ProtobufServerHandler extends SimpleChannelInboundHandler<MultiMessage.Message> {

    /**
     * 这里接收到的msg已经是被netty内置的protobuf处理器解码过的了
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MultiMessage.Message msg) throws Exception {
        MultiMessage.Message.MessageType messageType = msg.getMessageType();
        /**
         * 其实这里就相当于SpringMVC中的一个一个路由匹配。
         * 我们这里这样写，是因为客户端和服务端有多种消息格式。对应到http接口中，每个http接口的入参相当于是一个单独的消息格式
         */
        if (messageType == MultiMessage.Message.MessageType.Person) {
            MultiMessage.Person person = msg.getPerson();
            System.out.println(person.getName());
            System.out.println(person.getAddress());
            System.out.println(person.getAge());
        } else if (messageType == MultiMessage.Message.MessageType.Dog) {
            MultiMessage.Dog dog = msg.getDog();
            System.out.println(dog.getName());
            System.out.println(dog.getAge());
        } else {
            MultiMessage.Cat cat = msg.getCat();
            System.out.println(cat.getName());
            System.out.println(cat.getCity());
        }
    }
}
