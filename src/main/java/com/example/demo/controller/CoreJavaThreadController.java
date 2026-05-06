package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.javaprep.thread.DownloadTask;
import com.example.demo.javaprep.thread.MusicTask;
import com.example.demo.javaprep.thread.MyTask;
// import com.example.demo.javaprep.thread.MyThread;

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

        // t2.join();

        return "Thread example demonstration completed. Check the console for thread outputs.";
    }

}
