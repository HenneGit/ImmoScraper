package org.oszimt.fa83.util;

import javafx.application.Platform;

import java.util.concurrent.TimeUnit;

public class SearchJob extends Thread {

    private final Runnable target;

    private volatile boolean exit = false;

    public SearchJob(final Runnable target) {
        this.target = target;
    }

    public void run() {
        while (!exit) {
            long random = RandomTimeoutSupplier.getNewTimeout();

            Platform.runLater(target);
            try {
                TimeUnit.MILLISECONDS.sleep(random);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void kill() {
        this.exit = true;
    }


}
