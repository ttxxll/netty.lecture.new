package com.example.charset;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @author taoxinglong
 * @description 字符集测试
 * @date 2025-03-30 21:11
 */
public class CharsetTest1 {

    public static void main(String[] args) throws Exception{
        // NIO文件操作，先获取文件channel
        RandomAccessFile inputAccessFile = new RandomAccessFile("Charset_Input1.txt", "r");
        RandomAccessFile outputAccessFile = new RandomAccessFile("Charset_Output1.txt", "rw");
        FileChannel inputChannel = inputAccessFile.getChannel();
        FileChannel outputChannel = outputAccessFile.getChannel();

        // 用文件映射Buffer来试试
        // 得到文件字节数
        long length = new File("Charset_Input1.txt").length();
        // 文件内容直接映射到内存，直接在内存处理，最后OS持久化到文件
        MappedByteBuffer mappedByteBuffer = inputChannel.map(FileChannel.MapMode.READ_ONLY, 0, length);

        /**
         * 因为 ISO-8859-1 的编码和解码过程是可逆的，所以在整个操作过程中，文件的字节数据并没有发生改变。尽管原文件是 UTF - 8 编码，
         * 但使用 ISO-8859-1 进行解码和编码后，字节数据保持不变，因此新文件依然是 UTF - 8 编码，内容也就不会出现乱码
         */
        Charset charset = StandardCharsets.ISO_8859_1;
        CharsetDecoder utf8Decoder = charset.newDecoder();
        CharsetEncoder utf8Encoder = charset.newEncoder();

        // 将文件内容先解码得到字符，在编码得到字节，在写到output.txt中
        CharBuffer charBuffer = utf8Decoder.decode(mappedByteBuffer);
        ByteBuffer byteBuffer = utf8Encoder.encode(charBuffer);
        outputChannel.write(byteBuffer);

        inputChannel.close();
        outputChannel.close();
    }
}
