package com.example.thrift;

import com.example.thrift.generated.PersonService;
import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.layered.TFramedTransport;

/**
 * @author taoxinglong
 * @description TODO
 * @date 2025-02-10 21:42
 */
public class ThriftServer {

    public static void main(String[] args) throws Exception {
        // 异步非阻塞服务端
        TNonblockingServerSocket socket = new TNonblockingServerSocket(8899);
        THsHaServer.Args arg = new THsHaServer.Args(socket).minWorkerThreads(2).maxWorkerThreads(4);
        PersonService.Processor<PersonServiceImpl> processor = new PersonService.Processor<>(new PersonServiceImpl());
        arg.protocolFactory(new TCompactProtocol.Factory());
        arg.transportFactory(new TFramedTransport.Factory());
        arg.processorFactory(new TProcessorFactory(processor));
        THsHaServer server = new THsHaServer(arg);
        System.out.println("Thrift Server Started!");
        // 开始监听
        server.serve();
    }
}
