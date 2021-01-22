package org.oszimt.fa83.util;

public class RandomTimeoutSupplier {

    private static long random;
    private final static RandomTimeoutSupplier INSTANCE = new RandomTimeoutSupplier();

    private RandomTimeoutSupplier() {
    }

    public static RandomTimeoutSupplier getInstance(){
        return INSTANCE;
    }

    public static long getCurrentTimeout(){
        return random;
    }

    public static long getNewTimeout(){
        random = getRandomLong();
        return random;
    }

    private static long getRandomLong(){
        long leftLimit = 50000L;
        long rightLimit = 700000L;
        return leftLimit + (long) (Math.random() * (rightLimit - leftLimit));
    }
}
