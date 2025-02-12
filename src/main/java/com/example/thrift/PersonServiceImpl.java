package com.example.thrift;

import com.example.thrift.generated.DateException;
import com.example.thrift.generated.Person;
import com.example.thrift.generated.PersonService;
import org.apache.thrift.TException;

/**
 * @author taoxinglong
 * @description TODO
 * @date 2025-02-10 21:38
 */
public class PersonServiceImpl implements PersonService.Iface{
    @Override
    public Person getPersonByUsername(String username) throws DateException, TException {
        System.out.println("Got Client Param: " + username);
        Person person = new Person();
        person.setAge(20);
        person.setUsername(username);
        person.setMarried(false);
        return person;
    }

    @Override
    public void savePerson(Person person) throws DateException, TException {
        System.out.println("Got Client param: " + person.toString());
        System.out.println(person.getUsername());
        System.out.println(person.getAge());
        System.out.println(person.isMarried());
    }
}
