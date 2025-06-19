package com.example.bytebuf;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @author taoxinglong
 * @description
 *  AtomicIntegerFieldUpdater和AtomicIntegerUpdater的区别
 *      AtomicInteger和对象实例绑定，每个实例都有一个独立的AtomicInteger
 *      AtomicIntegerFieldUpdater和对象实例无关，所有实例共享一个AtomicIntegerFieldUpdater
 * @date 2025-06-18 20:03
 */
public class AtomicUpdaterTest {
    public static void main(String[] args) {
        Person person = new Person();
//        for (int i = 0; i < 10; i++) {
//            new Thread(() -> {
//                try {
//                    Thread.sleep(20);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                System.out.println(person.age++);
//            }).start();
//        }

        // AtomicInteger和对象实例绑定，每个实例都有一个独立的AtomicInteger
        AtomicInteger atomicInteger = new AtomicInteger(person.age);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(atomicInteger.getAndIncrement());
            }).start();
        }

        // AtomicIntegerFieldUpdater和对象实例无关，所有实例共享一个AtomicIntegerFieldUpdater
        AtomicIntegerFieldUpdater<Person> updater = AtomicIntegerFieldUpdater.newUpdater(Person.class, "age");
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(updater.getAndIncrement(person));
            }).start();
        }
    }
}

class Person {
    volatile int age = 1;
}