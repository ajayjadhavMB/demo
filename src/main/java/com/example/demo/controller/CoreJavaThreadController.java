package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.javaprep.thread.Counter;
import com.example.demo.javaprep.thread.DownloadTask;
import com.example.demo.javaprep.thread.MusicTask;
import com.example.demo.javaprep.thread.MyTask;
// import com.example.demo.javaprep.thread.MyThread;
import com.example.demo.javaprep.thread.MyThread;


@RestController
@RequestMapping("/core-java-thread")
public class CoreJavaThreadController {
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

    @GetMapping("/start-vs-run")
    public String getStartVsRun() {

        MyThread myThread = new MyThread();
        myThread.run();// wrong way to start a thread, it will execute in the main thread, not in a new thread
        System.out.println("Called run() method directly, so it's executed in the main thread.");

        myThread.start();
        return "This is a simple demonstration of the difference between start() and run() methods in Java threads.";
    }

    @GetMapping("/race-condition-problem")
    public  String getRaceConditionProblem(){
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

}
