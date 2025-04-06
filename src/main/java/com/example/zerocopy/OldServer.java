package com.example.zerocopy;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author taoxinglong
 * @description TODO
 * @date 2025-04-06 19:39
 */
public class OldServer {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8899);
        int total = 0;
        while (true) {
            Socket socket = serverSocket.accept();
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            try {
                byte[] buffer = new byte[4096];
                while (true) {
                    int read = dataInputStream.read(buffer, 0, buffer.length);
                    total += read;
                    if (-1 == read) {
                        System.out.println("读取字节数：" + total);
                        total = 0;
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
