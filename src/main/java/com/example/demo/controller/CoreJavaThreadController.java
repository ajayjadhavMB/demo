package com.example.demo.controller;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.javaprep.thread.Counter;
import com.example.demo.javaprep.thread.DownloadTask;
import com.example.demo.javaprep.thread.MusicTask;
import com.example.demo.javaprep.thread.MyTask;
// import com.example.demo.javaprep.thread.MyThread;
import com.example.demo.javaprep.thread.MyThread;
import com.example.demo.javaprep.thread.NumberPrinter;
import com.example.demo.javaprep.thread.Singleton;

@RestController
@RequestMapping("/core-java-thread")
public class CoreJavaThreadController {
    /**
     * This endpoint demonstrates the lifecycle of a thread in Java. It creates a
     * thread, starts it, puts it to sleep, and then waits for it to finish using
     * join(). The thread states are printed at each stage to illustrate the
     * lifecycle.
     * 
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/thread-lifecycle")
    public String hello() throws InterruptedException {

        // new using Thread class
        // Thread t1 = new MyThread();

        // thread using Runnable interface
        Thread t1 = new Thread(new MyTask());
        System.out.println("state after creation: " + t1.getState());

        // runnable
        t1.start();
        System.out.println("state after start: " + t1.getState());

        // timed waiting
        Thread.sleep(1000);
        System.out.println("state after sleep: " + t1.getState());

        // terminated
        t1.join();
        System.out.println("state after completion: " + t1.getState());

        return "Thread lifecycle demonstration completed. Check the console for thread states.";

    }

    /**
     * This endpoint demonstrates the use of multiple threads in Java. It creates
     * two threads, one for downloading a file and another for playing music. Both
     * threads are started, and the main thread waits for the music thread to finish
     * using join(). The outputs from both threads will be printed in the console,
     * illustrating concurrent execution.
     * 
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/thread-example")
    public String getThreadExample() throws InterruptedException {
        Thread t1 = new DownloadTask();
        Thread t2 = new MusicTask();

        t1.start();

        t2.start();

        // t1.join();

        t2.join();

        return "Thread example demonstration completed. Check the console for thread outputs.";
    }

    /**
     * This endpoint demonstrates the difference between the start() and run()
     * methods in Java threads. It creates a thread using the MyThread class and
     * calls the run() method directly, which executes the code in the main thread.
     * Then, it calls the start() method to properly start a new thread. The console
     * output will show that the run() method is executed in the main thread, while
     * the start() method creates a new thread for execution.
     * 
     * @return
     */
    @GetMapping("/start-vs-run")
    public String getStartVsRun() {

        MyThread myThread = new MyThread();
        myThread.run();// wrong way to start a thread, it will execute in the main thread, not in a new
                       // thread
        System.out.println("Called run() method directly, so it's executed in the main thread.");

        myThread.start();
        return "This is a simple demonstration of the difference between start() and run() methods in Java threads.";
    }

    /**
     * This endpoint demonstrates the race condition problem in Java threads. It
     * creates
     * two threads that increment a shared counter. Due to the lack of
     * synchronization,
     * the final count value will be less than the expected 2000.
     * 
     * @return
     */
    @GetMapping("/race-condition-problem")
    public String getRaceConditionProblem() {
        Counter counter = new Counter();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                counter.increment();
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Final count value: " + counter.getCount());
        return "Race condition problem demonstration completed.";
    }

    /**
     * This endpoint demonstrates how to print odd and even numbers using two
     * threads with synchronization. It creates an instance of the NumberPrinter
     * class, which has synchronized methods for printing odd and even numbers. Two
     * threads are created, one for printing odd numbers and another for printing
     * even numbers. Both threads are started, and the console output will show the
     * interleaved odd and even numbers up to a specified limit.
     * 
     * @return
     */
    @GetMapping("/print-odd-even")
    public String printOddEven() {
        NumberPrinter numberPrinter = new NumberPrinter();
        Thread odd = new Thread(() -> numberPrinter.printOdd());
        Thread even = new Thread(() -> numberPrinter.printEven());

        odd.start();
        even.start();
        return "This endpoint demonstrates how to print odd and even numbers using two threads. Check the console for output.";
    }

    /**
     * check singleton thread safe or not
     */
    @GetMapping("/singleton-thread-safe")
    public String singletonThreadSafe() {
        Thread t1 = new Thread(() -> {
            Singleton singleton = Singleton.getInstance();
            System.out.println("Singleton instance from thread 1: " + singleton);
        });

        Thread t2 = new Thread(() -> {
            Singleton singleton = Singleton.getInstance();
            System.out.println("Singleton instance from thread 2: " + singleton);
        });

        t1.start();
        t2.start();

        return "Singleton thread safety demonstration completed. Check the console for output.";

    }

    /**
     * This endpoint demonstrates the producer-consumer problem using a
     * BlockingQueue in Java. It creates a producer thread that generates integer
     * values and puts them into the queue, and a consumer thread that takes values
     * from the queue and processes them. The producer sleeps for 1 second after
     * producing an item, while the consumer sleeps for 2 seconds after consuming an
     * item. The console output will show the produced and consumed values,
     * illustrating the synchronization between the producer and consumer threads
     * using the BlockingQueue.
     */
    @GetMapping("/producer-consumer-blockingqueue")
    public String producerConsumerBlockingQueue() {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);

        Thread producer = new Thread(() -> {

            int value = 1;

            while (true) {

                try {

                    queue.put(value);

                    System.out.println(
                            "Produced: " + value);

                    value++;

                    Thread.sleep(1000);

                } catch (Exception e) {
                }
            }
        });

        Thread consumer = new Thread(() -> {

            while (true) {

                try {

                    int val = queue.take();

                    System.out.println(
                            "Consumed: " + val);

                    Thread.sleep(2000);

                } catch (Exception e) {
                }
            }
        });

        producer.start();
        consumer.start();

        return "Producer Consumer using BlockingQueue demonstration completed. Check the console for output.";
    }
}