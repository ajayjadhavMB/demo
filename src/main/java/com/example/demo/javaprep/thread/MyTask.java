package com.example.demo.javaprep.thread;

public class MyTask implements Runnable {

    @Override
    public void run() {
        try {
            System.out.println("MyTask is running.");
            Thread.sleep(2000);
            System.out.println("MyTask has finished.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}