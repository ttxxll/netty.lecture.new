package com.example.bytebuf;

/**
 * @author taoxinglong
 * @description
 *  实现ReferenceCounted接口的对象，在引用计数器中会被记录，当引用计数器为0时，调用deallocate()依赖JVM释放对象
 *  如果外层容器对象被释放，则内部对象也会被释放
 * @date 2025-06-18 19:21
 */
public class ReferenceCountedTest {

    public static void main(String[] args) {

    }
}
