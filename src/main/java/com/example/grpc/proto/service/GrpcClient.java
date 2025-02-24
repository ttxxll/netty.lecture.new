package com.example.grpc.proto.service;

import com.example.grpc.proto.bean.MyRequest;
import com.example.grpc.proto.bean.MyResponse;
import com.example.grpc.proto.bean.StudentServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

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
        // 优雅关闭：没有这行的话，客户端会立即关闭，但是服务端可能仍在尝试向客户端发送数据或进行清理操作，所以服务端发生了如下异常。优雅关闭
        // java.io.IOException: 远程主机强迫关闭了一个现有的连接。
        managedChannel.shutdown().awaitTermination(100, TimeUnit.MICROSECONDS);
    }
}
