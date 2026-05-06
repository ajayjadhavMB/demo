package com.example.demo.javaprep.thread;

public class DownloadTask extends Thread {

    @Override
    public void run() {

        for (int i = 1; i <= 5; i++) {

            System.out.println("Downloading file: " + i);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }

        }

    }

}
