package com.example.thrift;

import com.alibaba.fastjson.JSON;
import com.example.thrift.generated.Person;
import com.example.thrift.generated.PersonService;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.layered.TFramedTransport;

/**
 * @author taoxinglong
 * @description TODO
 * @date 2025-02-10 21:48
 */
public class ThriftClient {
    public static void main(String[] args) throws Exception {
        try (
                TTransport transport = new TFramedTransport(new TSocket("localhost", 8899), 600)
        ) {
            TCompactProtocol protocol = new TCompactProtocol(transport);
            PersonService.Client client = new PersonService.Client(protocol);

            transport.open();
            Person person = client.getPersonByUsername("张三");
            System.out.println(JSON.toJSONString(person));

            Person p = new Person();
            p.setAge(222);
            p.setUsername("李四");
            p.setMarried(true);
            client.savePerson(p);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
