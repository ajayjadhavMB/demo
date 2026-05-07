package com.example.demo.javaprep.thread;

// This class demonstrates how to print odd and even numbers using two threads with synchronization.

public class NumberPrinter {
    int number = 1;
    int limit = 10;

    public synchronized void printOdd() {

        while (number <= limit) {
            while (number % 2 == 0) {

                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            if (number <= limit) {
                System.out.println("odd: " + number++);
            }

            notify();
        }
    }

    public synchronized void printEven() {
        while (number <= limit) {
            while (number % 2 != 0) {

                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            if (number <= limit) {
                System.out.println("even: " + number++);
            }
            notify();

        }
    }

}
