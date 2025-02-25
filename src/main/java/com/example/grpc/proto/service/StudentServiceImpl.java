package com.example.grpc.proto.service;

import com.example.grpc.proto.bean.*;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author taoxinglong
 * @description TODO
 * @date 2025-02-24 21:42
 */
public class StudentServiceImpl extends StudentServiceGrpc.StudentServiceImplBase {

    @Override
    public void getRealNameByUsername(MyRequest request, StreamObserver<MyResponse> responseObserver) {
        System.out.println("接收到客户端信息：" + request.getUsername());
        // 在服务器端处理完请求后，通过 onNext 方法将响应数据发送给客户端。对于一元 RPC（即客户端发送一个请求，服务器返回一个响应），通常只需要调用一次 onNext
        responseObserver.onNext(MyResponse.newBuilder().setRealname("张三").build());
        //  在服务器端发送完所有响应数据后，调用 onCompleted 方法，告知客户端响应流已经结束。对于一元 RPC，通常在调用 onNext 之后立即调用 onCompleted
        responseObserver.onCompleted();
    }

    @Override
    public void getStudentByAge(StudentRequest request, StreamObserver<StudentResponse> responseObserver) {
        System.out.println("接收到客户端信息：" + request.getAge());
        responseObserver.onNext(StudentResponse.newBuilder().setName("张三").setAge(20).setCity("北京").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("李四").setAge(30).setCity("天津").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("王五").setAge(20).setCity("成都").build());
        responseObserver.onNext(StudentResponse.newBuilder().setName("赵六").setAge(20).setCity("南京").build());
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<StudentRequest> getStudentListByAges(StreamObserver<StudentResponseList> responseObserver) {
        return new StreamObserver<StudentRequest>() {

            private List<StudentRequest> studentRequestList = new ArrayList<>();

            @Override
            public void onNext(StudentRequest studentRequest) {
                studentRequestList.add(studentRequest);
                System.out.println("接收到客户端的流式请求：" + studentRequest.getAge());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                List<StudentResponse> studentResponses = new ArrayList<>();
                for (StudentRequest studentRequest : studentRequestList) {
                    StudentResponse studentResponse = StudentResponse.newBuilder().setAge(studentRequest.getAge()).setName("王一").setCity("西京").build();
                    studentResponses.add(studentResponse);
                }
                StudentResponseList studentResponseList = StudentResponseList.newBuilder().addAllStudentResponse(studentResponses).build();
                // 这里只有一次onNext是因为客户端是流式请求，但是服务端是一元响应，只在这里响应一次组装好的数据一次。对比下面的双向流式通信
                responseObserver.onNext(studentResponseList);
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<StreamRequest> biTalk(StreamObserver<StreamResponse> responseObserver) {
        return new StreamObserver<StreamRequest>() {

            private int count = 0;

            @Override
            public void onNext(StreamRequest streamRequest) {
                count++;
                System.out.println("接收到客户端的流式请求：" + streamRequest.getRequestInfo());
                // 因为是流式响应，每次回调到onNext方法时，都在其中调用responseObserver.onNext流式响应给客户端
                responseObserver.onNext(StreamResponse.newBuilder().setRequestInfo(UUID.randomUUID().toString()).build());
                // 主动结束本次通信：因为服务端和客户端的onCompleted的执行时机都是等着对方调用其本身的该方法后，才会触发回调。所以必须有一方主动发起关闭
                if (count>=10) {
                    onCompleted();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
