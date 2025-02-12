package com.example.protobuf;

public class ProtoBufTest {
    public static void main(String[] args) throws Exception{
        DataInfo.Student student = DataInfo.Student.newBuilder().setName("张三").setAge(20).setAddress("北京").build();
        byte[] studentByteArray = student.toByteArray();
        DataInfo.Student studentFromParsed = DataInfo.Student.parseFrom(studentByteArray);

        System.out.println(studentFromParsed.toString());
        System.out.println(studentFromParsed.getName());
        System.out.println(studentFromParsed.getAddress());
        System.out.println(studentFromParsed.getAge());
    }
}
