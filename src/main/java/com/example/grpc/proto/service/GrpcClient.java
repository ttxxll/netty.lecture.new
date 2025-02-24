package com.example.grpc.proto.service;

import com.example.grpc.proto.bean.MyRequest;
import com.example.grpc.proto.bean.MyResponse;
import com.example.grpc.proto.bean.StudentServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * @author taoxinglong
 * @description TODO
 * @date 2025-02-24 21:51
 */
public class GrpcClient {

    public static void main(String[] args) throws InterruptedException {
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 8899).usePlaintext().build();
        StudentServiceGrpc.StudentServiceBlockingStub stub = StudentServiceGrpc.newBlockingStub(managedChannel);
        MyResponse response = stub.getRealNameByUsername(MyRequest.newBuilder().setUsername("zhangsan").build());
        System.out.println("收到响应：" + response.getRealname());
        // 优雅关闭：没有这行的话，客户端会立即关闭，但是服务端不会立即关闭底层的TCP连接，服务端没有收到客户端断开连接的信息，认为客户端然有可能发送新的请求，所以会继续保持连接，期望持有该连接继续复用后续的请求。
        // 直到服务端发现了客户都已经断开了连接，比如心跳检测等机制。然后服务端会认为这是一个异常的情况，因为我没有收到断开的信息，会抛出异常：java.io.IOException: 远程主机强迫关闭了一个现有的连接。
        // managedChannel.shutdown().awaitTermination(100, TimeUnit.MICROSECONDS);
        managedChannel.shutdownNow();
    }
}
