package com.example.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

/**
 * @author taoxinglong
 * @description TODO
 * @date 2025-04-24 10:42
 */
public class Reactor implements Runnable{

    private static final SelectorProvider DEFAULT_SELECTOR_PROVIDER = SelectorProvider.provider();

    /**
     * NIO中更顶层的Channel接口描述：Channel可以看作IO操作的纽带。一个Channel代表着和一个实体的连接，比如硬件设备，文件，网络套接字等。
     * 这类的实体能执行一个或多个IO擦做，比如读/写。
     * Channel的目的主要是为了多线程进行I/O的安全性。
     *
     * 父类都是SelectableChannel，用来监听的socket的服务端channel
     * channel都可以被NIO中的Selector进行多路复用，首先要通过register方法注册到Selector中，并指明注册时Channel关注的事件。
     * Selector会分配给Channel所有资源，Channel保持registered状态，直到被注销。
     * Channel不能被直接注销，而是通过要先取消所有代表注册的SelectKey。在Selector下次的select()方法时，会移除取消的SelectKey，相当于注销Channel
     *
     * 通过close()或者interrupt中断在Channel上执行I/O操作时阻塞的线程，都会关闭Channel，此时也会cancel该Channel所有的SelectKey。
     * 或者是Selector被直接关闭
     * 一个通道最多只能注册到一个Selector中，可以通过isRegistered()方法判断是否已注册
     *
     * SelectableChannel可以被多个并发线程安全的使用
     * Channel默认是阻塞的，可以通过configureBlocking(false)设为非阻塞，非阻塞模式的Channel结合Selector的多路复用是非常强大的
     *
     */
    private ServerSocketChannel serverSocketChannel;

    /**
     * NIO中的多路复用器，尤其是SelectableChannel对象的多路复用器
     * 多路复用：在Java NIO中，多路复用指的是使用一个线程来管理多个Channel的I/O操作，实现是Selector，
     *  通过使用一个线程同时监控多个Channel的I/O事件，如连接就绪，读就绪，写就绪等。避免每个连接都需要独立的线程来处理，进而造成系统资源的大量消耗
     *  在Channel注册到Selector中并指定好感兴趣的事件类型后，Selector可以不断的轮询注册在其上的Channel，检查是否有通道的I/O事件已就绪
     *  一旦就绪可以通过selectedKey()方法获取这些就绪的SelectionKey集合，进而获得SelectionKey绑定的Channel，进而进行I/O事件处理
     *
     * 每次SelectableChannel注册到Selector中都会创建一个SelectKey对象，每个Selector都会维护着3中SelectKey的集合
     * 第一种：此选择器当前所有的SelectKey
     * 第二种：我们一般会先通过selector.select()这个阻塞方法，等待selector中注册的channel有就绪的IO事件。
     *  然后就可以通过selectedKeys获取这些注册的IO事件对应的SelectKey
     * 第三种：已cancelled的SelectKey，但是对应的Channel还没有注销。在下一次selector.select()方法时，cancelled的SelectKey会被移除
     *  SelectionKey.cancel();SelectKey被cancel加到cancelled-set同时Channel和Selector的注册关系也会被解除。也是在下次selector.select()方法生效
     *  只是解除和Selector的注册关系，但是Channel没有被关闭
     * 所以selector.select()方法会带来一些效果：
     *  向selected-key集合中添加SelectKey：准备就绪I/O操作的SelectKey
     *  从selected-key集合中移除SelectKey：已处理好了一个SelectKey，调用keyIterator.remove(); 立即移除
     *  从cancelled-key集合中移除SelectKey：之前cancel的SelectKey
     *
     * selector.keys()：
     *  会阻塞直到注册的Channel中至少有一个已准备好执行其对应兴趣集中的I/O事件
     */
    private Selector selector;

    Reactor(int port) throws IOException {
        selector = DEFAULT_SELECTOR_PROVIDER.openSelector();
        serverSocketChannel = DEFAULT_SELECTOR_PROVIDER.openServerSocketChannel();
        serverSocketChannel.socket().bind(new InetSocketAddress(8899));
        serverSocketChannel.configureBlocking(false);
        // 服务器Channel注册到Selector
        SelectionKey registerKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        registerKey.attach(new Acceptor(serverSocketChannel, selector));
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectedKeys.iterator();
                while (iterator.hasNext()) {
                    dispatch(iterator.next());
                }
                selectedKeys.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void dispatch(SelectionKey key) {
        Acceptor attachment = (Acceptor)key.attachment();
        if(null != attachment) {
            attachment.run();
        }
    }


}
