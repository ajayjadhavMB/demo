package com.example.demo.javaprep.thread;

public class MyThread extends Thread {
    @Override
    public  void run() {

        try {
            System.out.println("MyThread is running.");
            Thread.sleep(2000);
            System.out.println(Thread.currentThread().getName() + " is executing the run method.");
        
        } catch (InterruptedException e) {

            e.printStackTrace();
        }

    }

}
