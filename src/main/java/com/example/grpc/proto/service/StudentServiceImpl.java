package com.example.grpc.proto.service;

import com.example.grpc.proto.bean.*;
import io.grpc.stub.StreamObserver;

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
        //System.out.println("接收到客户端信息：" + request.getAge());
        return super.getStudentListByAges(responseObserver);
    }
}
