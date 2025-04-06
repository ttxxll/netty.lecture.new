package com.example.zerocopy;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;

/**
 * @author taoxinglong
 * @description 3520ms耗时
 * @date 2025-04-06 19:39
 */
public class OldClient {
    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", 8899);
        String fileName = "E:\\安装包\\安装包\\inteliJIDEA\\ideaIU-2021.1.3.exe";
        // inputStream：将数据读取到程序中
        FileInputStream inputStream = new FileInputStream(fileName);
        // socket.getOutputStream()：用于向连接的远程主机发送数据
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        byte[] buffer = new byte[4096];
        long readCount = 0;
        long total = 0;

        long start = System.currentTimeMillis();
        // inputStream.read(buffer) 将文件读到程序的buffer中
        while ((readCount = inputStream.read(buffer)) >= 0) {
            total += readCount;
            // 写到输出流
            dataOutputStream.write(buffer);
        }
        System.out.println("发送总字节数：" + total + "，耗时：" + (System.currentTimeMillis() - start));
        dataOutputStream.close();
        socket.close();
        inputStream.close();
    }
}
