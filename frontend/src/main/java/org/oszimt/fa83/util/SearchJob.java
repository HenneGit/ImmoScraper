package org.oszimt.fa83.util;

import javafx.application.Platform;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SearchJob extends Thread {

    private final Runnable target;

    private volatile boolean exit = false;

    public SearchJob(final Runnable target) {
        this.target = target;
    }

    public void run() {
        while (!exit) {
            System.out.println("Renne");
            try {
                System.out.println("Schlafe");
                Runnable updater = new Runnable() {
                    @Override
                    public void run() {
                       target.run();
                    }
                };
                Platform.runLater(updater);
                long random = getRandomLong();
                System.out.println(random);
                TimeUnit.MILLISECONDS.sleep(random);
            } catch (InterruptedException e) {
                this.exit = true;
                e.printStackTrace();
            }
        }
    }

    public void kill(){
        System.out.println("Stoppe");
        this.exit = true;
    }

    private long getRandomLong(){
        long leftLimit = 50000L;
        long rightLimit = 700000L;
        long generatedLong = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
        return generatedLong;
    }
}
