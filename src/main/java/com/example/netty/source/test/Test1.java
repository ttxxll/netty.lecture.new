package com.example.netty.source.test;

import io.netty.util.NettyRuntime;
import io.netty.util.internal.SystemPropertyUtil;

/**
 * @author taoxinglong
 * @description TODO
 * @date 2025-04-14 21:06
 */
public class Test1 {
    public static void main(String[] args) {
        /**
         * intel处理器：本机是8核，超线程技术后有16个逻辑核心，所以这里得到的是32
         */
        int threadNum = Math.max(1, SystemPropertyUtil.getInt(
                "io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
        System.out.println(threadNum);
    }
}
