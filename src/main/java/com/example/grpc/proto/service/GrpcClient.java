package com.example.grpc.proto.service;

import com.example.grpc.proto.bean.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.time.LocalDateTime;
import java.util.Iterator;
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
        // 1.一元请求响应
        MyResponse response = stub.getRealNameByUsername(MyRequest.newBuilder().setUsername("zhangsan").build());
        System.out.println("收到响应：" + response.getRealname());

        // 2.服务端流式响应
        Iterator<StudentResponse> studentByAgeResp = stub.getStudentByAge(StudentRequest.newBuilder().setAge(20).build());
        while (studentByAgeResp.hasNext()) {
            StudentResponse studentResponse = studentByAgeResp.next();
            System.out.println(studentResponse.getName() + ", " + studentResponse.getCity());
        }

        // 3.客户端流式请求：必须是异步的stub桩
        StreamObserver<StudentResponseList> streamObserver = new StreamObserver<StudentResponseList>() {
            @Override
            public void onNext(StudentResponseList studentResponseList) {
                studentResponseList.getStudentResponseList().forEach(student -> {
                    System.out.println("age: " + student.getAge() + ", name: " + student.getName() + ", city: " + student.getCity());
                });
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("流式请求结束!");
            }
        };
        // 异步的客户端桩：异步的请求，不能立即结束，会出现请求异步发出去后还没收到响应客户端就结束了。
        StudentServiceGrpc.StudentServiceStub asyncStub = StudentServiceGrpc.newStub(managedChannel);
        StreamObserver<StudentRequest> studentRequestObserver = asyncStub.getStudentListByAges(streamObserver);
        studentRequestObserver.onNext(StudentRequest.newBuilder().setAge(201).build());
        studentRequestObserver.onNext(StudentRequest.newBuilder().setAge(202).build());
        studentRequestObserver.onNext(StudentRequest.newBuilder().setAge(203).build());
        studentRequestObserver.onNext(StudentRequest.newBuilder().setAge(204).build());
        studentRequestObserver.onCompleted();

        // 4.服务端客户端双向流式通信
        StreamObserver<StreamRequest> biTalkStreamObserver = asyncStub.biTalk(new StreamObserver<StreamResponse>() {
            @Override
            public void onNext(StreamResponse streamResponse) {
                System.out.println("收到biTalk响应：" + streamResponse.getRequestInfo());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("biTalk completed!");
            }
        });
        for (int i = 0; i<10; i++) {
            biTalkStreamObserver.onNext(StreamRequest.newBuilder().setRequestInfo(LocalDateTime.now().toString()).build());
            Thread.sleep(1000);
        }

        // 优雅关闭：没有managedChannel.shutdownNow()这行的话，客户端会立即关闭，但是服务端不会立即关闭底层的TCP连接，服务端没有收到客户端断开连接的信息，认为客户端然有可能发送新的请求，所以会继续保持连接，期望持有该连接继续复用后续的请求。
        // 直到服务端发现了客户都已经断开了连接，比如心跳检测等机制。然后服务端会认为这是一个异常的情况，因为我没有收到断开的信息，会抛出异常：java.io.IOException: 远程主机强迫关闭了一个现有的连接。
        managedChannel.shutdown().awaitTermination(5000, TimeUnit.MICROSECONDS);
        // managedChannel.shutdownNow();
    }
}
