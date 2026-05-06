package com.example.demo.javaprep.thread;

public class MyThread extends Thread {
    @Override
    public void run() {

        try {
            System.out.println("MyThread is running.");
            Thread.sleep(2000);
            System.out.println("MyThread has finished.");
        } catch (InterruptedException e) {

            e.printStackTrace();
        }

    }

}
