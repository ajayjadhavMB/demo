package com.example.demo.javaprep.thread;

/**
 * This class implements the Singleton design pattern in Java. It ensures that
 * only
 * one instance of the Singleton class is created and provides a global point of
 * access to that instance. The getInstance() method is synchronized to make it
 * thread-safe, preventing multiple threads from creating multiple instances
 * simultaneously.
 */

public class Singleton {
    /**
     * The volatile keyword ensures that multiple threads handle the instance
     * variable
     * correctly when it is being initialized to the Singleton instance. This is
     * important to prevent issues with the double-checked locking pattern used in
     * the getInstance() method.
     */
    private static volatile Singleton instance;

    private Singleton() {
        // Private constructor to prevent instantiation
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }

}
