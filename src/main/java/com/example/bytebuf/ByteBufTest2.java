package com.example.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Iterator;

/**
 * @author taoxinglong
 * @description
 *  ByteBuf三种实现方式：
 *      堆上：被JVM管理，能直接访问内部数组，可快速释放申请。
 *          因为socket再将数据发送给网络上的其他机器时，一定是从直接缓冲来发送，不可能直接从堆缓冲发送
 *          所以每次读写数据时，都要先将数据从堆上拷贝到堆外缓冲区，再进行数据的传递（从堆外拷贝到socket缓冲区）
 *      堆外：堆外，不会占用堆的容量空间。
 *          在进行socket数据传递时性能非常好，因为数据直接位于操作系统的本地内存中，所以不需要从JVM将数据拷贝到直接缓冲区
 *          被操作系统管理，不能直接访问内部数据，需要通过JNI调用C代码来进行数据的读写。
 *          因为直接缓冲区是直接在操作系统内存的，所以内存空间的分配与释放要比堆空间更加复杂，而且速度更慢一些。
 *          为了避免这种复杂的堆外空间的申请和释放，Netty通过内存池来复用堆外空间，减少申请和释放的开销
 *      对于后端的业务编解码：推荐HeapBuf
 *      对于I/O通信线程在读写缓冲区时：推荐DirectBuf
 *
 *      复合：包含任意个堆上和堆外
 *
 * JDK的ByteBuffer和Netty的ByteBuf的区别：
 *   1. ByteBuf采用了读写索引分离的策略，既有读指针也有写指针，读写操作时都会更新维护读写指针
 *   2. ByteBuf读写切换时不需要再调用flip()方法切换
 *   3. ByteBuf创建时预留了一定的空间，避免频繁的扩容。并且容量满了时，再进行写操作，ByteBuf会自动扩容。
 * @date 2025-05-21 18:30
 */
public class ByteBufTest2 {
    public static void main(String[] args) {
        // 符合ByteBuf
        CompositeByteBuf bufs = Unpooled.compositeBuffer();
        ByteBuf headBuf = Unpooled.buffer(10);
        ByteBuf directBuf = Unpooled.directBuffer(8);
        bufs.addComponents(headBuf, directBuf);
        Iterator<ByteBuf> iterator = bufs.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }
}
