package com.example;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AtomicSolution {

    private ExecutorService service = Executors.newCachedThreadPool();
    private AtomicInteger namespaceFlag = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        AtomicSolution solution = new AtomicSolution();
        solution.startProgram();

        Stream<String> strings = Stream.of("one", "two", "three", "four")
//                .filter(e -> e.length() > 3)
                .peek(e -> System.out.println("Filtered value: " + e))
                .map(String::toUpperCase)
                .peek(e -> System.out.println("Mapped value: " + e));
//                .collect(Collectors.toList());

        strings.forEach(s -> System.out.println("Result value: " + s));
    }

    void delay() {
        AtomicInteger integer = new AtomicInteger();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void doWork() {
        putNamespace();
        System.out.println("do something else");
    }

    void startProgram() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            service.submit(this::doWork);
        }
        service.shutdown();
        service.awaitTermination(10, TimeUnit.MINUTES);
    }

    void selectSomething() {
        System.out.println("What do you want to do here");
        Runnable task = () -> {
            System.out.println("hello world");
        };
    }

    void putNamespace() {
        System.out.println("Enter putNamespace: " + Thread.currentThread().getName());
        if (namespaceFlag.get() != 2 && namespaceFlag.compareAndSet(0, 1)) {
//            System.out.println("Trying to put namespace: " + Thread.currentThread().getName());
//            if (namespaceFlag.compareAndSet(0, 1)) {
            System.out.println("Putting namespace: " + Thread.currentThread().getName());
            delay();
            namespaceFlag.set(2);
//            }
        }
    }
}
